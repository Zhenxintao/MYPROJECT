package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.HeatTransferStation;
import com.bmts.heating.commons.basement.model.db.response.CabinetPoint;
import com.bmts.heating.commons.basement.model.db.response.CommonTree;
import com.bmts.heating.commons.basement.model.db.response.HeatTransferStationInfo;
import com.bmts.heating.commons.basement.model.db.response.HeatTransferStationResponse;
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
public interface HeatTransferStationMapper extends BaseMapper<HeatTransferStation> {

//    @Select("select ht.*,ho.name orgName,hs.name heatSourceName from heatTransferStation ht left join heatOrganization ho ON ht.heatOrganizationId = ho.id " +
//            "left JOIN heatSource hs ON ht.heatSourceId = hs.id  ${ew.customSqlSegment}")
//    IPage<HeatTransferStationResponse> queryStationList(Page<HeatTransferStationResponse> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("SELECT ht.*, ho.name orgName, hs.name heatSourceName, b.cabinetCount heatCabinetCount, b.heatSystemCount heatSystemCount FROM heatTransferStation ht \n" +
            "            LEFT JOIN heatOrganization ho ON ht.heatOrganizationId = ho.id \n" +
            "            LEFT JOIN heatSource hs ON ht.heatSourceId = hs.id \n" +
            "            LEFT JOIN \n" +
            "            (SELECT b.id,COUNT(1) cabinetCount,SUM(b.heatSystemCount) heatSystemCount FROM (SELECT tabs.heatTransferStationId id,tabs.heatCabinetId,COUNT(tabs.systemId) heatSystemCount FROM \n" +
            "            (SELECT hc.heatTransferStationId,hc.id heatCabinetId,  hs.id systemId FROM heatCabinet hc LEFT JOIN heatSystem hs ON hc.id = hs.heatCabinetId) tabs \n" +
            "            GROUP BY tabs.heatCabinetId) b GROUP BY b.id) b ON ht.id = b.id ${ew.customSqlSegment}")
    IPage<HeatTransferStationResponse> queryStationPage(Page<HeatTransferStationResponse> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("SELECT ht.*, ho.name orgName, hs.name heatSourceName, b.cabinetCount heatCabinetCount, b.heatSystemCount heatSystemCount FROM heatTransferStation ht \n" +
            "            LEFT JOIN heatOrganization ho ON ht.heatOrganizationId = ho.id \n" +
            "            LEFT JOIN heatSource hs ON ht.heatSourceId = hs.id \n" +
            "            LEFT JOIN \n" +
            "            (SELECT b.id,COUNT(1) cabinetCount,SUM(b.heatSystemCount) heatSystemCount FROM (SELECT tabs.heatTransferStationId id,tabs.heatCabinetId,COUNT(tabs.systemId) heatSystemCount FROM \n" +
            "            (SELECT hc.heatTransferStationId,hc.id heatCabinetId,  hs.id systemId FROM heatCabinet hc LEFT JOIN heatSystem hs ON hc.id = hs.heatCabinetId) tabs \n" +
            "            GROUP BY tabs.heatCabinetId) b GROUP BY b.id) b ON ht.id = b.id ${ew.customSqlSegment}")
    List<HeatTransferStationResponse> queryStationList(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("SELECT org.id,org.name,org.pid,org.level,'org' properties FROM heatOrganization org  " +
            "UNION ALL " +
            "SELECT ht.id,ht.name,ht.heatOrganizationId pid,ho.level+1 LEVEL,'station' properties FROM heatTransferStation ht " +
            "LEFT JOIN heatOrganization ho ON ht.heatOrganizationId = ho.id " +
            "LEFT JOIN heatSource hs ON ht.heatSourceId = hs.id " +
            "${ew.customSqlSegment}")
    List<CommonTree> queryStationTree(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("SELECT hts.id stationId ,pc.pointStandardId ,hc.name cabiNetName ,hc.id ,hs.id heatSystemId " +
            " FROM heatTransferStation hts " +
            " INNER JOIN heatCabinet  hc ON hts.id=hc.heatTransferStationId " +
            " LEFT JOIN heatSystem hs ON hs.heatCabinetId=hc.id  " +
            " LEFT JOIN pointConfig pc ON pc.relevanceId = hs.id" +
            " ${ew.customSqlSegment} ")
    List<CabinetPoint> queryCollectPointAndCabinet(@Param(Constants.WRAPPER) Wrapper wrapper);


    @Select("SELECT  ht.*, ho.`name` AS orgName, hs.`name` AS heatSourceName   FROM  heatTransferStation ht " +
            " LEFT JOIN heatOrganization ho ON ht.heatOrganizationId = ho.id  " +
            " LEFT JOIN heatSource hs ON ht.heatSourceId = hs.id  ${ew.customSqlSegment}")
    IPage<HeatTransferStationInfo> pageStation(Page<HeatTransferStationInfo> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("SELECT  org.id,  org. NAME, org.pid,  org. LEVEL, 'org' properties  FROM heatOrganization org " +
            " UNION ALL " +
            " SELECT ht.id, ht. NAME, ht.heatOrganizationId pid, ho. LEVEL + 1 LEVEL, 'station' properties FROM heatTransferStation ht  " +
            " LEFT JOIN heatOrganization ho ON ht.heatOrganizationId = ho.id " +
            " UNION ALL " +
            " SELECT hs.id, hs.`name`, hc.heatTransferStationId AS pid, ho.`level` + 2 AS `level`, 'system' AS properties FROM heatSystem hs  " +
            " LEFT JOIN heatCabinet hc ON hc.id = hs.heatCabinetId " +
            " RIGHT JOIN heatTransferStation ht ON ht.id = hc.heatTransferStationId " +
            " LEFT JOIN heatOrganization ho ON ht.heatOrganizationId = ho.id  " +
            "${ew.customSqlSegment}")
    List<CommonTree> querySystemTree(@Param(Constants.WRAPPER) Wrapper wrapper);
}
