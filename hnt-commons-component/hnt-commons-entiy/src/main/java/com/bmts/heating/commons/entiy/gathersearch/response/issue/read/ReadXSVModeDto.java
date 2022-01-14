package com.bmts.heating.commons.entiy.gathersearch.response.issue.read;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("泄压阀读取实体类")
public class ReadXSVModeDto {

    @ApiModelProperty("0:手动 1:自动")
    private Integer options;

    @ApiModelProperty("泄压电磁阀工作方式0手动启停控制(0:关闭 1开启)")
    private Integer handStatus;

    @ApiModelProperty("回水压力")
    private Double p2h;

    @ApiModelProperty("自动高限设定值")
    private Double xsvP2hH;

    @ApiModelProperty("自动低限设定值")
    private Double xsvP2hL;

    @ApiModelProperty("泄压阀状态 0:关闭 1:打开")
    private Integer status;


}
