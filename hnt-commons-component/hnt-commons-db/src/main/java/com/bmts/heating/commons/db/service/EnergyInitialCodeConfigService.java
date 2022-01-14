package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.EnergyInitialCodeConfig;
import com.bmts.heating.commons.basement.model.db.response.energy.EnergyInitialCodeConfigResponse;


public interface EnergyInitialCodeConfigService extends IService<EnergyInitialCodeConfig> {

	IPage<EnergyInitialCodeConfigResponse> pageStation(IPage page, Wrapper wrapper);

	IPage<EnergyInitialCodeConfigResponse> pageSource(IPage page, Wrapper wrapper);
}
