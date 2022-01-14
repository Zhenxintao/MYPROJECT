package com.bmts.heating.commons.entiy.baseInfo.request.tdengine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PointInfo {
    @ApiModelProperty("点位标签名称")
    private String pointName;
    @ApiModelProperty("值")
    private String value;
}
