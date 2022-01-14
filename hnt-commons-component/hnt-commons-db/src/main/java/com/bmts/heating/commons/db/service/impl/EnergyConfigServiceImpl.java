package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.EnergyConfig;
import com.bmts.heating.commons.basement.model.db.response.EnergyConfigResponse;
import com.bmts.heating.commons.db.mapper.EnergyConfigMapper;
import com.bmts.heating.commons.db.service.EnergyConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnergyConfigServiceImpl extends ServiceImpl<EnergyConfigMapper, EnergyConfig> implements EnergyConfigService {

	@Autowired
	private EnergyConfigMapper mapper;

	public IPage page(IPage page, Wrapper wrapper) {
		return mapper.page(page,wrapper);
	}

	@Override
	public List<EnergyConfigResponse> queryConfig(Wrapper wrapper) {
		return mapper.queryConfig(wrapper);
	}
}
