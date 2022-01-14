package com.bmts.heating.web.scada.controller.history;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bmts.heating.commons.basement.model.db.entity.SourceFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.entiy.gathersearch.request.AggregatePoint;
import com.bmts.heating.commons.entiy.gathersearch.request.tdengine.HeatTypeEnum;
import com.bmts.heating.commons.entiy.gathersearch.request.tdengine.QueryAggregateHistoryDto;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.GetEnergyPointConfig;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.HistoryBaseDataResponse;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.HistoryEnergyDataResponse;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.common.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "历史汇聚查询")
@RestController
@RequestMapping("/history/converge")
public class ConvergeController {
    @Autowired
    CommonService commonService;
//
//    {
//        "aggregateDataType": "whole",
//            "aggregateTimeType": "hour",
//            "currentPage": 1,
//            "endTime": 1635221952000,
//            "heatType": "station",
//            "historyType": "history_minute",
//            "points": [
//        {
//            "aggregateType": "avg",
//                "pointName": "t1g"
//        }
//	],
//        "relevanceIds": [],
//        "size": 20,
//            "sortType": true,
//            "startTime": 1635214752000
//    }
    @ApiOperation(value = "历史汇聚查询")
    @PostMapping
    public Response list(@RequestBody QueryAggregateHistoryDto dto) {
        if (CollectionUtils.isEmpty(dto.getPoints())) {
            return Response.paramError();
        }
        if (dto.getHeatType().equals(HeatTypeEnum.station)) {

            List<StationFirstNetBaseView> firstNetBases = commonService.metaStation(dto.getRelevanceIds());
            List<Integer> ids = firstNetBases.stream().map(x -> x.getHeatSystemId()).distinct().collect(Collectors.toList());
            dto.setRelevanceIds(ids);
            HistoryBaseDataResponse historyBaseDataResponse = commonService.convergeHistory(dto);
            if (CollectionUtils.isEmpty(historyBaseDataResponse.getResponseData())) {
                return Response.success();
            }
            historyBaseDataResponse.getResponseData().forEach(x -> {
                StationFirstNetBaseView stationFirstNetBaseView = firstNetBases.stream().filter(item -> item.getHeatSystemId().equals(x.getRelevanceId())).findFirst().orElse(null);
                if (stationFirstNetBaseView != null) {
                    x.setName(stationFirstNetBaseView.getHeatTransferStationName());
                    x.setSystemName(stationFirstNetBaseView.getHeatSystemName());
                }
            });
            return Response.success(historyBaseDataResponse);


        } else if (dto.getHeatType().equals(HeatTypeEnum.source)) {
            List<SourceFirstNetBaseView> firstNetBases = commonService.metaSource(dto.getRelevanceIds());
            List<Integer> ids = firstNetBases.stream().map(x -> x.getHeatSystemId()).distinct().collect(Collectors.toList());
            dto.setRelevanceIds(ids);
            HistoryBaseDataResponse historyBaseDataResponse = commonService.convergeHistory(dto);
            if (CollectionUtils.isEmpty(historyBaseDataResponse.getResponseData())) {
                return Response.success();
            }
            historyBaseDataResponse.getResponseData().forEach(x -> {
                SourceFirstNetBaseView sourceFirstNetBaseView = firstNetBases.stream().filter(item -> item.getHeatSystemId().equals(x.getRelevanceId())).findFirst().orElse(null);
                if (sourceFirstNetBaseView != null) {
                    x.setName(sourceFirstNetBaseView.getHeatSourceName());
                }
            });
            return Response.success(historyBaseDataResponse);
        }
        return Response.success();
    }


}
