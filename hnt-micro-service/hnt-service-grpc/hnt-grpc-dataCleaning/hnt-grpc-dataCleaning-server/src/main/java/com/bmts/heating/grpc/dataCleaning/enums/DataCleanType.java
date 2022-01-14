package com.bmts.heating.grpc.dataCleaning.enums;


/**
 * 操作类型枚举
 */
public enum DataCleanType {

    /**
     * 大字节序机器：低地址的bit是高有效位，高地址的bit是低有效位
     * <p>
     * 小字节序机器：低地址的bit是低有效位，高地址的bit是高有效位
     */

    // 大端转小端  ABCD   转  为   DCBA
    BYTE_ORDER_BIG_TO_LITTLE(1, "字节序排序"),
    // ABCD    转为   BADC
    BYTE_ORDER_BIG_TO_SWAP(2, "字节序排序"),
    // ABCD    转为   CDAB
    BYTE_ORDER_LITTLE_TO_SWAP(3, "字节序排序"),

    RANGE_ALARM(4, "超量程警报"),

    ;


    int type;
    String name;

    DataCleanType(int type, String name) {
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
