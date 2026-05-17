package org.lbc.shortlink.project.common.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 自定义有效期校验注解
 * 要在请求参数对象类DTO前加@ValidDateRequired
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidDateRequiredValidator.class)
@Documented
public @interface ValidDateRequired {

    String message() default "自定义有效期时必须填写有效期";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}