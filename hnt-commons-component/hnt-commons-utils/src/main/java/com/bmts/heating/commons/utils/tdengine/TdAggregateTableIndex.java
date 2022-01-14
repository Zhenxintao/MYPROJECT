package com.bmts.heating.commons.utils.tdengine;

/**
 * TD聚合数据类型
 * */
public enum TdAggregateTableIndex {
    //Td换热站天能耗汇聚数据表
    STATION_ENERGY_DAY("station_energy_day"),
    //Td换热站小时能耗汇聚数据表
    STATION_ENERGY_HOUR("station_energy_hour"),
    //Td热源天能耗汇聚数据表
    SOURCE_ENERGY_DAY("source_energy_day"),
    //Td热源小时能耗汇聚数据表
    SOURCE_ENERGY_HOUR("source_energy_hour"),
    //Td换热站分钟汇聚数据
    STATION_MINUTE("station_minute"),
    //Td换热站小时汇聚数据
    STATION_HOUR("station_hour"),
    //Td热源分钟汇聚数
    SOURCE_MINUTE("source_minute"),
    //Td热源小时汇聚数据
    SOURCE_HOUR("source_hour"),
    //Td换热站天平均数据表
    STATION_DAY_AVG("station_day_avg"),
    //Td换热站小时平均数据表
    STATION_HOUR_AVG("station_hour_avg"),
    //Td换热站天平均数据表
    SOURCE_DAY_AVG("source_day_avg"),
    //Td换热站小时平均数据表
    SOURCE_HOUR_AVG("source_hour_avg");
    TdAggregateTableIndex(String index) {
        this.index = index;
    }

    private final String index;

    public String getIndex() {
        return index;
    }
}
