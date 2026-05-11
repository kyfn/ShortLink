package org.lbc.shortlink.admin.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserPasswordModifyReqDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;
    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = "^(?![a-zA-Z]+$)(?!\\d+$)(?![^a-zA-Z\\d]+$)[\\S]{6,12}$", message = "密码必须6-12位，且不能纯字母/纯数字/纯特殊符号")
    private String newPassword;
}
