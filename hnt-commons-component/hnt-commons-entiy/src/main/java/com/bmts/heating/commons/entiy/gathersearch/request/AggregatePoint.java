package com.bmts.heating.commons.entiy.gathersearch.request;

import lombok.Data;

@Data
public class AggregatePoint {
    private String pointName;
    private AggregateType aggregateType;
}
