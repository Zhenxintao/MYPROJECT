package com.bmts.heating.commons.entiy.common;

/**
 * @author naming
 * @description
 * @date 2021/4/21 9:57
 **/
public enum WeatherType {
    FORECAST(2),
    REAL(1);
    int value;

    WeatherType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
