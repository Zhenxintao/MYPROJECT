package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.EnergyCollectConfig;
import com.bmts.heating.commons.basement.model.db.response.EnergyNodeSource;
import com.bmts.heating.commons.entiy.energy.EnergyPointResponse;

import java.util.List;

public interface EnergyCollectConfigService extends IService<EnergyCollectConfig> {

	IPage page(IPage page, Wrapper wrapper);

	List<EnergyPointResponse> queryAll(Wrapper wrapper,Integer type);

	List<EnergyPointResponse> queryConstant(Wrapper wrapper);

	List<EnergyPointResponse> queryVariable(Wrapper wrapper);

	List<EnergyNodeSource> loadEnergyStationRule();

	List<EnergyNodeSource> loadEnergySourceRule();
}
