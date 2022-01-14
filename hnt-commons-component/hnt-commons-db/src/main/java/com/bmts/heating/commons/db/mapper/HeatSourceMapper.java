package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.HeatSource;
import com.bmts.heating.commons.basement.model.db.response.CabinetPoint;
import com.bmts.heating.commons.basement.model.db.response.CommonTree;
import com.bmts.heating.commons.basement.model.db.response.HeatSourceBaseResponse;
import com.bmts.heating.commons.basement.model.db.response.HeatSourceResponse;
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

public interface HeatSourceMapper extends BaseMapper<HeatSource> {
    /**
     * 关联热网表、组织结构表联合查询
     *
     * @param page
     * @return
     */
    @Select("SELECT hs.*, ho.name AS heatOrganizationName FROM heatSource hs LEFT JOIN heatOrganization ho ON hs.heatOrganizationId=ho.id ${ew.customSqlSegment}")
    Page<HeatSourceResponse> queryHeatSource(Page page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    @Select("SELECT hts.id stationId ,pc.pointStandardId ,hc.name cabiNetName ,hc.id ,hs.id heatSystemId " +
            " FROM heatSource hts " +
            " INNER JOIN heatCabinet  hc ON hts.id=hc.heatSourceId " +
            " LEFT JOIN heatSystem hs ON hs.heatCabinetId=hc.id  " +
            " LEFT JOIN pointConfig pc ON pc.relevanceId = hs.id" +
            " ${ew.customSqlSegment} ")
    List<CabinetPoint> queryCollectPointAndCabinet(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("SELECT hts.id heatSourceId,hts.name heatSourceName ,hc.name cabinetName ,hc.id cabinetId," +
            "hs.id heatSystemId ,hs.name heatSystemName" +
            " FROM heatSource hts " +
            " INNER JOIN heatCabinet  hc ON hts.id=hc.heatSourceId " +
            " LEFT JOIN heatSystem hs ON hs.heatCabinetId=hc.id  " +
            " ${ew.customSqlSegment} ")
    Page<HeatSourceBaseResponse> querySourceToSystem(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("SELECT hs.id,hs.heatArea FROM heatSystem hs INNER JOIN heatCabinet hc ON hs.`heatCabinetId`=hc.id INNER JOIN heatSource ht ON ht.id = hc.heatSourceId WHERE ht.id = #{id}")
    HeatSourceResponse heatSourceArea(@Param("id") int id);

    @Select("SELECT  hs.id, hs. NAME,  hs.heatOrganizationId pid, 1 AS LEVEL,  'source' properties  FROM  heatSource hs  " +
            "UNION ALL  " +
            "SELECT  hss.id,  hss.`name`,  hc.heatSourceId AS pid,  2 AS `level`,  'system' AS properties  FROM  heatSystem hss  " +
            "  LEFT JOIN heatCabinet hc ON hc.id = hss.heatCabinetId  " +
            "  RIGHT JOIN heatSource hs ON hs.id = hc.heatSourceId  " +
            "  LEFT JOIN heatOrganization ho ON hs.heatOrganizationId = ho.id  " +
            "${ew.customSqlSegment}")
    List<CommonTree> sourceSystemTree(@Param(Constants.WRAPPER) Wrapper wrapper);
}
