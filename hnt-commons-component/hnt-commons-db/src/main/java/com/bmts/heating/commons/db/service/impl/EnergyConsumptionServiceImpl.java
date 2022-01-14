package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.EnergyConsumption;
import com.bmts.heating.commons.db.mapper.EnergyConsumptionMapper;
import com.bmts.heating.commons.db.service.EnergyConsumptionService;
import org.springframework.stereotype.Service;

@Service
public class EnergyConsumptionServiceImpl extends ServiceImpl<EnergyConsumptionMapper, EnergyConsumption> implements EnergyConsumptionService {
}
