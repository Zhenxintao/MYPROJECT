package com.bmts.heating.commons.entiy.second.enums;


import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 二网设备类型枚举
 */
public enum SecondDeviceType {

    /**
     * 设备类型： 1.单元阀门 2.户阀 3.室温
     */
    DEVICE_UNIT_VALVE(1, "uv:"),
    DEVICE_HOUSE_VALVE(2, "hv:"),
    DEVICE_RT(3, "rt:");


    int type;
    String redisKey;

    SecondDeviceType(int type, String redisKey) {
        this.type = type;
        this.redisKey = redisKey;
    }

    private static final Map<Integer, String> DeviceTypeEnums = Stream.of(SecondDeviceType.values()).collect(Collectors.toMap(SecondDeviceType::getType, SecondDeviceType::getName));

    public int getType() {
        return type;
    }

    public String getName() {
        return redisKey;
    }

    public static String getRedisKey(Integer type) {
        return DeviceTypeEnums.get(type);
    }

}
