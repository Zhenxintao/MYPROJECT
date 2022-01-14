package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.EnergyCollectConfig;
import com.bmts.heating.commons.basement.model.db.response.EnergyNodeSource;
import com.bmts.heating.commons.db.mapper.EnergyCollectConfigMapper;
import com.bmts.heating.commons.db.service.EnergyCollectConfigService;
import com.bmts.heating.commons.entiy.energy.EnergyPointResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnergyCollectConfigServiceImpl extends ServiceImpl<EnergyCollectConfigMapper, EnergyCollectConfig> implements EnergyCollectConfigService {
	@Autowired
	private EnergyCollectConfigMapper mapper;

	public IPage page(IPage page, Wrapper wrapper){
		return mapper.page(page,wrapper);
	}

	public List<EnergyPointResponse> queryAll(Wrapper wrapper, Integer type){return mapper.queryAll(wrapper ,type);}

	public List<EnergyPointResponse> queryConstant(Wrapper wrapper){return mapper.queryConstant(wrapper);}

	public List<EnergyPointResponse> queryVariable(Wrapper wrapper){return mapper.queryVariable(wrapper);}

	public List<EnergyNodeSource> loadEnergyStationRule(){
		return mapper.loadEnergyStationRule();
	}

	public List<EnergyNodeSource> loadEnergySourceRule(){
		return mapper.loadEnergySourceRule();
	}
}
