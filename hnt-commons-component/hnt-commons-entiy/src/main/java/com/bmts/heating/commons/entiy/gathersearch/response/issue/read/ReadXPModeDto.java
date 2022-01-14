package com.bmts.heating.commons.entiy.gathersearch.response.issue.read;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("二次循环泵读取实体类")
public class ReadXPModeDto {

    @ApiModelProperty("运行状态： 0:停止 1:运行 ")
    private Integer runStatus;

    @ApiModelProperty("故障状态： 0:正常 1:故障 ")
    private Integer faultStatus;

    @ApiModelProperty("远程状态：0:本地 1:远程")
    private Integer longStatus;

    @ApiModelProperty("实际频率")
    private Double xpHz;

    @ApiModelProperty("给定频率")
    private Double xpSV;

    @ApiModelProperty("实际电流")
    private Double xpA;


    @ApiModelProperty("0:定值  1:恒定二次供回压差  2:恒定二次供压")
    private Integer options;

    @ApiModelProperty("频率设定值")
    private Double xpMSP;

    @ApiModelProperty("二次供回压差")
    private Double p2d;

    @ApiModelProperty("二次供回压差设定值")
    private Double xpPdSP;

    @ApiModelProperty("二次供压")
    private Double p2g;

    @ApiModelProperty("二次供压设定值")
    private Double xpPgSP;

    @ApiModelProperty("补水启动令")
    private Integer start;

    @ApiModelProperty("补水停止令")
    private Integer stop;

}
