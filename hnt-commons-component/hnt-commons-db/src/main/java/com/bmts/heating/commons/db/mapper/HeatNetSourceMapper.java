package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.db.entity.HeatNetSource;
import com.bmts.heating.commons.entiy.baseInfo.response.HeatNetSourceResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface HeatNetSourceMapper extends BaseMapper<HeatNetSource> {
    @Select("SELECT h.id,h.heatNetId,h.heatSourceId,h.matchupType,n.`name` as heatNetName,s.`name` as heatSourceName FROM  heatNetSource h LEFT JOIN heatNet n on h.heatNetId = n.id LEFT JOIN heatSource s on h.heatSourceId = s.id ${ew.customSqlSegment}")
    List<HeatNetSourceResponse> queryHeatNetSourceInfo(@Param(Constants.WRAPPER) Wrapper wrapper);
}
