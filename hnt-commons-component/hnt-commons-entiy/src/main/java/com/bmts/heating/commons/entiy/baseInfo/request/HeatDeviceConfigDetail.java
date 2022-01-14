package com.bmts.heating.commons.entiy.baseInfo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("关联设备详细")
@Data
public class HeatDeviceConfigDetail {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("类型：1、所属换热站    2、所属热源")
    private Integer type;


}
