package com.bmts.heating.commons.entiy.baseInfo.enums;


import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 标准点层级
 */
public enum PointStandardLevel {

    POINTSTANDARD_STATION(1, "热力站"),

    POINTSTANDARD_SOURCE(2, "热源"),

    ;

    int level;
    String name;

    PointStandardLevel(int level, String name) {
        this.level = level;
        this.name = name;
    }

    private static final Map<Integer, String> pointStandMap = Stream.of(PointStandardLevel.values()).collect(Collectors.toMap(PointStandardLevel::getLevel, PointStandardLevel::getName));

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public static String getValue(Integer level) {
        return pointStandMap.get(level);
    }

    public static Integer getStandardLevel(String name) {
        AtomicReference<Integer> standardLevel = new AtomicReference<>();
        pointStandMap.forEach((k, v) -> {
            if (Objects.equals(v, name)) {
                standardLevel.set(k);
            }
        });
        return standardLevel.get();
    }
}
