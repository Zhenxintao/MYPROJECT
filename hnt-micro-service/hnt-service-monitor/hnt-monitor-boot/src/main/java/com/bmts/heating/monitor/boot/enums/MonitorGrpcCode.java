package com.bmts.heating.monitor.boot.enums;


/**
 * GRPC 服务请求 code 编码
 */
public enum MonitorGrpcCode {

    CODE_SUCCESS("SUCCESS", "请求成功!"),
    CODE_FAIL("FAIL", "请求失败!"),
    CODE_MIDDLE("MIDDLE", "请求部分成功部分失败!"),
    CODE_NULL("NULL", "请求数据为空!"),

    ;


    String code;
    String name;

    MonitorGrpcCode(String code, String name) {
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
