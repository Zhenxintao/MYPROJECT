package com.bmts.heating.commons.db.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.cache.PointUnitAndParamTypeResponse;
import com.bmts.heating.commons.basement.model.db.entity.PointConfig;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.response.PointConfigResponse;
import com.bmts.heating.commons.entiy.baseInfo.response.CabinetPointResponse;
import com.bmts.heating.commons.entiy.baseInfo.response.PointCollectConfigAlarm;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author naming
 * @since 2021-01-29
 */
public interface PointConfigMapper extends BaseMapper<PointConfig> {

//    @Select("SELECT pc.*,ps.name AS pointStandardName,pvss.nodeCode FROM pointConfig pc LEFT JOIN pointStandard ps ON pc.pointStandardId=ps.id\n" +
//            "LEFT JOIN deviceConfig pvss ON pvss.id=pc.deviceConfigId " +
//            "${ew.customSqlSegment} LIMIT #{start},#{end}")
//    List<PointCollectConfigResponse> queryPoints(@Param(Constants.WRAPPER) QueryWrapper queryWrapper, @Param("start") int start, @Param("end") int end);

    @Select("SELECT\n" +
            "            pc.*,ps.expression,ps.name AS pointStandardName,1 AS heatType,ps.pointConfig,hs.name As systemName,hs.number,ps.netFlag, ps.columnName,\n" +
            "                    dc.nodeCode,\n" +
            "                    pu.unitName,\n" +
            "                    pu.unitValue, ppt.name pointParameterTypeName , ps.dataType AS dataLengthType, \n" +
            "                    `hs`.`number` AS `systemNum`,\n" +
            "                    `hts`.`syncNumber` AS `parentSyncNum`, \n" +
            "                     ei.equipmentCode  " +
            "                    FROM pointConfig pc \n" +
            "                    LEFT JOIN pointStandard ps ON pc.pointStandardId=ps.id \n" +
            "                    LEFT JOIN pointParameterType ppt ON ps.pointParameterTypeId=ppt.id \n" +
            "                    LEFT JOIN pointUnit pu ON pu.id=ps.pointUnitId \n" +
            "                    LEFT JOIN heatSystem hs ON hs.id=pc.relevanceId\n" +
            "                    LEFT JOIN heatCabinet hc ON hc.id =hs.heatCabinetId\n" +
            "                    RIGHT JOIN heatTransferStation hts ON hts.id=hc.heatTransferStationId " +
            "                    LEFT JOIN heatDeviceConfig hdc ON hdc.relevanceId = hts.id \n" +
            "                    LEFT JOIN deviceConfig dc ON dc.id = hdc.deviceConfigId " +
            "                    LEFT JOIN equipmentInfoPointStandard eps ON eps.pointStandardId=ps.id \n" +
            "                    LEFT JOIN equipmentInfo ei ON ei.id = eps.equipmentInfoId  " +
            " ${ew.customSqlSegment} ")
    List<PointUnitAndParamTypeResponse> queryPointsBasic(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    @Select("SELECT\n" +
            "            pc.*,ps.expression,ps.name AS pointStandardName,2 AS heatType,ps.pointConfig,hs.name As systemName,hs.number,ps.netFlag, ps.columnName,\n" +
            "                    dc.nodeCode,\n" +
            "                    pu.unitName,\n" +
            "                    pu.unitValue, ppt.name pointParameterTypeName , ps.dataType AS dataLengthType,  \n" +
            "                    `hs`.`number` AS `systemNum`,\n" +
            "                    `heatSource`.`syncNumber` AS `parentSyncNum`, \n" +
            "                     ei.equipmentCode  " +
            "                    FROM pointConfig pc \n" +
            "                    LEFT JOIN pointStandard ps ON pc.pointStandardId=ps.id \n" +
            "                    LEFT JOIN pointParameterType ppt ON ps.pointParameterTypeId=ppt.id \n" +
            "                    LEFT JOIN pointUnit pu ON pu.id=ps.pointUnitId \n" +
            "                    LEFT JOIN heatSystem hs ON hs.id=pc.relevanceId\n" +
            "                    LEFT JOIN heatCabinet hc ON hc.id =hs.heatCabinetId\n" +
            "                    RIGHT JOIN heatSource heatSource ON heatSource.id=hc.heatSourceId" +
            "                    LEFT JOIN heatDeviceConfig hdc ON hdc.relevanceId = heatSource.id \n" +
            "                    LEFT JOIN deviceConfig dc ON dc.id = hdc.deviceConfigId " +
            "                    LEFT JOIN equipmentInfoPointStandard eps ON eps.pointStandardId=ps.id  \n" +
            "                    LEFT JOIN equipmentInfo ei ON ei.id = eps.equipmentInfoId  " +
            " ${ew.customSqlSegment} ")
    List<PointUnitAndParamTypeResponse> querySourcePointsBasic(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    @Select("SELECT ps.name pointStandardName,ps.columnName,pcc.* FROM pointConfig pcc LEFT JOIN alarmConfig ac ON pcc.alarmConfigId=ac.id\n" +
            "LEFT JOIN pointStandard ps ON ps.id= pcc.pointStandardId" +
            " ${ew.customSqlSegment} ")
    List<PointCollectConfigAlarm> queryConfigAndAlarm(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);


    @Select("SELECT pcc.id pointId,hs.id relevanceId,hc.id cabinetId FROM pointConfig pcc \n" +
            "LEFT JOIN heatSystem hs ON pcc.relevanceId=hs.id and pcc.level=1 \n" +
            "LEFT JOIN heatCabinet hc ON hs.heatCabinetId=hc.id ${ew.customSqlSegment}")
    List<CabinetPointResponse> loadPointsByCabinet(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    /*----------------------------------*/

    @Select("SELECT pc.*,(CASE ps.netFlag WHEN 1 THEN '一次侧' WHEN 2 THEN '二次侧' ELSE '公用' END) netFlag,ps.name pointStandardName,ps.dataType,ps.columnName,ps.pointUnitId,pu.unitValue,ppt.name pointParameterTypeName FROM pointConfig pc \n" +
            " LEFT JOIN pointStandard ps ON pc.pointStandardId = ps.id  \n" +
            " LEFT JOIN pointUnit pu ON ps.pointUnitId = pu.id  " +
            " left join pointParameterType ppt on ps.pointParameterTypeId = ppt.id " +
            "${ew.customSqlSegment}")
    Page<PointConfigResponse> page(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select pc.*,ps.netFlag,ps.name pointStandardName,ps.columnName,pu.id pointUnitId,pu.unitValue from pointConfig pc " +
            " left join pointStandard ps on pc.pointStandardId = ps.id " +
            " left join pointUnit pu on pu.id = ps.pointUnitId where pc.id = #{id}")
    PointConfigResponse emptyInfo(Integer id);


    @Select("SELECT * FROM pointStandard ps WHERE ps.id NOT IN " +
            "(SELECT pc.pointStandardId FROM pointConfig pc WHERE pc.level= #{level} pc.`relevanceId` = #{heatSystemId}) ")
    Page<PointStandard> loadOtherPoint(Page page, int level, Integer relevanceId);

    @Select("SELECT * FROM pointStandard ps WHERE ps.id NOT IN " +
            "(SELECT pc.pointStandardId FROM pointConfig pc WHERE level = #{level} and pc.`relevanceId` = #{relevanceId}) " +
            "AND (ps.name LIKE CONCAT('%',#{keyWord},'%') OR ps.columnName like CONCAT('%',#{keyWord},'%'))")
    Page<PointStandard> loadOtherPointAndSearch(Page page, int level, Integer relevanceId, String keWord);

    @Select("SELECT pc.*,ps.columnName as pointColumnName FROM pointConfig pc LEFT JOIN pointStandard ps ON  ps.id=pc.pointStandardId ${ew.customSqlSegment}")
    List<PointConfig> queryList(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("SELECT s.* FROM pointConfig p  LEFT JOIN pointStandard s on p.pointStandardId = s.id ${ew.customSqlSegment}")
    List<PointStandard> queryPointConfigExist(@Param(Constants.WRAPPER) Wrapper wrapper);

//    @Select("SELECT  pc.id,  pc.`level`, pc.pointStandardId, pc.relevanceId, pc.showSort, pc.syncNumber, pc.syncParentNum, pc.createTime, \n" +
//            "  psa.accidentHigh,  psa.accidentLower,  psa.runningHigh,  psa.runningLower,  psa.alarmValue, psa.alarmConfigId, psa.isAlarm \n" +
//            " FROM  pointConfig pc   \n" +
//            " LEFT JOIN pointStandard ps ON pc.pointStandardId = ps.id \n" +
//            " LEFT JOIN pointStandardAlarm psa ON psa.pointStandardId = ps.id \n" +
//            " LEFT JOIN pointAlarm pa ON pa.pointConfigId = pc.id " +
//            " ${ew.customSqlSegment} ")
//    List<PointUnitAndParamTypeResponse> queryStandardAlarm(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);
//
//    @Select("SELECT  pc.id,  pc.`level`, pc.pointStandardId, pc.relevanceId, pc.showSort, pc.syncNumber, pc.syncParentNum, pc.createTime, \n" +
//            " pa.accidentHigh,  pa.accidentLower,  pa.runningHigh,  pa.runningLower, pa.alarmValue, pa.alarmConfigId, pa.isAlarm, \n" +
//            " FROM  pointConfig pc  \n" +
//            " LEFT JOIN pointAlarm pa ON pa.pointConfigId = pc.id " +
//            " ${ew.customSqlSegment} ")
//    List<PointUnitAndParamTypeResponse> queryPointsAlarm(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);


    @Select("SELECT  pc.id, ei.equipmentCode \n" +
            " FROM  pointConfig pc " +
            " LEFT JOIN pointStandard ps ON pc.pointStandardId = ps.id  " +
            " LEFT JOIN equipmentInfoPointStandard eps ON eps.pointStandardId = ps.id " +
            " LEFT JOIN equipmentInfo ei ON ei.id = eps.equipmentInfoId " +
            " ${ew.customSqlSegment} ")
    List<PointUnitAndParamTypeResponse> queryEquipmentInfo(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);

}
