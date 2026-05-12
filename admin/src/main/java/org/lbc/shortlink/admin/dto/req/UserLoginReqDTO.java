package org.lbc.shortlink.admin.dto.req;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户登录请求参数对象
 */
@Data
public class UserLoginReqDTO {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
}
