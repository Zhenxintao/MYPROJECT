package com.bmts.heating.commons.entiy.baseInfo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
@ApiModel("页面选择点位配置实体类")
public class PointPageConfig {
    @ApiModelProperty("大类单位外键")
    private Integer pointUnitId;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("配置标准点信息")
    List<PointUnitDetailDto> pointUnitDetailDtoList;
}
