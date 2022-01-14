package com.bmts.heating.commons.entiy.baseInfo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("操作关联设备参数")
@Data
public class HeatDeviceConfigListDto {


    @ApiModelProperty("关联的设备id")
    private Integer deviceConfigId;

    @ApiModelProperty("类型：1、所属换热站    2、所属热源")
    private Integer type;

    /**
     * 关联id ： 所属换热站id 或热源id
     */
    @ApiModelProperty("关联id ： 所属换热站id 或热源id")
    private List<Integer> relevanceId;

}
