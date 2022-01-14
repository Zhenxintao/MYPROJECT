package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.EnergyInitialCodeConfig;
import com.bmts.heating.commons.basement.model.db.response.energy.EnergyInitialCodeConfigResponse;
import com.bmts.heating.commons.db.mapper.EnergyInitialCodeConfigMapper;
import com.bmts.heating.commons.db.service.EnergyInitialCodeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EnergyInitialConfigServiceImpl  extends ServiceImpl<EnergyInitialCodeConfigMapper, EnergyInitialCodeConfig> implements EnergyInitialCodeConfigService {

	@Autowired
	private EnergyInitialCodeConfigMapper mapper;

	public IPage<EnergyInitialCodeConfigResponse> pageStation(IPage page, Wrapper wrapper){
		return mapper.pageStation(page,wrapper);
	}

	public IPage<EnergyInitialCodeConfigResponse> pageSource(IPage page, Wrapper wrapper){
		return mapper.pageSource(page,wrapper);
	}


}
