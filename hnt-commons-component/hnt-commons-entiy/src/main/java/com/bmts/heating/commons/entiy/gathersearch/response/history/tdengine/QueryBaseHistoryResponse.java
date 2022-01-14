package com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.JsonArray;
import lombok.Data;

import java.util.Map;

@Data
public class QueryBaseHistoryResponse {
    private Integer total;
    private JSONArray jsonData;
    private Map managedChannel;
}
