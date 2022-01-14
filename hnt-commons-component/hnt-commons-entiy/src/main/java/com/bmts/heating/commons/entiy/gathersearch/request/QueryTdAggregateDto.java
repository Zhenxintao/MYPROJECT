package com.bmts.heating.commons.entiy.gathersearch.request;

import lombok.Data;

@Data
public class QueryTdAggregateDto {
    private QueryAggregateTdDto queryAggregateTdDto;
    private Integer level;
}
