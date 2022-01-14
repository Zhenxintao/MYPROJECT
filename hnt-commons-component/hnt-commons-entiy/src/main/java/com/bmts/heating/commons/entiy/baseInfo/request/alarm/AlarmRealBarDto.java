package com.bmts.heating.commons.entiy.baseInfo.request.alarm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AlarmRealBarDto {
    @ApiModelProperty("报警分类:0为全部分类信息,1.数据报警 2.设备故障")
    private Integer alarmType;
    @ApiModelProperty("组织机构Id:0为全部")
    private Integer heatorganId;
    @ApiModelProperty("热源Id:0为全部")
    private Integer heatSouceId;
    @ApiModelProperty("需要展示的换热站个数:0为全部")
    private Integer barChartCount;
    @ApiModelProperty("排序类型:ASC正序,DESC倒序")
    private String sortType;
}
