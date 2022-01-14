package com.bmts.heating.grpc.dataCleaning.enums;


/**
 * 系统 类型枚举
 */
public enum SystemType {

    JK_SYSTEM("JK", "JK系统点数据"),
    PVSS_SYSTEM("PVSS", "PVSS系统点数据"),

    ;


    String type;
    String name;

    SystemType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
