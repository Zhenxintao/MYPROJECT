package com.bmts.heating.commons.entiy.baseInfo.request.pointConfig;

import com.bmts.heating.commons.entiy.baseInfo.pojo.PointConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;

@ApiModel("点添加pojo")
@Data
public class PointConfigAddDto {

    @ApiModelProperty("关联id")
    private Collection<Integer> relevanceId;

    @ApiModelProperty("配置点")
    private PointConfig pointConfig;

    @ApiModelProperty("层级")
    private Integer level;


}
