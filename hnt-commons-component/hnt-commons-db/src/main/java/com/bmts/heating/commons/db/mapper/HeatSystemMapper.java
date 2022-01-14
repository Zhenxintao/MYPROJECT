package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.cache.FirstNetBase;
import com.bmts.heating.commons.basement.model.db.entity.HeatSystem;
import com.bmts.heating.commons.basement.model.db.response.station.HeatSystemCSResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author naming
 * @since 2020-11-10
 */
public interface HeatSystemMapper extends BaseMapper<HeatSystem> {


    /**
     * 关联 网 、源、站、控制柜、系统 查询
     *
     * @return
     */
    @Select("SELECT hss.id AS heatSystemId,hss.`name` AS heatSystemName,hss.heatingType, hss.heatArea AS heatSystemArea,hc.id AS heatCabinetId,hc.`name` AS heatCabinetName," +
            "hts.id AS heatTransferStationId,hts.`name` AS heatTransferStationName,hts.heatArea AS heatStationArea,hts.netArea AS heatStationNetArea,hts.status," +
            "hts.heatOrganizationId AS heatStationOrgId,ho.`name` AS heatStationOrgName,hs.id AS heatSourceId,hs.`name` AS heatSourceName," +
            "hs.heatArea AS heatSourceArea,hs.heatOrganizationId AS heatSourceOrgId,hor.`name` AS heatSourceOrgName,hn.id AS heatNetId," +
            "hn.`name` AS heatNetName,hn.heatArea AS heatNetArea,hn.heatOrganizationId AS heatNetOrgId,horg.`name` AS heatNetOrgName  FROM  `heatSystem` hss  " +
            "LEFT JOIN heatCabinet hc ON hc.id = hss.heatCabinetId  AND hc.deleteFlag = 0  " +
            "LEFT JOIN heatTransferStation hts ON hts.id = hc.heatTransferStationId  AND hts.deleteFlag = 0   " +
            "LEFT JOIN heatOrganization ho ON ho.id = hts.heatOrganizationId  AND ho.deleteFlag = 0  " +
            "LEFT JOIN heatSource hs ON hs.id = hts.heatSourceId  AND hs.deleteFlag = 0  " +
            "LEFT JOIN heatOrganization hor ON hor.id = hs.heatOrganizationId  AND hor.deleteFlag = 0  " +
            "JOIN heatNetSource hns ON hns.heatSourceId = hs.id  " +
            "JOIN heatNet hn ON hn.id = hns.heatNetId  AND hn.deleteFlag = 0   " +
            "LEFT JOIN heatOrganization horg ON horg.id = hn.heatOrganizationId  AND horg.deleteFlag = 0 " +
            " ${ew.customSqlSegment}")
    List<FirstNetBase> querySystem(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    @Select("SELECT hss.id AS heatSystemId,hss.`name` AS heatSystemName,hss.heatingType,\n" +
            " hss.heatArea AS heatSystemArea,hc.id AS heatCabinetId,hc.`name`\n" +
            "  AS heatCabinetName,hs.id AS heatSourceId,hs.`name` AS heatSourceName,hs.heatArea AS heatSourceArea,hs.heatOrganizationId \n" +
            "   AS heatSourceOrgId,hor.`name` AS heatSourceOrgName,hn.id AS heatNetId,hn.`name` AS heatNetName,hn.heatArea \n" +
            "   AS heatNetArea,hn.heatOrganizationId AS heatNetOrgId,horg.`name` AS heatNetOrgName FROM `heatSystem` hss \n" +
            "LEFT JOIN heatCabinet hc ON hc.id = hss.heatCabinetId AND hc.deleteFlag = 0 \n" +
            " LEFT JOIN heatSource hs ON hs.id = hc.heatSourceId AND hs.deleteFlag = 0 \n" +
            " LEFT JOIN heatOrganization hor ON hor.id = hs.heatOrganizationId AND hor.deleteFlag = 0 \n" +
            "  JOIN heatNetSource hns ON hns.heatSourceId = hs.id  " +
            "  JOIN heatNet hn ON hn.id = hns.heatNetId  AND hn.deleteFlag = 0   " +
            "LEFT JOIN heatOrganization horg ON horg.id = hn.heatOrganizationId AND horg.deleteFlag = 0" +
            " ${ew.customSqlSegment}")
    List<FirstNetBase> querySourceSystem(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);


    /**
     * 获取站下所有机组
     *
     * @return
     */
    @Select("SELECT hts.id stationId, hts.name stationName,hc.name cabinetName,hc.id cabinetId, hs.name heatSystemName,hs.id heatSystemId FROM heatSystem hs " +
            "INNER JOIN heatCabinet hc ON hs.heatCabinetId = hc.id " +
            "INNER JOIN heatTransferStation hts ON hc.heatTransferStationId = hts.id " +
            "${ew.customSqlSegment}")
    List<HeatSystemCSResponse> querySystemNameByStationId(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    /**
     * 获取站下所有机组
     *
     * @return
     */
    @Select("SELECT hs.id FROM heatSystem hs INNER JOIN heatCabinet hc ON hs.heatCabinetId = hc.id " +
            "${ew.customSqlSegment}")
    List<Integer> querySystemIdByHeatSourceId(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);

//    /**
//     * 获取控制柜下
//     *
//     * @return
//     */
//    @Select("SELECT hs.id FROM heatSystem hs INNER JOIN heatCabinet hc ON hs.heatCabinetId = hc.id " +
//            "${ew.customSqlSegment}")
//    List<Integer> querySystemIdByHeatSourceId(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);
}
