package com.bmts.heating.commons.entiy.gathersearch.response.cache;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
@ApiModel("点明细")
@Data
public class PointDetail {

    @ApiModelProperty("点Id")
    private int pointId;
    @ApiModelProperty("系统Id")
    private int systemId;
    @ApiModelProperty("实时值")
    private String value;
    @ApiModelProperty("实时列标识")
    private String columnName;

    @ApiModelProperty("事故低报警")
    private BigDecimal accidentLower;

    @ApiModelProperty("事故高报警")
    private BigDecimal accidentHigh;
    @ApiModelProperty("运行低报警")
    private BigDecimal runningLower;
    @ApiModelProperty("运行高报警")
    private BigDecimal runningHigh;
    @ApiModelProperty("量程低报警")
    private BigDecimal rangeLower;
    @ApiModelProperty("json描述信息")
    private String descriptionJson;
    @ApiModelProperty("质量戳")
    private int qualityStrap;
    @ApiModelProperty("采集时间")
    private Long timeStamp;

}
