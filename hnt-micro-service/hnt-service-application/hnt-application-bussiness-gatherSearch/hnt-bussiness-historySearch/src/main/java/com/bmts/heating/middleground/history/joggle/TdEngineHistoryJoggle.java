package com.bmts.heating.middleground.history.joggle;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.entiy.gathersearch.request.CheckTdPointDto;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryTdAggregateDto;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryTdDto;
import com.bmts.heating.commons.entiy.gathersearch.request.tdengine.QueryAggregateHistoryDto;
import com.bmts.heating.commons.entiy.gathersearch.request.tdengine.QueryBaseHistoryDto;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.HistoryBaseDataResponse;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.HistoryEnergyDataResponse;
import com.bmts.heating.middleground.history.service.TdEngineQueryHistoryData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "Td历史数据查询")
@Slf4j
@RestController
@RequestMapping("/tdEngineHistory")
public class TdEngineHistoryJoggle {

    @Autowired
    private TdEngineQueryHistoryData tdEngineQueryHistoryData;

    @ApiOperation("查询TdEngine历史基础数据(Old)")
    @PostMapping("/queryHistoryData")
    public Map<String, Object> queryHistoryData(@RequestBody QueryTdDto dto) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, String>> list = tdEngineQueryHistoryData.queryHistoryData(dto);
        resultMap.put("data", list);
        if (list.size() <= 0) {
            resultMap.put("total", 0);
        } else {
            resultMap.put("total", Integer.valueOf(list.get(list.size() - 1).get("total")));
            list.remove(list.size() - 1);
        }
        return resultMap;
    }

    @ApiOperation("查询TdEngine历史能耗聚合数据(Old)")
    @PostMapping("/queryHistoryEnergyData")
    public Collection<Map<String, String>> queryHistoryEnergyData(@RequestBody QueryTdAggregateDto dto) {
        Collection<Map<String, String>> list = tdEngineQueryHistoryData.queryHistoryEnergyData(dto.getQueryAggregateTdDto(), dto.getLevel());
        return list;
    }

    /**
     * Td基础历史数据查询
     */
    @ApiOperation("Td基础历史数据查询")
    @PostMapping("/queryHistoryBase")
    public HistoryBaseDataResponse queryHistoryBase(@RequestBody QueryBaseHistoryDto dto) {
        return tdEngineQueryHistoryData.queryHistoryBase(dto);
    }

    /**
     * Td基础历史能耗数据查询
     */
    @ApiOperation("Td基础历史能耗数据查询")
    @PostMapping("/queryHistoryEnergy")
    public HistoryEnergyDataResponse queryHistoryEnergy(@RequestBody QueryBaseHistoryDto dto) {
        return tdEngineQueryHistoryData.queryHistoryEnergy(dto);
    }

    /**
     * Td聚合历史数据查询
     */
    @ApiOperation("Td聚合历史数据查询")
    @PostMapping("/queryHistoryAggregate")
    public HistoryBaseDataResponse queryHistoryAggregate(@RequestBody QueryAggregateHistoryDto dto) {
        return tdEngineQueryHistoryData.queryHistoryAggregate(dto);
    }

    /**
     * Td聚合历史能耗数据查询
     */
    @ApiOperation("Td聚合历史数据查询")
    @PostMapping("/queryHistoryAggregateEnergy")
    public HistoryEnergyDataResponse queryHistoryAggregateEnergy(@RequestBody QueryAggregateHistoryDto dto) {
        return tdEngineQueryHistoryData.queryHistoryAggregateEnergy(dto);
    }

}
