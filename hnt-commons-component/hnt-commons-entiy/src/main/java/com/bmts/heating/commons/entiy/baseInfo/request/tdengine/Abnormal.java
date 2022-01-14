package com.bmts.heating.commons.entiy.baseInfo.request.tdengine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class Abnormal {

    @ApiModelProperty("时间戳")
    private long ts;
    @ApiModelProperty("网、源、站、控制柜(0号机组) 或系统Id")
    private Integer groupId;
    @ApiModelProperty("所属： 1.站 2.源 对应枚举 AbnormalLevel")
    private Integer level;
    @ApiModelProperty("异常类型：3、设备损坏   4、数据异常  对应枚举 HealthStatus")
    private Integer type;


    @ApiModelProperty("点位标签名称")
    private String point;
    @ApiModelProperty("值")
    private String value;
    @ApiModelProperty("异常描述")
    private String msg;

    @ApiModelProperty("站名")
    private String stationName;

}
