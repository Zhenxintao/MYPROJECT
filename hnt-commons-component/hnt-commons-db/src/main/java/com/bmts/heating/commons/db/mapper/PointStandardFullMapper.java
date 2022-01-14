package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.entity.PointStandardFull;
import com.bmts.heating.commons.basement.model.db.response.PointStandardResponse;
import com.bmts.heating.commons.entiy.baseInfo.response.PointStandardFullResponse;
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
public interface PointStandardFullMapper extends BaseMapper<PointStandardFull> {

    @Select("SELECT ps.id,ps.name,ps.type,ps.columnName,ps.netFlag,ps.pointConfig,pu.unitName,pu.unitValue FROM  pointStandardFull ps  LEFT JOIN  pointUnit pu on ps.pointUnitId = pu.id")
    List<PointStandardFullResponse> pointStandardFullList();

    @Select("SELECT ps.id,ps.name,ps.type,ps.columnName,ps.netFlag,ps.pointConfig,pu.unitName,pu.unitValue FROM  pointStandard ps  LEFT JOIN  pointUnit pu on ps.pointUnitId = pu.id")
    List<PointStandardFullResponse> pointStandardList();
}
