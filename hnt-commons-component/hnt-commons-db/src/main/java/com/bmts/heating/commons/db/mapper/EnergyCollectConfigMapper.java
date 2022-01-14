package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.db.entity.EnergyCollectConfig;
import com.bmts.heating.commons.basement.model.db.response.EnergyNodeSource;
import com.bmts.heating.commons.entiy.energy.EnergyCollectConfigResponse;
import com.bmts.heating.commons.entiy.energy.EnergyPointResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EnergyCollectConfigMapper extends BaseMapper<EnergyCollectConfig> {


    @Select("SELECT hc.id AS cabinetId,hc.name AS cabinetName,hs.id AS systemId,hs.name AS systemName,ecc.* FROM energy_collect_config ecc  " +
            " INNER JOIN heatSystem hs ON ecc.relevanceId = hs.id " +
            " INNER JOIN heatCabinet hc ON hs.heatCabinetId = hc.id ${ew.customSqlSegment}")
    IPage<EnergyCollectConfigResponse> page(IPage page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("SELECT hc.id AS cabinetId,hc.name AS cabinetName,hs.id AS systemId, hs.name systemName,CONCAT(hs.name,'-',ps.name) pointStandardName,ps.columnName,pc.id pointId,'' val  FROM pointConfig pc " +
            " INNER JOIN pointStandard ps ON pc.pointStandardId = ps.id  " +
            " INNER JOIN energy_config ec ON ec.pointStandardId = ps.id  " +
            " INNER JOIN  heatSystem hs ON hs.id = pc.relevanceId " +
            " INNER JOIN heatCabinet hc ON hs.heatCabinetId = hc.id ${ew.customSqlSegment} And ec.type = #{type}" +
            " UNION ALL " +
            " SELECT hc.id AS cabinetId,hc.name AS cabinetName,hs.id AS systemId, hs.name systemName, CONCAT(hs.name,'-系统面积') pointStandardName,'area' columnName,'' pointId,hs.heatArea val FROM heatSystem hs " +
            " INNER JOIN heatCabinet hc ON hs.heatCabinetId = hc.id ${ew.customSqlSegment}")
    List<EnergyPointResponse> queryAll(@Param(Constants.WRAPPER) Wrapper wrapper, @Param("type") Integer type);

    @Select("SELECT hc.id AS cabinetId,hc.name AS cabinetName,hs.id AS systemId, hs.name systemName,CONCAT(hs.name,'-',ps.name) pointStandardName,ps.columnName,pc.id pointId,'' val  FROM pointConfig pc " +
            " INNER JOIN pointStandard ps ON pc.pointStandardId = ps.id  " +
            " INNER JOIN energy_config ec ON ec.pointStandardId = ps.id  " +
            " INNER JOIN  heatSystem hs ON hs.id = pc.relevanceId " +
            " INNER JOIN heatCabinet hc ON hs.heatCabinetId = hc.id ${ew.customSqlSegment} ")
    List<EnergyPointResponse> queryVariable(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("SELECT hc.id AS cabinetId,hc.name AS cabinetName,hs.id AS systemId, hs.name systemName, CONCAT(hs.name,'-系统面积') pointStandardName,'area' columnName,'' pointId,hs.heatArea val FROM heatSystem hs " +
            " INNER JOIN heatCabinet hc ON hs.heatCabinetId = hc.id ${ew.customSqlSegment}")
    List<EnergyPointResponse> queryConstant(@Param(Constants.WRAPPER) Wrapper wrapper);


    //获取需要计算的站
    @Select("SELECT hts.id masterId, " +
            "  ecc.id AS id, " +
            "  ecc.relevanceId, " +
            "  ecc.pointTargetId, " +
            "  ecc.pointTargetName, " +
            "  ecc.expression, " +
            "  ecc.isConverge, " +
            "  ecc.computeType, " +
            "  ecc.sign " +
            "  FROM energy_collect_config ecc LEFT JOIN heatSystem hs ON ecc.relevanceId = hs.id " +
            "  INNER JOIN heatCabinet hc ON hc.id = hs.heatCabinetId " +
            "  INNER JOIN heatTransferStation hts ON hts.id = hc.heatTransferStationId ")
    List<EnergyNodeSource> loadEnergyStationRule();

    //获取需要计算的热源
    @Select("SELECT hts.id masterId, " +
            "  ecc.id AS id, " +
            "  ecc.relevanceId, " +
            "  ecc.pointTargetId ," +
            "  ecc.pointTargetName, " +
            "  ecc.expression, " +
            "  ecc.isConverge, " +
            "  ecc.computeType, " +
            "  ecc.sign " +
            "  FROM energy_collect_config ecc LEFT JOIN heatSystem hs ON ecc.relevanceId = hs.id " +
            "  INNER JOIN heatCabinet hc ON hc.id = hs.heatCabinetId " +
            "  INNER JOIN heatSource hts ON hts.id = hc.heatTransferStationId ")
    List<EnergyNodeSource> loadEnergySourceRule();
}
