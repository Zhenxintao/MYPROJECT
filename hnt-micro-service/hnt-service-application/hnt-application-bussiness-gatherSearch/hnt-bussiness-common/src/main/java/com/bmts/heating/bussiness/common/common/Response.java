package com.bmts.heating.bussiness.common.common;

import com.alibaba.fastjson.annotation.JSONField;
import com.bmts.heating.bussiness.common.enums.ResponseCode;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Description
 * @Author fei.chang
 * @Date 2020/8/26 14:47
 * @Version 1.0
 */
@Data
@ApiModel("响应类")
public class Response {

    public Response() {

    }

    public Response(ResponseCode code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    public Response(ResponseCode code, Object data) {
        this.code = code.getCode();
        this.msg = code.getMsg();
        this.data = data;
    }
    public Response(ResponseCode code, Object data,int total) {
        this.code = code.getCode();
        this.msg = code.getMsg();
        this.data = data;
        this.total=total;
    }
    public Response(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private String code;

    private String msg;

    private Object data;
    @JSONField(serialize = false)
    private int total;
    public static Response success() {
        return new Response(ResponseCode.SUCCESS);
    }

    public static Response success(Object data,int total) {
        return new Response(ResponseCode.SUCCESS, data,total);
    }
    public static Response success(Object data) {
        return new Response(ResponseCode.SUCCESS, data);
    }
    public static Response fail() {
        return new Response(ResponseCode.FAIL);
    }

    public static Response notData() {
        return new Response(ResponseCode.NOT_DATA);
    }

    public static Response paramError() {
        return new Response(ResponseCode.PARAM_ERROR);
    }

}
