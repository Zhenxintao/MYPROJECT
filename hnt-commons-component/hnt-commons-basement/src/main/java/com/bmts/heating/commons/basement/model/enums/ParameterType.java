package com.bmts.heating.commons.basement.model.enums;


/**
 * 参量 类型枚举
 */
public enum ParameterType {

    PARAMETER_RUN(1, "运行参数"),
    PARAMETER_COMPUTE(2, "计算参数");


    int type;
    String name;

    ParameterType(int type, String name) {
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
