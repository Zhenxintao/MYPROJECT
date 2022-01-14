package com.bmts.heating.commons.entiy.forecast.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("预测首页指标评价响应类")
public class IndexEvaluateResponse {
    //累计热量
    @ApiModelProperty("累计热量")
    private float realhot;
    //实际热指标
    @ApiModelProperty("实际热指标")
    private float realhotIndex;
    //预测热量
    @ApiModelProperty("预测热量")
    private float forecasthot;
    //预测热指标
    @ApiModelProperty("预测热指标")
    private float forecasthotIndex;
    //完成情况 超供/欠供 数值为+值或-值
    @ApiModelProperty("完成情况 超供/欠供 数值为+值或-值")
    private float status;
    //完成率
    @ApiModelProperty("完成率")
    private float evaluationValue;
    //评价指标信息  优、良、差
    @ApiModelProperty("评价指标信息  优、良、差")
    private String evaluationInfo;
}
