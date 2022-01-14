package com.bmts.heating.commons.entiy.common;

/**
 * @author naming
 * @description
 * @date 2021/1/31 17:06
 **/
public enum PointProperties {
    ReadOnly(1),
    ReadAndControl(2),
    ControlOnly(3),
    Compute(4);
    int type;

    PointProperties(int type) {
        this.type = type;
    }

    public int type() {
        return type;
    }
}
