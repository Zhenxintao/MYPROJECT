package com.bmts.heating.commons.entiy.baseInfo.request.monitor;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class QuerySystemDto {
    @ApiModelProperty("系统id")
    private Integer relevanceId;
    @ApiModelProperty("点位信息集合")
    private List<String> pointsName;
}
