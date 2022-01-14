package com.bmts.heating.commons.entiy.baseInfo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@ApiModel("热力站实时报警响应类")
@Data
public class AlarmRealResponse {

    @ApiModelProperty("id")
    private Integer Id;

    @ApiModelProperty("报警级别")
    private Integer grade;

    @ApiModelProperty("报警点名称")
    private String pointName;

    @ApiModelProperty("报警点标签名称")
    private String columnName;

    @ApiModelProperty("报警时间")
    private LocalDateTime alarmTime;

    @ApiModelProperty("确认时间")
    private LocalDateTime confirmTime;

    @ApiModelProperty("报警描述")
    private String alarmDesc;

    @ApiModelProperty("确认人")
    private String confirmUser;

    @ApiModelProperty("系统id")
    private Integer heatSystemId;

    @ApiModelProperty("具体地址")
    private String address;

    @ApiModelProperty("换热站Id 或热源id")
    private Integer relevanceId;

    @ApiModelProperty("换热站名称或热源名称")
    private String relevanceName;

    @ApiModelProperty("类型  1.热力站 2.热源")
    private Integer type;

    @ApiModelProperty("报警值")
    private BigDecimal alarmValue;

    @ApiModelProperty("状态：1 报警 ，2 确认 ，3 恢复")
    private Integer status;

    @ApiModelProperty("系统名称")
    private String systemName;

}
