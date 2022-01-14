package com.bmts.heating.web.energy.service.energyByTd.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bmts.heating.commons.basement.model.db.entity.SourceFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.entiy.gathersearch.request.AggregatePoint;
import com.bmts.heating.commons.entiy.gathersearch.request.AggregateType;
import com.bmts.heating.commons.entiy.gathersearch.request.tdengine.HeatTypeEnum;
import com.bmts.heating.commons.entiy.gathersearch.request.tdengine.QueryAggregateHistoryDto;
import com.bmts.heating.commons.entiy.gathersearch.request.tdengine.QueryBaseHistoryDto;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.GetEnergyPointConfig;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.HistoryEnergyDataResponse;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.energy.service.CommonService;
import com.bmts.heating.web.energy.service.energyByTd.EnergySystemReportFormsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zxt
 */
@Service
@Slf4j
public class EnergySystemReportFormsServiceImpl implements EnergySystemReportFormsService {
    @Autowired
    private CommonService commonService;
    @Autowired
    TSCCRestTemplate tsccRestTemplate;
    private final String gatherSearch = "gather_search";

    @Override
    public Response page(QueryBaseHistoryDto dto) {
        if (dto.getHeatType().equals(HeatTypeEnum.station)) {
            /*热力站*/
            return this.pageStation(commonService.metaStation(dto.getRelevanceIds()), dto);
        } else if (dto.getHeatType() == HeatTypeEnum.source) {
            return this.pageSource(commonService.metaSource(dto.getRelevanceIds()), dto);
        } else
            return Response.fail("参数错误");
    }

    @Override
    public Response converge(QueryAggregateHistoryDto dto) {
        if (dto.getHeatType().equals(HeatTypeEnum.station)) {
            /*热力站*/

            if (CollectionUtils.isEmpty(dto.getPoints())) {
                GetEnergyPointConfig energyPointConfig = commonService.getEnergyPointConfig();
                List<AggregatePoint> list = new ArrayList<>();
                buildPoint(energyPointConfig.getStationElectricityPoint(), list);
                buildPoint(energyPointConfig.getStationHeatPoint(), list);
                buildPoint(energyPointConfig.getStationWaterPoint(), list);
                dto.setPoints(list);
            }
            return this.convergeStation(commonService.metaStation(dto.getRelevanceIds()), dto);
        } else if (dto.getHeatType() == HeatTypeEnum.source) {
            if (CollectionUtils.isEmpty(dto.getPoints())) {
                GetEnergyPointConfig energyPointConfig = commonService.getEnergyPointConfig();
                List<AggregatePoint> list = new ArrayList<>();
                buildPoint(energyPointConfig.getSourceElectricityPoint(), list);
                buildPoint(energyPointConfig.getSourceHeatPoint(), list);
                buildPoint(energyPointConfig.getSourceWaterPoint(), list);
                dto.setPoints(list);

            }
            return this.convergeSource(commonService.metaSource(dto.getRelevanceIds()), dto);
        } else
            return Response.fail("参数错误");
    }

    /**
     * 设置汇聚点
     *
     * @param pointName
     * @param list
     */
    private void buildPoint(String pointName, List<AggregatePoint> list) {
        AggregatePoint aggregatePoint = new AggregatePoint();
        aggregatePoint.setPointName(pointName);
        aggregatePoint.setAggregateType(AggregateType.sum);
        list.add(aggregatePoint);
    }

    private Response pageStation(List<StationFirstNetBaseView> firstNetBases, QueryBaseHistoryDto dto) {
        List<Integer> ids = firstNetBases.stream().map(x -> x.getHeatTransferStationId()).distinct().collect(Collectors.toList());
        dto.setRelevanceIds(ids);
        HistoryEnergyDataResponse historyEnergyDataResponse = tsccRestTemplate.doHttp("/tdEngineHistory/queryHistoryEnergy", dto,gatherSearch , HistoryEnergyDataResponse.class, HttpMethod.POST);
        if (CollectionUtils.isEmpty(historyEnergyDataResponse.getResponseData())) {
            return Response.success();
        }
        historyEnergyDataResponse.getResponseData().forEach(x -> {
            StationFirstNetBaseView stationFirstNetBaseView = firstNetBases.stream().filter(item -> item.getHeatTransferStationId().equals(x.getRelevanceId())).findFirst().orElse(null);
            if (stationFirstNetBaseView != null)
                x.setName(stationFirstNetBaseView.getHeatTransferStationName());
        });
        return Response.success(historyEnergyDataResponse);
    }

