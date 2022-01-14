package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.EnergyDevice;
import com.bmts.heating.commons.basement.model.db.response.EnergyDeviceResponse;
import com.bmts.heating.commons.db.mapper.EnergyDeviceMapper;
import com.bmts.heating.commons.db.service.EnergyDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnergyDeviceServiceImpl extends ServiceImpl<EnergyDeviceMapper, EnergyDevice> implements EnergyDeviceService {

	@Autowired
	private EnergyDeviceMapper energyDeviceMapper;

	@Override
	public IPage<EnergyDeviceResponse> getPageStation(IPage<EnergyDevice> page, QueryWrapper wrapper) {
		return energyDeviceMapper.getPageStation(page,wrapper);
	}

	@Override
	public IPage<EnergyDeviceResponse> getPageSource(IPage<EnergyDevice> page, QueryWrapper wrapper) {
		return energyDeviceMapper.getPageSource(page,wrapper);
	}
}
