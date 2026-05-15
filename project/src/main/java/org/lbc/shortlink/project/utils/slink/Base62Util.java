package org.lbc.shortlink.project.utils.slink;

import org.lbc.shortlink.project.common.convention.exception.ServiceException;

/**
 * 6 位可逆混淆短码工具类。
 *
 * <p>短链生成流程：
 * <ol>
 *     <li>数据库按 domain 分配一个递增序号，例如 1、2、3。</li>
 *     <li>encode(long) 把递增序号先映射到 0 开始的 raw 空间。</li>
 *     <li>通过 Feistel 网络做可逆混淆，让连续序号在 6 位空间中被打散。</li>
 *     <li>最后把混淆后的数字转成固定 6 位 Base62 字符串。</li>
 * </ol>
 *
 * <p>这里使用的是可逆置换，不是 hash。只要输入序号不重复，输出短码也不会重复。
 * 容量：62^6 = 56,800,235,584。
 */
public final class Base62Util {

    /**
     * Base62 字符表。
     *
     * <p>字符顺序会影响短码结果，已经生成线上短码后不要随意调整。
     */
    private static final char[] BASE62 =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * 6 位 Base62 可表达的最大业务序号。
     *
     * <p>因为业务序号从 1 开始，所以最大值是 62^6 - 1。
     */
    public static final long MAX_6_VALUE = 56800235583L;

    /**
     * Base62 进制。
     */
    private static final int BASE = 62;

    /**
     * 固定输出 6 位短码。
     */
    private static final int CODE_LENGTH = 6;

    /**
     * Feistel 网络把 6 位 Base62 空间拆成左右两半，每半 3 位。
     *
     * <p>HALF_BASE = 62^3，每一半的取值范围是 [0, 62^3)。
     */
    private static final int HALF_BASE = BASE * BASE * BASE;

    /**
     * Feistel 轮数。轮数越多，连续序号被打散得越明显。
     */
    private static final int ROUNDS = 4;

    /**
     * 3+3 位 Feistel 的轮函数参数。
     *
     * <p>只要 multiplier 与 HALF_BASE 互质，每轮映射就能稳定落在半区间内。
     * 这些参数会影响最终短码结果，已经生成线上短码后不要随意调整。
     */
    private static final int[] ROUND_MULTIPLIERS = {1009, 2003, 3001, 4001};
    private static final int[] ROUND_OFFSETS = {7, 19, 41, 73};

    private Base62Util() {
    }

    /**
     * 把正整数转成固定 6 位可逆混淆短码。
     *
     * <p>输入 value 是业务序号，范围是 [1, MAX_6_VALUE]。
     * 内部先减 1 转成 raw，因为 Feistel 空间从 0 开始。
     *
     * @param value 同一 domain 下分配出来的唯一递增序号
     * @return 固定 6 位 Base62 短码
     */
    public static String encode(long value) {
        if (value <= 0 || value > MAX_6_VALUE) {
            throw new ServiceException("短码空间已耗尽，value=" + value);
        }

        long mixed = mix(value - 1);
        return toBase62(mixed);
    }

    /**
     * 反解可逆混淆短码，返回原始序号。
     *
     * <p>这个方法不是短链跳转必须依赖的逻辑，主要用于排查问题、测试可逆性，
     * 或者未来需要从短码反查原始序号时使用。
     *
     * @param code 固定 6 位 Base62 短码
     * @return encode(long) 的原始输入序号
     */
    public static long decode(String code) {
        if (code == null || code.length() != CODE_LENGTH) {
            throw new ServiceException("短码格式不正确，code=" + code);
        }

        long mixed = fromBase62(code);
        long raw = unmix(mixed);
        if (raw < 0 || raw >= MAX_6_VALUE) {
            throw new ServiceException("短码格式不正确，code=" + code);
        }

        return raw + 1;
    }

    /**
     * 正向 Feistel 混淆。
     *
     * <p>raw 的范围是 [0, 62^6)。先拆成：
     * <pre>
     * left  = raw / 62^3
     * right = raw % 62^3
     * </pre>
     * 每轮执行：
     * <pre>
     * nextLeft  = right
     * nextRight = left + F(right)
     * </pre>
     * 最后再把左右两半拼回一个 6 位空间内的数字。
     */
    private static long mix(long raw) {
        int left = (int) (raw / HALF_BASE);
        int right = (int) (raw % HALF_BASE);

        for (int round = 0; round < ROUNDS; round++) {
            int nextLeft = right;
            int nextRight = mod(left + roundFunction(right, round), HALF_BASE);
            left = nextLeft;
            right = nextRight;
        }

        return (long) left * HALF_BASE + right;
    }

    /**
     * 反向 Feistel 解混淆。
     *
     * <p>Feistel 的好处是轮函数 F 本身不需要可逆，只要按相反轮次把左右半区
     * 还原，就能得到 mix(long) 前的 raw。
     */
    private static long unmix(long mixed) {
        int left = (int) (mixed / HALF_BASE);
        int right = (int) (mixed % HALF_BASE);

        for (int round = ROUNDS - 1; round >= 0; round--) {
            int prevRight = left;
            int prevLeft = mod(right - roundFunction(prevRight, round), HALF_BASE);
            left = prevLeft;
            right = prevRight;
        }

        return (long) left * HALF_BASE + right;
    }

    /**
     * Feistel 轮函数。
     *
     * <p>输出必须始终落在 [0, HALF_BASE) 内，所以最后做取模。
     */
    private static int roundFunction(int value, int round) {
        return mod(value * ROUND_MULTIPLIERS[round] + ROUND_OFFSETS[round], HALF_BASE);
    }

    /**
     * 把 0 到 62^6 - 1 之间的数字转成固定 6 位 Base62。
     *
     * <p>高位不足时用 BASE62[0] 补齐，也就是字符 '0'。
     */
    private static String toBase62(long value) {
        char[] chars = new char[CODE_LENGTH];
        for (int i = CODE_LENGTH - 1; i >= 0; i--) {
            chars[i] = BASE62[(int) (value % BASE)];
            value = value / BASE;
        }
        return new String(chars);
    }

    /**
     * 把固定 6 位 Base62 字符串还原成数字。
     */
    private static long fromBase62(String code) {
        long value = 0;
        for (int i = 0; i < code.length(); i++) {
            int idx = indexOf(code.charAt(i));
            if (idx < 0) {
                throw new ServiceException("短码格式不正确，code=" + code);
            }
            value = value * BASE + idx;
        }
        return value;
    }

    /**
     * 查找字符在 Base62 字符表中的下标。
     *
     * <p>返回 -1 表示不是合法 Base62 字符。
     */
    private static int indexOf(char ch) {
        for (int i = 0; i < BASE62.length; i++) {
            if (BASE62[i] == ch) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Java 的 % 在负数场景下会返回负余数。
     *
     * <p>这里统一修正成数学意义上的非负取模，保证结果落在 [0, mod)。
     */
    private static int mod(int value, int mod) {
        int result = value % mod;
        return result < 0 ? result + mod : result;
    }
}
