package org.lbc.shortlink.admin.controller;

import lombok.RequiredArgsConstructor;
import org.lbc.shortlink.admin.common.convention.result.Result;
import org.lbc.shortlink.admin.common.convention.result.Results;
import org.lbc.shortlink.admin.dto.req.UserRegisterReqDTO;
import org.lbc.shortlink.admin.dto.resp.UserActualRespDTO;
import org.lbc.shortlink.admin.dto.resp.UserRespDTO;
import org.lbc.shortlink.admin.service.UserService;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制层
 */
@RestController
@RequiredArgsConstructor
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
    public Result<Void> userRegister(@RequestBody UserRegisterReqDTO requestParam) {
        userService.userRegister(requestParam);
        return Results.success();
    }
}
