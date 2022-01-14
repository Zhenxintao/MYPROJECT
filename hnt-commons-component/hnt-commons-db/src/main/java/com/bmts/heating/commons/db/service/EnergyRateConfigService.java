package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.EnergyRateConfig;
import com.bmts.heating.commons.basement.model.db.response.energy.EnergyRateConfigResponse;

public interface EnergyRateConfigService extends IService<EnergyRateConfig> {

	IPage<EnergyRateConfigResponse> pageStation(IPage page, Wrapper wrapper);

	IPage<EnergyRateConfigResponse> pageSource(IPage page, Wrapper wrapper);
}
