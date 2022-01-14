package com.bmts.heating.commons.entiy.gathersearch.request.tdengine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 基础历史数据参数实体
 * */
@Data
public class QueryBaseHistoryDto {
    @ApiModelProperty("开始时间")
    private long startTime;
    @ApiModelProperty("结束时间")
    private long endTime;
    @ApiModelProperty("供热类型(station、source)")
    private HeatTypeEnum heatType;
    @ApiModelProperty("点位集合")
    private List<String> points;
    @ApiModelProperty("查询id集合（系统id或站id）")
    private List<Integer> relevanceIds;
    @ApiModelProperty("历史类型")
    private HistoryTypeEnum historyType;
    @ApiModelProperty("当前页(分页当前页码（如不需分页不传该参数即可）)")
    private Integer currentPage;
    @ApiModelProperty("每页条数(每页条数（如不需分页不传该参数即可）)")
    private Integer size;
    @ApiModelProperty("排序类型(true为正序  false为倒序 （默认为正序）)")
    private Boolean sortType;
}
