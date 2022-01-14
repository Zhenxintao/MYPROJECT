package com.bmts.heating.commons.entiy.converge;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("告警汇报实体")
public class ReportAlarmDto {

    @ApiModelProperty("户号")
    private String id;

    @ApiModelProperty("户表或者NB表等类型信息")
    private String type;

    @ApiModelProperty("值信息")
    private String value;

    @ApiModelProperty("告警信息")
    private String description;

}
