package com.bmts.heating.compute.forecast.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author naming
 * @description 在室外某时刻室外温度下标准工况热负荷率η
 * @date 2021/3/25 10:34
 **/
@Data
public class ComputeLoadRateBo {

    /**
     * 室内设计温度
     */
    BigDecimal insideDesignTemp;
    /**
     * 采暖期逐时室外温度
     */
    BigDecimal outsideTemp;
    /**
     * 采暖期设计室外计算温度 t´w
     */
    BigDecimal outsideComputeTemp;
}
