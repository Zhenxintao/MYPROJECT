package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.HeatNet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmts.heating.commons.basement.model.db.entity.NetSource;
import com.bmts.heating.commons.basement.model.db.response.HeatNetResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
public interface HeatNetMapper extends BaseMapper<HeatNet> {
    @Select("SELECT hn.*,ho.name as heatOrganizationName FROM heatNet hn " +
            "LEFT JOIN  heatOrganization ho ON hn.heatOrganizationId=ho.id ${ew.customSqlSegment}")
    Page<HeatNetResponse> queryHeatNet(Page page,@Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    @Select("SELECT hs.name AS sourceName,hs.id sourceId,hn.name netName,hn.id AS netId FROM heatSource hs " +
            " INNER JOIN heatNetSource hns ON hs.`id` = hns.heatSourceId " +
            " LEFT JOIN heatNet hn ON hns.heatNetId = hn.id  ${ew.customSqlSegment} " +
            " AND hn.deleteFlag = 0 ")
    List<NetSource> netJoinSource(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);
}
