package com.bmts.heating.commons.utils.restful;

public enum ResponseCode {

    SUCCESS(200, "请求成功"),
    NOT_DATA(204, "无数据"),
    PARAM_ERROR(408, "参数错误"),
    FAIL(9999, "请稍后……"),
    SERVER_ERROR(500, "服务端响应错误"),
    NOT_FOUND(404, "资源未找到"),
    MIDDLE(333, "请求部分成功部分失败!"),
    WARN(409,"警告"),
    CODE_Refresh_Token_Exprie(203,"refresh_token 过期"),
    CODE_Access_Token_Expire(206,"用户token过期"),
    ;


    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
