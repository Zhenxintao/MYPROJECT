package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.EnergyRateConfig;
import com.bmts.heating.commons.basement.model.db.response.energy.EnergyInitialCodeConfigResponse;
import com.bmts.heating.commons.basement.model.db.response.energy.EnergyRateConfigResponse;
import com.bmts.heating.commons.db.mapper.EnergyRateConfigMapper;
import com.bmts.heating.commons.db.service.EnergyRateConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnergyRateConfigServiceImpl  extends ServiceImpl<EnergyRateConfigMapper, EnergyRateConfig> implements EnergyRateConfigService {

	@Autowired
	private EnergyRateConfigMapper mapper;

	@Override
	public IPage<EnergyRateConfigResponse> pageStation(IPage page, Wrapper wrapper) {
		return mapper.pageStation(page,wrapper);
	}

	@Override
	public IPage<EnergyRateConfigResponse> pageSource(IPage page, Wrapper wrapper) {
		return mapper.pageSource(page,wrapper);
	}
}
