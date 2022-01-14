package com.bmts.heating.commons.entiy.baseInfo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PointUnitDetailDto {
    @ApiModelProperty("标准点外键")
    private Integer pointStandardId;
    @ApiModelProperty("排序")
    private Integer sort;
}
