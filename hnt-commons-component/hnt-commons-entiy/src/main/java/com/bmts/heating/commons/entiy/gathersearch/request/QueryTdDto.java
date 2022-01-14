package com.bmts.heating.commons.entiy.gathersearch.request;

import lombok.Data;

import java.util.List;

/**
 * 查询TDENGINE基础、能耗历史数据信息实体类
 * */
@Data
public class QueryTdDto {
    private long startTime;
    private long endTime;
    private Integer level;
    private Integer limit;
    private Integer offset;
    private List<String> points;
    private List<Integer> groupId;
    private String tableName;
    /**
     * 排序方式，ASC/DESC"
     * */
    private String  order;
    private Integer abnormalType;
    private OriginalTd originalTd;
    private QueryTypeTd queryTypeTd;

    private String stationName;
}
