package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.db.entity.HeatCabinet;
import com.bmts.heating.commons.basement.model.db.response.HeatCabinetResponse;
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
public interface HeatCabinetMapper extends BaseMapper<HeatCabinet> {


    /**
     * list  获取控制详细信息和统计控制柜下的机组数量
     *
     * @return
     */
    @Select("SELECT hc.*," +
            "(SELECT COUNT(1) FROM heatSystem hs WHERE hs.heatCabinetId = hc.id AND hs.deleteFlag =FALSE) AS heatSystemCount " +
            " FROM  heatCabinet hc  " +
            "${ew.customSqlSegment}")
    List<HeatCabinetResponse> queryCabinet(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);
}
