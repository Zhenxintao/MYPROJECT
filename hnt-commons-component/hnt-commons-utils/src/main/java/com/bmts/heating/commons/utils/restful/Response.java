package com.bmts.heating.commons.utils.restful;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("响应类")
public class Response implements Serializable {

    public Response() {
        this.setCode(ResponseCode.SUCCESS.getCode());
        this.setMsg(ResponseCode.SUCCESS.getMsg());
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

    public static Response fail(int code,String msg) {
        return new Response(ResponseCode.CODE_Refresh_Token_Exprie,msg);
    }
    public Response(ResponseCode code, Object data, int total) {
        this.code = code.getCode();
        this.msg = code.getMsg();
        this.data = data;
        this.total = total;
    }

    public Response(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @ApiModelProperty("200成功，408参数错误，9999内部错误")
    private int code;
    @ApiModelProperty("描述信息")
    private String msg;
    @ApiModelProperty("响应数据")
    private Object data;
    @JSONField(serialize = false)
    private int total;

    public static Response success() {
        return new Response(ResponseCode.SUCCESS);
    }

    public static Response success(Object data, int total) {
        return new Response(ResponseCode.SUCCESS, data, total);
    }

    public static Response success(Object data) {
        return new Response(ResponseCode.SUCCESS, data);
    }

    public static Response fail() {
        return new Response(ResponseCode.FAIL);
    }

    public static Response warn() {
        return new Response(ResponseCode.WARN);
    }

    public static Response warn(String msg) {
        return new Response(ResponseCode.WARN.getCode(), msg);
    }

    public static Response fail(String msg) {
        return new Response(ResponseCode.FAIL.getCode(), msg);
    }

    public static Response notData() {
        return new Response(ResponseCode.NOT_DATA);
    }

    public static Response paramError() {
        return new Response(ResponseCode.PARAM_ERROR);
    }

    public static Response paramError(String msg) {
        return new Response(ResponseCode.PARAM_ERROR, msg);
    }

    public static Response assemblingResponse(ResponseCode code, Object data) {
        return new Response(code, data);
    }


    public static Response middle() {
        return new Response(ResponseCode.MIDDLE);
    }

    public static Response middle(String msg) {
        return new Response(ResponseCode.MIDDLE.getCode(), msg);
    }
}
