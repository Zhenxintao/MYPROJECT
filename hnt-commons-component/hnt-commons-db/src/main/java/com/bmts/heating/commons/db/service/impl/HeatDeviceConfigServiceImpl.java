package com.bmts.heating.commons.db.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.HeatDeviceConfig;
import com.bmts.heating.commons.basement.model.db.entity.HeatSource;
import com.bmts.heating.commons.basement.model.db.entity.HeatTransferStation;
import com.bmts.heating.commons.basement.model.db.response.HeatSourceResponse;
import com.bmts.heating.commons.basement.model.db.response.HeatTransferStationResponse;
import com.bmts.heating.commons.db.mapper.HeatDeviceConfigMapper;
import com.bmts.heating.commons.db.service.HeatDeviceConfigService;
import com.bmts.heating.commons.entiy.baseInfo.response.HeatDeviceConfigResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务  service  实现类
 * </p>
 *
 * @author naming
 * @since 2020-11-16
 */
@Service
public class HeatDeviceConfigServiceImpl extends ServiceImpl<HeatDeviceConfigMapper, HeatDeviceConfig> implements HeatDeviceConfigService {


    @Autowired
    private HeatDeviceConfigMapper heatDeviceConfigMapper;

    @Override
    public IPage<HeatDeviceConfigResponse> pageHeatSource(Page<HeatDeviceConfig> page, Wrapper wrapper) {
        return heatDeviceConfigMapper.pageHeatSource(page, wrapper);
    }

    @Override
    public IPage<HeatDeviceConfigResponse> pageHeatStation(Page<HeatDeviceConfig> page, Wrapper wrapper) {
        return heatDeviceConfigMapper.pageHeatStation(page, wrapper);
    }

    @Override
    public IPage<HeatSourceResponse> heatSourcePage(Page<HeatSource> page, Wrapper wrapper) {
        return heatDeviceConfigMapper.heatSourcePage(page, wrapper);
    }

    @Override
    public IPage<HeatTransferStationResponse> heatStationPage(Page<HeatTransferStation> page, Wrapper wrapper) {
        return heatDeviceConfigMapper.heatStationPage(page, wrapper);
    }
}
