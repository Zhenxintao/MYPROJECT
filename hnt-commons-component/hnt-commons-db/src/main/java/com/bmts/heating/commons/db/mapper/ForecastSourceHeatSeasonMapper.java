package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceHeatSeason;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author naming
 * @since 2021-03-17
 */
public interface ForecastSourceHeatSeasonMapper extends BaseMapper<ForecastSourceHeatSeason> {

    @Select("SELECT  fshs.*   FROM  forecast_source_heat_season fshs  " +
            " LEFT JOIN forecast_source_core fsc ON fsc.id = fshs.forcastSourceCoreId  " +
            " ${ew.customSqlSegment}")
    List<ForecastSourceHeatSeason> queryHeatSeason(@Param(Constants.WRAPPER) Wrapper queryWrapper);

}
