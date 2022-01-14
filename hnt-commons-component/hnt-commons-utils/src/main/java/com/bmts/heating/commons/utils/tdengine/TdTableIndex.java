package com.bmts.heating.commons.utils.tdengine;

public enum TdTableIndex {

    //Td换热站初始化
    STATION_MINUTE_INITIALIZE("station_minute"),
    //Td热源初始化
    SOURCE_MINUTE_INITIALIZE("source_minute"),
    //Td换热站历史数据
    STATION_HISTORY("station"),
    //Td热源历史数据
    SOURCE_HISTORY("source"),
    //Td换热站能耗汇聚数据
    STATION_ENERGY("station_energy"),
    //Td热源能耗汇聚数据
    SOURCE_ENERGY("source_energy");


    TdTableIndex(String index) {
        this.index = index;
    }

    private final String index;

    public String getIndex() {
        return index;
    }
}
