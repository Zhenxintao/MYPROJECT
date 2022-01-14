package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceHistory;
import com.bmts.heating.commons.entiy.forecast.response.ForecastDataHistoryResponse;
import com.bmts.heating.commons.entiy.forecast.response.ForecastDataResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author naming
 * @since 2021-03-22
 */
public interface ForecastSourceHistoryMapper extends BaseMapper<ForecastSourceHistory> {
    @Select("SELECT h.forecast_t1g as forecastTg,h.forecast_t1h as  forecastTh, h.real_t1g AS realT1g,h.real_t1h AS realT1h, h.*,c.`name` AS forecastSourceName,c.areaHeatingIndex FROM forecast_source_history h  JOIN forecast_source_core c on h.forecastSourceCoreId = c.id ${ew.customSqlSegment}")
    Page<ForecastDataResponse> queryForecastDataResponse(Page page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    @Select("SELECT h.forecast_t1g as forecastTg,h.forecast_t1h as  forecastTh,h.real_t1g AS realT1g,h.real_t1h AS realT1h, h.*,c.`name` AS forecastSourceName,c.areaHeatingIndex FROM forecast_source_history h  JOIN forecast_source_core c on h.forecastSourceCoreId = c.id ${ew.customSqlSegment}")
    Page<ForecastDataHistoryResponse> queryForecastHistoryDataResponse(Page page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    @Select("SELECT h.forecast_t1g as forecastTg,h.forecast_t1h as  forecastTh, h.real_t1g AS realT1g,h.real_t1h AS realT1h, h.*,c.`name` AS forecastSourceName,c.areaHeatingIndex FROM forecast_source_history h  JOIN forecast_source_core c on h.forecastSourceCoreId = c.id ${ew.customSqlSegment}")
    List<ForecastDataResponse> listForecastDataResponse(@Param(Constants.WRAPPER) Wrapper queryWrapper);

}
