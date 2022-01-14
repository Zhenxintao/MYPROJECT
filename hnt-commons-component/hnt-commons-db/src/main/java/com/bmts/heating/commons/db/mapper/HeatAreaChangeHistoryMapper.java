package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.HeatAreaChangeHistory;
import com.bmts.heating.commons.basement.model.db.response.HeatAreaChangeHistoryResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface HeatAreaChangeHistoryMapper extends BaseMapper<HeatAreaChangeHistory> {
    @Select("SELECT a.*,b.name FROM `heatAreaChangeHistory` a INNER JOIN heatSource b ON a.relevanceId=b.id ${ew.customSqlSegment}")
    Page<HeatAreaChangeHistoryResponse> queryHeatSourceAreaChangeHistory(Page page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    @Select("SELECT a.*,b.name FROM `heatAreaChangeHistory` a INNER JOIN heatSystem b ON a.relevanceId=b.id  ${ew.customSqlSegment}")
    Page<HeatAreaChangeHistoryResponse> queryHeatSystemAreaChangeHistory(Page page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    @Select("SELECT a.*,b.name FROM `heatAreaChangeHistory` a INNER JOIN heatTransferStation b ON a.relevanceId=b.id ${ew.customSqlSegment}")
    Page<HeatAreaChangeHistoryResponse> queryHeatStationAreaChangeHistory(Page page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    @Select("SELECT a.*,b.name FROM `heatAreaChangeHistory` a INNER JOIN heatNet b ON a.relevanceId=b.id ${ew.customSqlSegment}")
    Page<HeatAreaChangeHistoryResponse> queryHeatNetAreaChangeHistory(Page page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);
}
