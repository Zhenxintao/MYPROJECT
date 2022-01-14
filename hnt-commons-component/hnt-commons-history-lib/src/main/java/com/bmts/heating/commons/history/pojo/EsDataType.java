package com.bmts.heating.commons.history.pojo;

import org.springframework.context.annotation.Description;

@Description("点数据类型")
public enum EsDataType {

    BOOL(1, "integer"),
    INT(2, "integer"),
    Uint(3, "integer"),
    Long(4, "long"),
    ULong(5, "long"),
    Float(6, "float"),
    Double(7, "double");

    private final int index;
    private final String name;

    EsDataType(int i, String name) {
        this.index = i;
        this.name = name;
    }

    public static String getName(int index) {
        if (index == 1)
            index = 2;
        for (EsDataType c : EsDataType.values()) {
            if (c.index == index) {
                return c.name;
            }
        }
        return null;
    }
}
