package com.bmts.heating.commons.entiy.baseInfo.enums;


/**
 * 报警状态
 */
public enum AlarmStatus {

    // 1 报警 ，2 确认 ，3 恢复
    ALARM_STATUS_ALARM(1, "报警"),

    ALARM_STATUS_CONFIRM(2, "确认"),

    ALARM_STATUS_RECOVER(3, "恢复"),

    ;

    int status;
    String name;

    AlarmStatus(int status, String name) {
        this.status = status;
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }
}
