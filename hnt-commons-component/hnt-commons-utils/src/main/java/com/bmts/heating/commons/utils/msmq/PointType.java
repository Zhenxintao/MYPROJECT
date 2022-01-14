package com.bmts.heating.commons.utils.msmq;

/**
 * 点数据 类型枚举
 */
public enum PointType {
    POINT_BOOL(1, "Bool"),
    POINT_INT(2, "int"),
    POINT_UINT(3, "Uint"),
    POINT_LONG(4, "Long"),
    POINT_ULONG(5, "ULong"),
    POINT_FLOAT(6, "Float"),
    POINT_DOUBLE(7, "Double"),
            ;

    int type;
    String name;

    PointType(int type, String name) {
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
