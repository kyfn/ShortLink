package org.lbc.shortlink.project.common.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.lbc.shortlink.project.dto.ValidDate;

/**
 * 有效期注解校验器
 */
public class ValidDateRequiredValidator implements ConstraintValidator<ValidDateRequired, ValidDate> {

    @Override
    public boolean isValid(ValidDate dto, ConstraintValidatorContext context) {
        // 类型为空，交给 @NotNull 处理
        if (dto.getValidDateType() == null) {
            return true;
        }

        // 0-永久有效，validDate 必须为 null
        if (dto.getValidDateType() == 0) {
            if (dto.getValidDate() != null) {
                // 错误定位到 validDate 字段
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("永久有效时不能填写有效期")
                        .addPropertyNode("validDate")
                        .addConstraintViolation();
                return false;
            }
            return true;
        }

        // 1-自定义，validDate 必须不为 null
        if (dto.getValidDateType() == 1) {
            if (dto.getValidDate() == null) {
                // 错误定位到 validDate 字段
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("自定义有效期时必须填写有效期")
                        .addPropertyNode("validDate")
                        .addConstraintViolation();
                return false;
            }
            return true;
        }

        // 其他值，交给业务判断
        return true;
    }
}
