package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.EnergyPrice;
import com.bmts.heating.commons.basement.model.db.response.energy.EnergyPriceResponse;

public interface EnergyPriceService extends IService<EnergyPrice> {

	IPage<EnergyPriceResponse> page(IPage page, Wrapper wrapper);
}
