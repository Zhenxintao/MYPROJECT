package com.bmts.heating.commons.basement.model.enums;


import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 点数据 类型枚举
 */
public enum DataType {
    // "数据类型： 1-Bool 2-Int 3-Uint 4-Long 5-ULong 6-Float 7-Double"
    POINT_BOOL(1, "boolean"),
    POINT_INT(2, "integer"),
    POINT_UINT(3, "uinteger"),
    POINT_LONG(4, "long"),
    POINT_ULONG(5, "ulong"),
    POINT_FLOAT(6, "float"),
    POINT_DOUBLE(7, "double"),

    ;

    int type;
    String name;

    DataType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    private static final Map<Integer, String> DataTypeEnums = Stream.of(DataType.values()).collect(Collectors.toMap(DataType::getType, DataType::getName));

    private static final Map<String, Integer> EnumDataType = Stream.of(DataType.values()).collect(Collectors.toMap(DataType::getName, DataType::getType));

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static String getValue(Integer type) {
        return DataTypeEnums.get(type);
    }

    public static Integer getValue(String name) {
        return EnumDataType.get(name);
    }
}
