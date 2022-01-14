package com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine;

import lombok.Data;

/**
 * web_page_config 配置各项目水电热能耗点位信息
 * */
@Data
public class GetEnergyPointConfig {
    private String stationWaterPoint;
    private String stationElectricityPoint;
    private String stationHeatPoint;
    private String sourceWaterPoint;
    private String sourceElectricityPoint;
    private String sourceHeatPoint;
}
