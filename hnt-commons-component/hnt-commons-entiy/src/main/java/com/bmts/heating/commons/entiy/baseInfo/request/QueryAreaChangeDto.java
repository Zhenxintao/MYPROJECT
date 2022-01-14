package com.bmts.heating.commons.entiy.baseInfo.request;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QueryAreaChangeDto {
    @ApiModelProperty("层级 系统:1 控制柜:2 换热站:3 热源:4 热网:5 系统分支:6")
    private Integer level;
    @ApiModelProperty("关联Id集合")
    private Integer[] relevanceIds;
    @ApiModelProperty("区间时间开始")
    private LocalDateTime startTime;
    @ApiModelProperty("区间时间结束")
    private LocalDateTime endTime;
}
