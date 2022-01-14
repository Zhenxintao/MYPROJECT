package com.bmts.heating.commons.db.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.HeatDeviceConfig;
import com.bmts.heating.commons.basement.model.db.entity.HeatSource;
import com.bmts.heating.commons.basement.model.db.entity.HeatTransferStation;
import com.bmts.heating.commons.basement.model.db.response.HeatSourceResponse;
import com.bmts.heating.commons.basement.model.db.response.HeatTransferStationResponse;
import com.bmts.heating.commons.entiy.baseInfo.response.HeatDeviceConfigResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author pxf
 * @since 2020-11-16
 */
public interface HeatDeviceConfigMapper extends BaseMapper<HeatDeviceConfig> {


    @Select("SELECT hdc.*,dc.nodeCode,hs.`code`,hs.`name` FROM heatDeviceConfig hdc  \n" +
            " LEFT JOIN deviceConfig dc ON dc.id = hdc.deviceConfigId  \n" +
            " LEFT JOIN heatSource hs ON hs.id = hdc.relevanceId " +
            " ${ew.customSqlSegment} ")
    IPage<HeatDeviceConfigResponse> pageHeatSource(Page<HeatDeviceConfig> page, @Param(Constants.WRAPPER) Wrapper wrapper);


    @Select("SELECT hdc.*,dc.nodeCode,hts.`code` , hts.`name` FROM heatDeviceConfig hdc  \n" +
            " LEFT JOIN deviceConfig dc ON dc.id = hdc.deviceConfigId  \n" +
            " LEFT JOIN heatTransferStation hts ON hts.id = hdc.relevanceId  " +
            " ${ew.customSqlSegment} ")
    IPage<HeatDeviceConfigResponse> pageHeatStation(Page<HeatDeviceConfig> page, @Param(Constants.WRAPPER) Wrapper wrapper);


    @Select("SELECT  hs.*  FROM   heatSource hs \n" +
            " LEFT JOIN  heatDeviceConfig hdc ON hdc.relevanceId= hs.id   " +
            " ${ew.customSqlSegment} ")
    IPage<HeatSourceResponse> heatSourcePage(Page<HeatSource> page, @Param(Constants.WRAPPER) Wrapper wrapper);


    @Select("SELECT  hts.*  FROM  heatTransferStation hts  \n" +
            " LEFT JOIN  heatDeviceConfig hdc ON hdc.relevanceId= hts.id   " +
            " ${ew.customSqlSegment} ")
    IPage<HeatTransferStationResponse> heatStationPage(Page<HeatTransferStation> page, @Param(Constants.WRAPPER) Wrapper wrapper);

}
