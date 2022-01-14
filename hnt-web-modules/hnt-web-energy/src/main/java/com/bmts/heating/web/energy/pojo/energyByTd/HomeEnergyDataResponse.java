package com.bmts.heating.web.energy.pojo.energyByTd;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 能耗首页--昨日能耗数据
 * */
@Data
public class HomeEnergyDataResponse {
    //室外温度
    private BigDecimal temp=new BigDecimal(0);
    //水能耗
    private BigDecimal waterUnitConsumption = null;
    //电能耗
    private BigDecimal electricityUnitConsumption = null;
    //折标热能耗
    private BigDecimal heatUnitConsumption = null;
    //水耗
    private BigDecimal waterConsumption = new BigDecimal(0);
    //电耗
    private BigDecimal electricityConsumption = new BigDecimal(0);
    //折标热耗
    private BigDecimal heatConsumption = new BigDecimal(0);
    //室外温度环比
    private BigDecimal tempHb=new BigDecimal(0);
    //水能耗环比
    private BigDecimal waterHb=new BigDecimal(0);
    //电能耗环比
    private BigDecimal electricityHb=new BigDecimal(0);
    //折标热能耗环比
    private BigDecimal heatHb=new BigDecimal(0);
    //室外温度同比
    private BigDecimal tempTb=new BigDecimal(0);
    //水能耗同比
    private BigDecimal waterTb=new BigDecimal(0);
    //电能耗同比
    private BigDecimal electricityTb=new BigDecimal(0);
    //折标热能耗同比
    private BigDecimal heatTb=new BigDecimal(0);
}
