package org.lbc.shortlink.admin.common.enums;

import org.lbc.shortlink.admin.common.convention.errorcode.IErrorCode;

public enum UserErrorCodeEnums implements IErrorCode {

    USER_REG_ERROR("A0000100", "用户注册失败"),
    USER_SAVE_ERROR("A0000100", "用户注册失败"),
    USER_LOGIN_ERROR("A0000200", "用户登录失败"),
    USER_NULL("A0000301", "用户不存在");

    private final String code;
    private final String message;

    UserErrorCodeEnums(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
