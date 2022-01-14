package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.db.entity.WebPointSearchUnit;
import com.bmts.heating.commons.entiy.baseInfo.response.PointPageConfigResponse;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zxt
 * @since 2021-02-25
 */
public interface WebPointSearchUnitMapper extends BaseMapper<WebPointSearchUnit> {

    @Select("SELECT wp.id,wp.pointUnitId,wp.sort,pu.unitName AS pointUnitName,pu.unitValue AS pointUnitValue FROM web_point_search_unit wp LEFT JOIN pointUnit pu on wp.pointUnitId = pu.id ${ew.customSqlSegment}")
    List<PointPageConfigResponse> pointPageConfigResponseList(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);
}
