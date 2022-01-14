package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.response.PointStandardResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
public interface PointStandardMapper extends BaseMapper<PointStandard> {

    @Select("SELECT ps.*,dc.name AS pointTypeName,ppt.name AS pointName,pu.unitName AS unit ,pu.unitValue AS unitDisable FROM pointStandard ps " +
            "LEFT JOIN dic dc ON ps.type=dc.id " +
            "LEFT JOIN pointParameterType ppt ON ps.pointParameterTypeId=ppt.id " +
            "LEFT JOIN pointUnit pu ON ps.pointUnitId=pu.id " +
            "${ew.customSqlSegment}")
    Page<PointStandardResponse> queryPointStandard(Page page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);


    @Select("SELECT ps.*,dc.name AS pointTypeName,ppt.name AS pointName,pu.unitName AS unit,pu.unitValue AS unitDisable FROM pointStandard ps " +
            "LEFT JOIN dic dc ON ps.type=dc.id " +
            "LEFT JOIN pointParameterType ppt ON ps.pointParameterTypeId=ppt.id " +
            "LEFT JOIN pointUnit pu ON ps.pointUnitId=pu.id " +
            "${ew.customSqlSegment}")
    List<PointStandardResponse> listPointStandard(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);

}
