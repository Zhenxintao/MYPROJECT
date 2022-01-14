package com.bmts.heating.bussiness.search.boot.controller;

import com.bmts.heating.bussiness.search.boot.route.IDataRouteCoreService;
import com.bmts.heating.commons.entiy.gathersearch.request.DataRouteParam;
import com.bmts.heating.commons.entiy.gathersearch.request.HistoryDocument;
import com.bmts.heating.commons.entiy.gathersearch.request.HistorySourceType;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsDto;
import com.bmts.heating.middleground.history.service.HistoryDataImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@Api(tags = "获取历史-实时数据")
@Slf4j
@RestController
@RequestMapping("/route")
public class DateRouteCoreController {

    @Autowired
    private IDataRouteCoreService dataRouteCoreService;

    @Autowired
    private HistoryDataImpl historyData;

    @PostMapping("/data")
    @ApiModelProperty
    public Object dataRoute(@RequestBody DataRouteParam param) {
        log.info("Data Route param={}", param);
        return dataRouteCoreService.dataRoute(param).getData();
    }

    @GetMapping
    public Collection<Map<String, String>> queryHistory() {
        QueryEsDto dto = new QueryEsDto();
        Integer[] systemIds = new Integer[]{272, 239};
        dto.setHeatSystemId(systemIds);
        dto.setStart(0L);
        dto.setEnd(5000000000000L);
        dto.setDocument(HistoryDocument.REAL_DATA);
        dto.setSourceType(HistorySourceType.FIRST);
        dto.setIncludeFields(new String[]{"timeStrap"});
        dto.setField("timeStrap");
        dto.setSortType(true);
        return historyData.findHistoryData(dto);
    }

}
