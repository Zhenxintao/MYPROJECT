package com.bmts.heating.commons.entiy.gathersearch.request;

import lombok.Data;

import java.util.List;

@Data
public class QueryAggregateTdDto {
    private long startTime;
    private long endTime;
    private List<AggregatePoint> points;
    private List<Integer> groupId;
    private String tableName;
    private OriginalTd originalTd;
    private Integer limit;
    private Integer offset;
    /**
     * 排序方式，ASC/DESC"
     * */
    private String  order;
    private AggregateGroupType groupType;
}
