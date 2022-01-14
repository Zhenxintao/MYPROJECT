package com.bmts.heating.commons.entiy.report.enums;


/**
 * 是否调峰
 */
public enum PeakShaveType {

    PEAKSHAVE_NORMAL("正常"),
    PEAKSHAVE_ADJUST("调峰"),
    PEAKSHAVE_FAULT("故障"),

    ;

    String name;

    PeakShaveType(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

}
