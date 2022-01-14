package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.db.entity.PointUnit;
import com.bmts.heating.commons.basement.model.db.response.PointStandardResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2020-11-27
 */
public interface PointUnitMapper extends BaseMapper<PointUnit> {

    @Select("SELECT ps.*,pu.unitName AS unit,pu.unitValue AS unitDisable FROM pointUnit pu  " +
            " JOIN pointStandard ps ON pu.id = ps.pointUnitId AND ps.deleteFlag =FALSE  " +
            "${ew.customSqlSegment}")
    List<PointStandardResponse> listPoint(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);
}
