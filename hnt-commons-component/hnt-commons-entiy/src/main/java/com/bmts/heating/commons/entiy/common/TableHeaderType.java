package com.bmts.heating.commons.entiy.common;

/**
 * @author naming
 * @description
 * @date 2021/2/3 20:48
 **/
public enum TableHeaderType {
    RealValue(1),
    NetBalance(2);
    int type;

    TableHeaderType(int type) {
        this.type = type;
    }

    public int type() {
        return type;
    }
}
