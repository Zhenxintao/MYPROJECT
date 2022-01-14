package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.PointAlarm;
import com.bmts.heating.commons.entiy.baseInfo.response.PointAlarmResponse;
import com.bmts.heating.commons.entiy.baseInfo.response.PointAlarmSetResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zxt
 * @since 2021-06-29
 */
public interface PointAlarmMapper extends BaseMapper<PointAlarm> {
    @Select("SELECT p.*,(CASE ps.netFlag WHEN 1 THEN '一次侧' WHEN 2 THEN '二次侧' ELSE '公用' END) netFlag,ps.name pointStandardName,ps.dataType,ps.columnName,ps.pointUnitId,pu.unitValue FROM pointAlarm p LEFT JOIN pointConfig pc on p.pointConfigId = pc.id LEFT JOIN pointStandard ps ON pc.pointStandardId = ps.id LEFT JOIN pointUnit pu ON ps.pointUnitId = pu.id ${ew.customSqlSegment}")
    Page<PointAlarmResponse> page(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);


    @Select("SELECT pc.id pointConfigId,ps.* FROM pointConfig pc \n" +
            " LEFT JOIN pointStandard ps ON pc.pointStandardId = ps.id  \n" +
            " LEFT JOIN pointAlarm pa ON pa.pointConfigId = pc.id " +
            "${ew.customSqlSegment}")
    Page<PointAlarmSetResponse> pagePoint(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);


}
