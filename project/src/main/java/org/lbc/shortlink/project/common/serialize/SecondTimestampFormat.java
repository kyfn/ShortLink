package org.lbc.shortlink.project.common.serialize;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.*;

/**
 * 时间戳秒格式化注解
 * 用于将Long类型的秒级时间戳转换为指定格式的日期时间字符串
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = SecondTimestampSerializer.class)
public @interface SecondTimestampFormat {
    /**
     * 日期时间格式，默认为 "yyyy-MM-dd HH:mm:ss"
     */
    String pattern() default "yyyy-MM-dd HH:mm:ss";
}
