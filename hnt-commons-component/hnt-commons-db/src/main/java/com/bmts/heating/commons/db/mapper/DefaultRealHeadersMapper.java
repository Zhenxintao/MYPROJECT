package com.bmts.heating.commons.db.mapper;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.db.entity.DefaultRealHeaders;
import com.bmts.heating.commons.entiy.baseInfo.response.DefaultRealHeadersDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author pxf
 * @since 2020-11-16
 */
public interface DefaultRealHeadersMapper extends BaseMapper<DefaultRealHeaders> {

    @Select("SELECT drh.pointStandardId, drh.sort, drh.type, ps.`name`, ps.`columnName`, ps.`netFlag`, ppt.`name` AS pointName, pu.unitName AS unit, pu.unitValue AS unitDisable  FROM default_real_headers drh " +
            "LEFT JOIN pointStandard ps ON ps.id = drh.pointStandardId  AND ps.deleteFlag = 0  " +
            "LEFT JOIN pointParameterType ppt ON ps.pointParameterTypeId = ppt.id  AND ppt.deleteFlag = 0  " +
            "LEFT JOIN pointUnit pu ON ps.pointUnitId = pu.id  AND pu.deleteFlag = 0 ${ew.customSqlSegment}")
    List<DefaultRealHeadersDto> listHeaders(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);
}
