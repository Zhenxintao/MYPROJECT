package com.bmts.heating.commons.utils.enums;


/**
 * 数据 健康状态
 */
public enum HealthStatus {

    /**
     * 0	变量未启用	变量属性默认值	如数据采集时，此变量点无对应的仪表，则健康状态属性为“0”，数据筛选、健康状态检查等数据处理时，不对此变量进行操作；	无恢复操作
     * 1	数据健康	数据采集时，系统数据读取成功，则将“变量健康状态”属性修改为“1”	数据筛选时，该值可能根据筛选结果进行修改。数据计算时，变量健康状态为“1”的数据，均被认为是“正常”数据，可以正常参与计算。	健康值
     * 2	数据缺失	采集系统中，该变量的数值为“NULL”或“0”	该仪表设备断线、离线或完全损坏，需进行排查。	数值恢复后，自动恢复为“1”
     * 3	数据超出测量范围	数据超出预设仪表测量范围	通常为仪表设备损坏	数值不自动恢复，用于标志设备可能的损坏，直至人为确认后恢复为“1”
     * 4	数据超出合理范围	数据的读数低于合理范围下限；或者高于合理范围上限	通常为仪表正常，但是数据处理或转换错误	数值恢复后，自动恢复为“1”
     * 5	异常毛刺	在均值附近正负急剧变化，幅度超过一定限值	仪表、信号、电源的干扰	数值恢复后，自动恢复为“1”
     * 6	其他异常情况
     */

    NOT_ENABLED(0, "变量未启用"),

    HEALTH_DATA(1, "数据健康"),

    MISSING_DATA(2, "数据缺失"),

    OUT_MEASURING_RANGE(3, "设备损坏-数据超出测量范围"),

    OUT_REASONABLE_RANGE(4, "数据异常-数据超出合理范围"),

    EXCEPTION_BURR(5, "异常毛刺"),

    ;


    int status;
    String name;

    HealthStatus(int status, String name) {
        this.status = status;
        this.name = name;
    }

    public int status() {
        return status;
    }

    public String getName() {
        return name;
    }
}
