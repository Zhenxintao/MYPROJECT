package com.bmts.heating.commons.db.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.HeatDeviceConfig;
import com.bmts.heating.commons.basement.model.db.entity.HeatSource;
import com.bmts.heating.commons.basement.model.db.entity.HeatTransferStation;
import com.bmts.heating.commons.basement.model.db.response.HeatSourceResponse;
import com.bmts.heating.commons.basement.model.db.response.HeatTransferStationResponse;
import com.bmts.heating.commons.entiy.baseInfo.response.HeatDeviceConfigResponse;

/**
 * <p>
 * 业务 service
 * </p>
 *
 * @author pxf
 * @since 2020-11-16
 */
public interface HeatDeviceConfigService extends IService<HeatDeviceConfig> {

    IPage<HeatDeviceConfigResponse> pageHeatSource(Page<HeatDeviceConfig> page, Wrapper wrapper);

    IPage<HeatDeviceConfigResponse> pageHeatStation(Page<HeatDeviceConfig> page, Wrapper wrapper);

    IPage<HeatSourceResponse> heatSourcePage(Page<HeatSource> page, Wrapper wrapper);

    IPage<HeatTransferStationResponse> heatStationPage(Page<HeatTransferStation> page, Wrapper wrapper);

}
