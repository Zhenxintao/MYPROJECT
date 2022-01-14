package com.bmts.heating.bussiness.common.enums;

/**
 * @Description
 * @Author fei.chang
 * @Date 2020/8/26 14:49
 * @Version 1.0
 */
public enum ResponseCode {

    SUCCESS("200", "请求成功"),
    NOT_DATA("0001", "无数据"),
    PARAM_ERROR("0002", "参数错误"),
    FAIL("9999", "请求失败");

    ResponseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
