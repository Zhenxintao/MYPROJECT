package com.bmts.heating.web.energy.pojo.energyByTd;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 能耗首页--地图总耗信息
 */
@Data
public class HomeEnergyOverallResponse {
    //总热耗
    private BigDecimal heatOverall=new BigDecimal(0);
    //电总耗
    private BigDecimal electricityOverall=new BigDecimal(0);
    //水总耗
    private BigDecimal waterOverall=new BigDecimal(0);
}
