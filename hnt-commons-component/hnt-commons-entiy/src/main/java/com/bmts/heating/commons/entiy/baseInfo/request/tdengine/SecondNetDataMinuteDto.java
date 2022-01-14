package com.bmts.heating.commons.entiy.baseInfo.request.tdengine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SecondNetDataMinuteDto {
    @ApiModelProperty("超级表名称")
    private String stableName;
    @ApiModelProperty("子表名称")
    private String tableName;
    @ApiModelProperty("时间戳")
    private long ts;
    @ApiModelProperty("点位信息")
    List<PointInfo> points;
    @ApiModelProperty("设备标签信息")
    List<PointInfo> tags;
}
