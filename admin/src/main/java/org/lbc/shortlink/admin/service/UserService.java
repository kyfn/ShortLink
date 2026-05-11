package org.lbc.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.lbc.shortlink.admin.dao.entity.UserDO;
import org.lbc.shortlink.admin.dto.req.UserPasswordModifyReqDTO;
import org.lbc.shortlink.admin.dto.req.UserPhoneModifyReqDTO;
import org.lbc.shortlink.admin.dto.req.UserRegisterReqDTO;
import org.lbc.shortlink.admin.dto.resp.UserActualRespDTO;
import org.lbc.shortlink.admin.dto.resp.UserRespDTO;

/**
 * 用户接口层
 */
public interface UserService extends IService<UserDO> {
    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户返回对象
     */
    UserRespDTO getUserByUsername(String username);
    /**
     * 根据用户名获取无脱敏用户信息
     * @param username 用户名
     * @return 用户无脱敏数据
     */
    UserActualRespDTO getActualUserByUsername(String username);

    /**
     * 查询用户名是否注册
     * @param username 用户名
     */
    void checkUsernameExists(String username);

    /**
     * 用户注册
     * @param requestParam 注册参数
     */
    void userRegister(UserRegisterReqDTO requestParam);

    /**
     * 用户密码修改
     * @param requestParam {username oldPassword newPassword}
     */
    void userPasswordModify(UserPasswordModifyReqDTO requestParam);

    /**
     * 用户手机号修改
     * @param requestParam {username phone code}
     */
    void userPhoneModify(UserPhoneModifyReqDTO requestParam);
}
