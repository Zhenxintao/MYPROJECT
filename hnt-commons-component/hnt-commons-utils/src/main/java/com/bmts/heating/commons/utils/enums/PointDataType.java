package com.bmts.heating.commons.utils.enums;


/**
 * 数据类型枚举
 */
public enum PointDataType {

    /**
     * 数据类型（一网数据、二网数据、用户数据）
     */

    // 一网数据
    FIRST_NET(1, "一网数据"),
    // 二网数据
    SECOND_NET(2, "二网数据"),
    // 用户数据
    USER_NET(3, "用户数据"),

    ;


    int type;
    String name;

    PointDataType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int type() {
        return type;
    }

    public String getName() {
        return name;
    }
}
