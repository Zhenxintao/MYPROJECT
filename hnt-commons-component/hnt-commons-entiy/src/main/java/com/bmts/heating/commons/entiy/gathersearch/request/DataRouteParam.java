package com.bmts.heating.commons.entiy.gathersearch.request;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DataRouteParam  {

    /**
     * 实时数据查询条件Map
     */
    private Map<Integer,String[]> map;

    /**
     * 历史数据查询条件集合
     */
    private List<Integer> pointLs;

    /**
     * 区间开始时间
     */
    private Long startTime;

    /**
     * 区间结束时间
     */
    private Long endTime;

    /**
     * 起始页数
     */
    private int start = 1;

    /**
     * 每页记录数
     */
    private int limit = 100;

}
