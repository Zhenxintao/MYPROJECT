package com.bmts.heating.commons.entiy.baseInfo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("实时库查询实体类")
public class QueryCacheDto {
    @ApiModelProperty("系统集合")
    List<Integer> systemIds;
    @ApiModelProperty("点位集合")
    private String[] pointStandName;
}
