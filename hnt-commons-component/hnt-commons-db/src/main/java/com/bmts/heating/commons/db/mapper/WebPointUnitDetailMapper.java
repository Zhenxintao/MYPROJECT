package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.db.entity.WebPointUnitDetail;
import com.bmts.heating.commons.entiy.baseInfo.response.PointUnitDetailResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zxt
 * @since 2021-02-25
 */
public interface WebPointUnitDetailMapper extends BaseMapper<WebPointUnitDetail> {
    @Select("SELECT ps.name,ps.type,ps.dataType,ps.columnName,ps.netFlag,ps.fixValueType,ps.userId,ps.pointConfig,dc.name AS pointTypeName,ppt.name AS pointName,wp.webPointSearchUnitId,wp.pointStandardId, wp.sort FROM web_point_unit_detail wp LEFT JOIN pointStandard ps on wp.pointStandardId = ps.id" +
            "            LEFT JOIN dic dc ON ps.type=dc.id" +
            "            LEFT JOIN pointParameterType ppt ON ps.pointParameterTypeId=ppt.id ${ew.customSqlSegment}")
    List<PointUnitDetailResponse> pointUnitDetailResponseList(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);
}
