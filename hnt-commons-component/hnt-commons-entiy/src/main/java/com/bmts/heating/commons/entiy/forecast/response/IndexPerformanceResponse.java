package com.bmts.heating.commons.entiy.forecast.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("预测首页调度信息响应类")
public class IndexPerformanceResponse {
    //调取情况 单个热源为阶段设置展示
    @ApiModelProperty("调取情况 单个热源为阶段设置展示")
    private List<ForecastHeatLoad> forecastHeatLoadList;
    //今日实际供热量
    @ApiModelProperty("今日实际供热量")
    private BigDecimal dayRealHeat;
    //今日预计供热量
    @ApiModelProperty("今日预计供热量")
    private BigDecimal dayForecastHeat;
    //供暖季实际供热量
    @ApiModelProperty("供暖季实际供热量")
    private BigDecimal seasonRealHeat;
    //供暖季预测供热量
    @ApiModelProperty("供暖季预测供热量")
    private BigDecimal seasonForecastHeat;

    @Data
    public static class ForecastHeatLoad {
        //预测阶段开始时间
        @ApiModelProperty("预测阶段开始时间")
        private LocalDateTime startTime;
        //预测阶段结束时间
        @ApiModelProperty("预测阶段结束时间")
        private LocalDateTime endTime;
        //调度供热量
        @ApiModelProperty("调度供热量")
        private BigDecimal predictHeatRate;
        //调度流量
        @ApiModelProperty("调度流量")
        private BigDecimal firstNetCycleFlow;
        //调度供水温度
        @ApiModelProperty("调度供水温度")
        private BigDecimal firstNetTg;
        //调度回水温度
        @ApiModelProperty("调度回水温度")
        private BigDecimal firstNetTh;
    }


}
