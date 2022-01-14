package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.EnergyEvaluateConfig;
import com.bmts.heating.commons.db.mapper.EnergyEvaluateConfigMapper;
import com.bmts.heating.commons.db.service.EnergyEvaluateConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnergyEvaluateConfigServiceImpl extends ServiceImpl<EnergyEvaluateConfigMapper, EnergyEvaluateConfig> implements EnergyEvaluateConfigService {

	@Autowired
	private EnergyEvaluateConfigMapper mapper;


}
