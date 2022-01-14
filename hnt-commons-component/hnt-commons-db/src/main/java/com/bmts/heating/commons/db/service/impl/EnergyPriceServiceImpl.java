package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.EnergyPrice;
import com.bmts.heating.commons.basement.model.db.response.energy.EnergyPriceResponse;
import com.bmts.heating.commons.db.mapper.EnergyPriceMapper;
import com.bmts.heating.commons.db.service.EnergyPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnergyPriceServiceImpl extends ServiceImpl<EnergyPriceMapper, EnergyPrice> implements EnergyPriceService {

	@Autowired
	private EnergyPriceMapper mapper;

	public IPage<EnergyPriceResponse> page(IPage page, Wrapper wrapper){
		return mapper.page(page, wrapper);
	}
}
