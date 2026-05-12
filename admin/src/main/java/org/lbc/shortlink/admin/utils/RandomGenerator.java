package org.lbc.shortlink.admin.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public class RandomGenerator {
    private static final Snowflake SNOWFLAKE = IdUtil.getSnowflake(1, 1);
    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String generate() {
        long id = SNOWFLAKE.nextId();
        return toBase62(id);  // 约 11 位
    }

    private static String toBase62(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(BASE62.charAt((int)(num % 62)));
            num /= 62;
        }
        return sb.reverse().toString();
    }
}
