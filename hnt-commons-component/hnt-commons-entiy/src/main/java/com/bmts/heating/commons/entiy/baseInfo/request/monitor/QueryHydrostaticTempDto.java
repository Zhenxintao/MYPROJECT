package com.bmts.heating.commons.entiy.baseInfo.request.monitor;

import lombok.Data;

import java.util.List;

@Data
public class QueryHydrostaticTempDto {
    private List<Integer> relevanceIds;
    private List<String> pointsName;
}
