package org.lbc.shortlink.project.test;

import org.lbc.shortlink.project.common.convention.exception.ServiceException;

/**
 * 6 位可逆混淆短码工具类。
 * 容量：62^6 = 56,800,235,584。
 */
public final class Base62Util {

    private static final char[] BASE62 =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public static final long MAX_6_VALUE = 56800235583L;

    private static final int BASE = 62;
    private static final int CODE_LENGTH = 6;
    private static final int HALF_BASE = BASE * BASE * BASE;
    private static final int ROUNDS = 4;

    /**
     * 3+3 位 Feistel 的轮函数参数。
     * 只要 multiplier 与 HALF_BASE 互质即可。
     */
    private static final int[] ROUND_MULTIPLIERS = {1009, 2003, 3001, 4001};
    private static final int[] ROUND_OFFSETS = {7, 19, 41, 73};

    private Base62Util() {
    }

    /**
     * 把正整数转成固定 6 位可逆混淆短码。
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

    private static int roundFunction(int value, int round) {
        return mod(value * ROUND_MULTIPLIERS[round] + ROUND_OFFSETS[round], HALF_BASE);
    }

    private static String toBase62(long value) {
        char[] chars = new char[CODE_LENGTH];
        for (int i = CODE_LENGTH - 1; i >= 0; i--) {
            chars[i] = BASE62[(int) (value % BASE)];
            value = value / BASE;
        }
        return new String(chars);
    }

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

    private static int indexOf(char ch) {
        for (int i = 0; i < BASE62.length; i++) {
            if (BASE62[i] == ch) {
                return i;
            }
        }
        return -1;
    }

    private static int mod(int value, int mod) {
        int result = value % mod;
        return result < 0 ? result + mod : result;
    }

    public static void main(String[] args) {
        String str = "N04cPL";
        long num = Base62Util.decode(str);
        System.out.println("==========" + String.valueOf(num));
    }
}
