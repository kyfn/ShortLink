package org.lbc.shortlink.admin.common.enums;

import org.lbc.shortlink.admin.common.convention.errorcode.IErrorCode;

public enum ServiceErrorCodeEnums implements IErrorCode {

    SERVICE_SQL_MODIFY_ERROR("B0000100", "修改失败");

    private final String code;
    private final String message;

    ServiceErrorCodeEnums(String code, String message) {
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
