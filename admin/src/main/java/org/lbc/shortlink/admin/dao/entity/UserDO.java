package org.lbc.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.lbc.shortlink.admin.common.database.BaseDO;

/**
 * 用户
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_user")
public class UserDO extends BaseDO {
    /**
     * ID
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String mail;
    /**
     * 注销时间戳
     */
    private Long deleteionTime;
}
