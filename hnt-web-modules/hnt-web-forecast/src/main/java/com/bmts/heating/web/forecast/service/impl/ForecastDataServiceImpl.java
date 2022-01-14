package com.bmts.heating.web.forecast.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceCore;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceHistory;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.db.mapper.ForecastSourceHistoryMapper;
import com.bmts.heating.commons.db.service.ForecastSourceCoreService;
import com.bmts.heating.commons.entiy.forecast.SearchDataDto;
import com.bmts.heating.commons.entiy.forecast.response.ForecastDataHistoryResponse;
import com.bmts.heating.commons.entiy.forecast.response.ForecastDataResponse;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.forecast.service.ForecastDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForecastDataServiceImpl extends SavantServices implements ForecastDataService {

    @Autowired
    private ForecastSourceHistoryMapper forecastSourceHistoryMapper;

    @Autowired
    private ForecastSourceCoreService forecastSourceCoreService;

    @Override
    public Response searchForecastData(SearchDataDto dto) {
        try {
            QueryWrapper<ForecastSourceHistory> queryWrapper = new QueryWrapper<>();
            if (dto.getStartTime() != null && dto.getEndTime() != null) {
                queryWrapper.ge("h.startTime", dto.getStartTime());
                queryWrapper.le("h.startTime", dto.getEndTime());
                if (dto.getForecastType() != null) {
                    queryWrapper.eq("h.type", dto.getForecastType());
                }
                if (dto.getForecastSourceCoreId() != null && dto.getForecastSourceCoreId() != 0) {
                    queryWrapper.eq("h.forecastSourceCoreId", dto.getForecastSourceCoreId());
                }
                queryWrapper.orderByAsc("c.name");
                queryWrapper.orderByAsc("h.startTime");
                Page<ForecastDataResponse> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
                Page<ForecastDataResponse> forecastDataResponsePage = forecastSourceHistoryMapper.queryForecastDataResponse(page, queryWrapper);
                return Response.success(forecastDataResponsePage);
            }
            return Response.fail();
        } catch (Exception e) {
            return Response.fail();
        }

    }

    @Override
    public Response searchForecastHistoryData(SearchDataDto dto) {
        try {
            QueryWrapper<ForecastSourceHistory> queryWrapper = new QueryWrapper<>();
            if (dto.getStartTime() != null && dto.getEndTime() != null) {
                queryWrapper.ge("h.startTime", dto.getStartTime());
                queryWrapper.le("h.endTime", dto.getEndTime());
            }
            if (dto.getForecastType() != null) {
                queryWrapper.eq("h.type", dto.getForecastType());
            }
            if (dto.getForecastSourceCoreId() != null && dto.getForecastSourceCoreId() != 0) {
                queryWrapper.eq("h.forecastSourceCoreId", dto.getForecastSourceCoreId());
            }
            queryWrapper.orderByAsc("c.name");
            queryWrapper.orderByAsc("h.startTime");
            Page<ForecastDataHistoryResponse> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
            return Response.success(forecastSourceHistoryMapper.queryForecastHistoryDataResponse(page, queryWrapper));
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @Override
    public Response searchForecastSourceCoreList() {
        return Response.success(forecastSourceCoreService.list(Wrappers.<ForecastSourceCore>lambdaQuery().select(ForecastSourceCore::getId, ForecastSourceCore::getName)));
    }
}
