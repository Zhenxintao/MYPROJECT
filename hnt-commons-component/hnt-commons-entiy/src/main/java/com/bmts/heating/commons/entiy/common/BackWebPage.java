package com.bmts.heating.commons.entiy.common;

public enum BackWebPage {
    //全局表格抖动key
    Tablerunconfig("tableRunConfig"),
    //用户表格抖动key
    Tablerunconfigchild("tableRunConfigChild");
    String value;

    BackWebPage(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
