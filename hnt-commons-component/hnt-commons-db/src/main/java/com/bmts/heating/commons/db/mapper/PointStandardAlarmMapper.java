package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.PointStandardAlarm;
import com.bmts.heating.commons.basement.model.db.response.PointStandardResponse;
import com.bmts.heating.commons.entiy.baseInfo.response.PointStandardAlarmResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zxt
 * @since 2021-06-29
 */
public interface PointStandardAlarmMapper extends BaseMapper<PointStandardAlarm> {
    @Select("SELECT p.*,(CASE ps.netFlag WHEN 1 THEN '一次侧' WHEN 2 THEN '二次侧' ELSE '公用' END) netFlag,ps.name pointStandardName,ps.dataType,ps.columnName,ps.pointUnitId,pu.unitValue FROM pointStandardAlarm p  LEFT JOIN pointStandard ps ON p.pointStandardId = ps.id LEFT JOIN pointUnit pu ON ps.pointUnitId = pu.id ${ew.customSqlSegment}")
    Page<PointStandardAlarmResponse> page(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);


    @Select("SELECT ps.*  FROM pointStandard ps  \n" +
            " LEFT JOIN pointStandardAlarm psa ON psa.pointStandardId = ps.id  " +
            " ${ew.customSqlSegment}")
    Page<PointStandardResponse> pagePointStandard(Page page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);


    @Select("SELECT  DISTINCT   pc.relevanceId  \n" +
            " FROM  pointStandardAlarm psa  \n" +
            " JOIN pointConfig pc ON psa.pointStandardId = pc.pointStandardId  " +
            " ${ew.customSqlSegment}")
    List<Integer> listRelevanceId(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);

}
