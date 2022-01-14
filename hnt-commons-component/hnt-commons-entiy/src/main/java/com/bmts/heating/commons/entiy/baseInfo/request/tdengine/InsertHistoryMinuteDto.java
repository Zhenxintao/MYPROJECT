package com.bmts.heating.commons.entiy.baseInfo.request.tdengine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class InsertHistoryMinuteDto {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("TD表名称")
    private String tableName;
    @ApiModelProperty("设备唯一编码")
    private String deviceCode;
    @ApiModelProperty("网、源、站、控制柜(0号机组) 或系统Id")
    private Integer groupId;
    @ApiModelProperty("默认值1属于系统 2.控制柜 3.站 4.源 5.网")
    private Integer level;
    @ApiModelProperty("时间戳")
    private long ts;
    @ApiModelProperty("点位信息")
    List<PointInfo> points;
}
