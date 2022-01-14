package com.bmts.heating.commons.entiy.baseInfo.enums;


/**
 * 异常所属
 */
public enum AbnormalLevel {

    HEAT_STATION(1, "热力站"),

    HEAT_SOURCE(2, "热源"),

    ;

    int level;
    String name;

    AbnormalLevel(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }
}
