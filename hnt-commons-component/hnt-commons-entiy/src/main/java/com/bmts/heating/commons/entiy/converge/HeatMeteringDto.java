package com.bmts.heating.commons.entiy.converge;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("热计量实时数据实体")
public class HeatMeteringDto {

    @ApiModelProperty("房产id 与客服系统保持一致")
    private String id;
    @ApiModelProperty("换热站id")
    private Integer stationId;
    @ApiModelProperty("换热站名称")
    private String stationName;

    @JsonProperty("fFlowRate")
    @ApiModelProperty("瞬时流量")
    private BigDecimal fFlowRate;

    @ApiModelProperty("累积流量")
    @JsonProperty("fCumFlux")
    private BigDecimal fCumFlux;

    @ApiModelProperty("瞬时热量")
    @JsonProperty("fHeatRate")
    private BigDecimal fHeatRate;

    @ApiModelProperty("累积热量")
    @JsonProperty("fCumHeat")
    private BigDecimal fCumHeat;

    @ApiModelProperty("供水温度")
    @JsonProperty("fEnterTemp")
    private BigDecimal fEnterTemp;

    @ApiModelProperty("回水温度")
    @JsonProperty("fReturnTemp")
    private BigDecimal fReturnTemp;

    @ApiModelProperty("状态码")
    @JsonProperty("nMeterError")
    private Integer nMeterError;

    @ApiModelProperty("正累积流量")
    @JsonProperty("fForwardCumFlux")
    private BigDecimal fForwardCumFlux;

    @ApiModelProperty("负累积流量")
    @JsonProperty("fReverseCumFlux")
    private BigDecimal fReverseCumFlux;

    @ApiModelProperty("正累积热量")
    @JsonProperty("fForwardCumHeat")
    private BigDecimal fForwardCumHeat;

    @ApiModelProperty("负累积热量")
    @JsonProperty("fReverseCumHeat")
    private BigDecimal fReverseCumHeat;

    @ApiModelProperty("累计运行时间")
    @JsonProperty("nRunTime")
    private String nRunTime;

    @ApiModelProperty("采集时间")
    @JsonProperty("dCollectedTime")
    private String dCollectedTime;


}
