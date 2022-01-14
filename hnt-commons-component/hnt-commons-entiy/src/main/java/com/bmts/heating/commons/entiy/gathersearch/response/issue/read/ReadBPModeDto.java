package com.bmts.heating.commons.entiy.gathersearch.response.issue.read;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("二次补水变频泵读取实体类")
public class ReadBPModeDto {


    @ApiModelProperty("运行状态： 0:停止 1:运行 ")
    private Integer runStatus;

    @ApiModelProperty("故障状态： 0:正常 1:故障 ")
    private Integer faultStatus;

    @ApiModelProperty("远程状态：0:本地 1:远程")
    private Integer longStatus;

    @ApiModelProperty("实际频率")
    private Double bpHz;

    @ApiModelProperty("设值频率")
    private Double bpSV;

    @ApiModelProperty("实际电流")
    private Double bpA;


    @ApiModelProperty("0:定值  1:恒定二次回压  2:高低限自动（电节点压力表方式）")
    private Integer options;

    @ApiModelProperty("频率设定值")
    private Double bpMsP;

    @ApiModelProperty("二次回压")
    private Double p2h;

    @ApiModelProperty("二次回压设定值")
    private Double bpPhSP;

    @ApiModelProperty("高限设定值")
    private Double bpP2hH;

    @ApiModelProperty("低限设定值")
    private Double bpP2hL;

    @ApiModelProperty("补水启动令")
    private Integer start;

    @ApiModelProperty("补水停止令")
    private Integer stop;


}
