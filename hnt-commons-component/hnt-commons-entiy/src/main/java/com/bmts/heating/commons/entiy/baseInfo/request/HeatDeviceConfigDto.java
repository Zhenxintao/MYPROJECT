package com.bmts.heating.commons.entiy.baseInfo.request;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("关联设备参数")
@Data
public class HeatDeviceConfigDto extends BaseDto {

    @ApiModelProperty("关联的设备id")
    private Integer deviceConfigId;

    @ApiModelProperty("类型：1、所属换热站    2、所属热源")
    private Integer type;


}
