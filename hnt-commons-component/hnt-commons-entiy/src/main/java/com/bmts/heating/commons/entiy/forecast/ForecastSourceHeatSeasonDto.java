package com.bmts.heating.commons.entiy.forecast;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("负荷预测采暖季阶段信息")
public class ForecastSourceHeatSeasonDto {
    private Integer id;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 一次网阶段流量G（t/h）
     */
    private BigDecimal firstNetStageFlow;

    /**
     * 一次网相对流量 (计算得来)
     */
    private BigDecimal firstNetRelativeFlow;

    /**
     * 二次网相对流量
     */
    private BigDecimal secondRelativeFlow;

    private Integer forcastSourceCoreId;

    /**
     * 采暖季阶段名称
     */
    private String stageName;
}
