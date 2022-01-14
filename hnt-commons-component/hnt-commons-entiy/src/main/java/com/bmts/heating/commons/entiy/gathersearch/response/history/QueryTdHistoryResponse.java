package com.bmts.heating.commons.entiy.gathersearch.response.history;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class QueryTdHistoryResponse {
    private Integer total;
    private List<Map<String,Object>> mapList;
    private String error;
}
