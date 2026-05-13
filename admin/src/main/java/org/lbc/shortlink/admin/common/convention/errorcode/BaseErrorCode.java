package org.lbc.shortlink.admin.common.convention.errorcode;

public enum BaseErrorCode implements IErrorCode {

    // ========== 一级宏观错误码 客户端错误 ==========
    CLIENT_ERROR("A0000001", "用户端错误"),
    CLIENT_PARAM_ERROR("A0000002", "参数错误"),
    CLIENT_PARAM_FORMAT_ERROR("A0000003", "参数格式错误"),
    CLIENT_REQUEST_METHOD_ERROR("A0000004", "请求方式错误"),
    NAME_NULL("A0000051", "用户名不能为空"),
    PASSWORD_NULL("A0000052", "密码不能为空"),
    EMAIL_NULL("A0000053", "邮箱不能为空"),
    USER_NAME_EXIST("A0000054", "用户名已存在"),

    // ========== 一级宏观错误码 系统执行出错 ==========
    SERVICE_ERROR("B0000001", "系统执行出错"),

    // ========== 一级宏观错误码 调用第三方服务出错 ==========
    REMOTE_ERROR("C0000001", "调用第三方服务出错");

    private final String code;
    private final String message;

    BaseErrorCode(String code, String message) {
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
