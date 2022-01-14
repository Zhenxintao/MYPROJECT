package com.bmts.heating.commons.auth.entity;


public enum  StatusCode {
    CODE_SUCCESS(200,"操作成功"),
    CODE_Error(202,"操作失败"),
    CODE_Access_Token_Expire(206,"用户token过期"),
    CODE_Code_Exprie(207,"code无效"),
    CODE_Server_Error(500,"服务端异常"),
    CODE_ERROR_PHONE(209,"手机号未注册"),
    CODE_SMS_Validate_Error(210,"验证码无效"),
    CODE_SMS_NotAllowedExprie(208,"验证码有效期内");


    private  int code;
    private  String status;
    private StatusCode(int code, String status){
        this.code = code;
        this.status = status;
    }
    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }
}
