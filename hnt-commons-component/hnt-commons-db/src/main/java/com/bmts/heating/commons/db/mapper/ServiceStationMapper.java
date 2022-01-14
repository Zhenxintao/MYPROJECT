package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.db.entity.ServiceStation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ServiceStationMapper extends BaseMapper<ServiceStation> {
    @Select("select h.* from serviceStation s left join heatTransferStation h on s.heatStationId=h.code ${ew.customSqlSegment}")
    List<ServiceStation> queryServiceStation(@Param(Constants.WRAPPER) Wrapper wrapper);
}
