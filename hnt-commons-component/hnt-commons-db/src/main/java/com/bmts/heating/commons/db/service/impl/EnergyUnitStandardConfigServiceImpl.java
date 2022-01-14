package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.EnergyUnitStandardConfig;
import com.bmts.heating.commons.db.mapper.EnergyUnitStandardConfigMapper;
import com.bmts.heating.commons.db.service.EnergyUnitStandardConfigService;
import org.springframework.stereotype.Service;

@Service
public class EnergyUnitStandardConfigServiceImpl
		extends ServiceImpl<EnergyUnitStandardConfigMapper, EnergyUnitStandardConfig> implements EnergyUnitStandardConfigService {
}
