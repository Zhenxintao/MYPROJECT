package com.bmts.heating.commons.basement.model.enums;


/**
 * 设备 类型枚举
 */
public enum DeviceType {

    DEVICE_PVSS(1, "PVSS"),
    DEVICE_JK(2, "JK");


    int type;
    String name;

    DeviceType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
