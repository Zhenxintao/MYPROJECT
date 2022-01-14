package com.bmts.heating.commons.entiy.baseInfo.enums;


import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 网测类型枚举
 */
public enum NetFlagType {

    /**
     * 网测类型 0.公用 1.一次侧 2.二次侧
     */
    NETFLAG_PUBLIC(0, "公共"),

    NETFLAG_ONCE(1, "一次侧"),

    NETFLAG_SECONDARY(2, "二次侧");

    int type;
    String name;

    NetFlagType(int type, String name) {
        this.type = type;
        this.name = name;
    }

    private static final Map<Integer, String> NetFlagEnumMap = Stream.of(NetFlagType.values()).collect(Collectors.toMap(NetFlagType::type, NetFlagType::getName));

    public int type() {
        return type;
    }

    public String getName() {
        return name;
    }


    public static String getValue(Integer type) {
//        Map<String, NetFlagType> enumMap = Stream.of(NetFlagType.values()).collect(Collectors.toMap(Enum::name, Function.identity()));
        return NetFlagEnumMap.get(type);
    }

    public static Integer getNetFlag(String name) {
        AtomicReference<Integer> flagType = new AtomicReference<>();
        NetFlagEnumMap.forEach((k, v) -> {
            if (Objects.equals(v, name)) {
                flagType.set(k);
            }
        });
        return flagType.get();
    }

}
