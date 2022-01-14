package com.bmts.heating.commons.entiy.gathersearch.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 查询换热站或热源基础、能耗历史数据信息实体类
 * */
@Data
public class QueryHistoryDataDto {
    /**
     * 开始时间
     * */
    @ApiModelProperty("开始时间")
    private long startTime;
    /**
     * 结束时间
     * */
    @ApiModelProperty("结束时间")
    private long endTime;
    /**
     * 数据类型，1.换热站，2热源
     * */
    @ApiModelProperty("数据类型，1.换热站，2热源")
    private Integer level;
    @ApiModelProperty("时间类型：1.分钟,2小时整点,3小时平均,4天平均")
    private Integer dateType;
    @ApiModelProperty("当前页")
    private Integer currentPage=0;//当前页
    @ApiModelProperty("页面条数")
    private Integer size=20;//页面条数
    @ApiModelProperty("点位信息集合")
    private List<String> points;
    @ApiModelProperty("系统id集合")
    private List<Integer> groupId;
    @ApiModelProperty("排序类型")
    private Boolean sortType;
}