    private Response pageSource(List<SourceFirstNetBaseView> firstNetBases, QueryBaseHistoryDto dto) {
        List<Integer> ids = firstNetBases.stream().map(x -> x.getHeatSystemId()).distinct().collect(Collectors.toList());
        dto.setRelevanceIds(ids);
        HistoryEnergyDataResponse historyEnergyDataResponse = tsccRestTemplate.doHttp("/tdEngineHistory/queryHistoryEnergy", dto, gatherSearch, HistoryEnergyDataResponse.class, HttpMethod.POST);
        if (CollectionUtils.isEmpty(historyEnergyDataResponse.getResponseData())) {
            return Response.success();
        }
        historyEnergyDataResponse.getResponseData().forEach(x -> {
            SourceFirstNetBaseView stationFirstNetBaseView = firstNetBases.stream().filter(item -> item.getHeatSystemId().equals(x.getRelevanceId())).findFirst().orElse(null);
            if (stationFirstNetBaseView != null)
                x.setName(stationFirstNetBaseView.getHeatSourceName());
        });
        return Response.success(historyEnergyDataResponse);
    }


    private Response convergeStation(List<StationFirstNetBaseView> firstNetBases, QueryAggregateHistoryDto dto) {
        List<Integer> ids = firstNetBases.stream().map(x -> x.getHeatTransferStationId()).distinct().collect(Collectors.toList());

        dto.setRelevanceIds(ids);

        HistoryEnergyDataResponse historyEnergyDataResponse = tsccRestTemplate
                .doHttp("/tdEngineHistory/queryHistoryAggregateEnergy", dto, gatherSearch, HistoryEnergyDataResponse.class, HttpMethod.POST);
        if (CollectionUtils.isEmpty(historyEnergyDataResponse.getResponseData())) {
            return Response.success();
        }
        historyEnergyDataResponse.getResponseData().forEach(x -> {
            StationFirstNetBaseView stationFirstNetBaseView = firstNetBases.stream().filter(item -> item.getHeatTransferStationId().equals(x.getRelevanceId())).findFirst().orElse(null);
            if (stationFirstNetBaseView != null)
                x.setName(stationFirstNetBaseView.getHeatTransferStationName());
        });
        return Response.success(historyEnergyDataResponse);
    }

    private Response convergeSource(List<SourceFirstNetBaseView> firstNetBases, QueryAggregateHistoryDto dto) {
        List<Integer> ids = firstNetBases.stream().map(x -> x.getHeatSystemId()).distinct().collect(Collectors.toList());
        dto.setRelevanceIds(ids);
        HistoryEnergyDataResponse historyEnergyDataResponse = tsccRestTemplate.
                doHttp("/tdEngineHistory/queryHistoryAggregateEnergy", dto, gatherSearch, HistoryEnergyDataResponse.class, HttpMethod.POST);
        if (CollectionUtils.isEmpty(historyEnergyDataResponse.getResponseData())) {
            return Response.success();
        }
        historyEnergyDataResponse.getResponseData().forEach(x -> {
            SourceFirstNetBaseView stationFirstNetBaseView = firstNetBases.stream().filter(item -> item.getHeatSystemId().equals(x.getRelevanceId())).findFirst().orElse(null);
            if (stationFirstNetBaseView != null)
                x.setName(stationFirstNetBaseView.getHeatSourceName());
        });
        return Response.success(historyEnergyDataResponse);
    }

}
