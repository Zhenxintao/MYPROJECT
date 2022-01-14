package com.bmts.heating.commons.auth.entity;

public enum ResponseCode {

    SUCCESS(200, "请求成功"),
    NOT_DATA(204, "无数据"),
    PARAM_ERROR(408, "参数错误"),
    FAIL(9999, "请求失败"),
    SERVER_ERROR(500,"服务端响应错误"),
    NOT_FOUND(404, "资源未找到");

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
