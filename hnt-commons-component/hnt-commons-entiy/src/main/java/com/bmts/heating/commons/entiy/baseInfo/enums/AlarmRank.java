package com.bmts.heating.commons.entiy.baseInfo.enums;


/**
 * 报警级别
 */
public enum AlarmRank {

    ALARM_RANK_SERIOUS(1, "严重"),

    ALARM_RANK_WARN(2, "警告"),

    ALARM_RANK_COMMON(3, "一般"),

    ;

    int rank;
    String name;

    AlarmRank(int rank, String name) {
        this.rank = rank;
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }
}
