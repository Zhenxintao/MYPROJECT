package com.bmts.heating.grpc.dataCleaning.enums;


/**
 * 系统 code 编码
 */
public enum GrpcCode {

    CODE_SUCCESS("SUCCESS", "请求成功!"),
    CODE_FAIL("FAIL", "下发失败!"),
    CODE_MIDDLE("MIDDLE", "请求部分成功部分失败!"),
    CODE_NULL("NULL", "请求数据为空!"),

    ;


    String code;
    String name;

    GrpcCode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
