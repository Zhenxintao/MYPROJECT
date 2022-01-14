package com.bmts.heating.commons.entiy.common;

/**
 * @author naming
 * @description
 * @date 2021/4/20 20:24
 **/
public enum ForecastType {
    STAGE(1),
    HOUR(2),
    Week(3);
    int value;

    ForecastType(int type) {
        this.value = type;
    }

    public int value() {
        return value;
    }
}
