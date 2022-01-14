package com.bmts.heating.commons.entiy.baseInfo.cache;

import lombok.Data;

import java.util.Map;

@Data
public class PointRank {
    /**
     *  key systemId value 实时值
     */
    Map<Integer,Double> map;
    String columnName;
    Long total;
}
