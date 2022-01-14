package com.bmts.heating.commons.entiy.baseInfo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("单站实时展示界面实体类")
public class RedisCacheDto {
    @ApiModelProperty("id")
    private int id;
    @ApiModelProperty("点位集合")
    private String[] pointStandName;
}
