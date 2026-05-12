package org.lbc.shortlink.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.lbc.shortlink.admin.common.convention.result.Result;
import org.lbc.shortlink.admin.common.convention.result.Results;
import org.lbc.shortlink.admin.dto.req.UserLoginReqDTO;
import org.lbc.shortlink.admin.dto.req.UserPasswordModifyReqDTO;
import org.lbc.shortlink.admin.dto.req.UserPhoneModifyReqDTO;
import org.lbc.shortlink.admin.dto.req.UserRegisterReqDTO;
import org.lbc.shortlink.admin.dto.resp.UserActualRespDTO;
import org.lbc.shortlink.admin.dto.resp.UserLoginRespDTO;
import org.lbc.shortlink.admin.dto.resp.UserRespDTO;
import org.lbc.shortlink.admin.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制层
 */
@RestController
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    /**
     * 根据用户名查询用户信息
     */
    @GetMapping("/api/slink/v1/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username) {
        UserRespDTO user = userService.getUserByUsername(username);
        return Results.success(user);
    }

    /**
     * 根据用户名查询无脱敏用户信息
     */
    @GetMapping("/api/slink/v1/actual/user/{username}")
    public Result<UserActualRespDTO> getActualUserByUsername(@PathVariable("username") String username) {
        UserActualRespDTO user = userService.getActualUserByUsername(username);
        return Results.success(user);
    }

    /**
     * 查询用户名是否已存在
     * @param username 用户名
     * @return 存在返回报错信息，不存在返回可用信息
     */
    @GetMapping("/api/slink/v1/user/exists")
    public Result<Void> checkUsernameExists(@RequestParam String username) {
        userService.checkUsernameExists(username);
        return Results.success("用户名可用");
    }

    /**
     * 用户注册
     * @param requestParam 注册参数
     * @return 任意
     */
    @PostMapping("/api/slink/v1/user")
    public Result<Void> register(@RequestBody UserRegisterReqDTO requestParam) {
        userService.register(requestParam);
        return Results.success();
    }

    /**
     * 用户修改密码
     * @param requestParam 修改参数
     * @return 任意
     */
    @PutMapping("/api/slink/v1/user/password")
    public Result<Void> modifyPassword(@RequestBody @Valid UserPasswordModifyReqDTO requestParam) {
        userService.modifyPassword(requestParam);
        return Results.success();
    }

    /**
     * 用户修改手机号
     * @param requestParam 修改参数
     * @return 任意
     */
    @PutMapping("/api/slink/v1/user/phone")
    public Result<Void> modifyPhone(@RequestBody UserPhoneModifyReqDTO requestParam) {
        // TODO 参数注解校验
        userService.modifyPhone(requestParam);
        return Results.success();
    }

    /**
     * 用户登录
     * @param requestParam 登录参数
     * @return token 数据
     */
    @PostMapping("/api/slink/v1/user/login")
    public Result<UserLoginRespDTO> login(@RequestBody @Valid UserLoginReqDTO requestParam) {
        UserLoginRespDTO loginRes = userService.login(requestParam);
        return Results.success(loginRes);
    }

    /**
     * 检查用户是否登录
     * @param  username 用户名
     * @param  token 用户token
     * @return true 登录 false 未登录
     */
    @GetMapping("/api/slink/v1/user/check-login")
    public Result<Boolean> checkLogin(@RequestParam String username, @RequestParam String token) {
        return Results.success(userService.checkLogin(username, token));
    }

    @DeleteMapping("/api/slink/v1/user/loginout")
    public Result<Void> logout(@RequestParam String username, @RequestParam String token) {
        userService.loginout(username, token);
        return Results.success();
    }
}
