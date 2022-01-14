package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.EnergyDevice;
import com.bmts.heating.commons.basement.model.db.response.EnergyDeviceResponse;


public interface EnergyDeviceService extends IService<EnergyDevice> {
	IPage<EnergyDeviceResponse> getPageStation(IPage<EnergyDevice> page, QueryWrapper wrapper);

	IPage<EnergyDeviceResponse> getPageSource(IPage<EnergyDevice> page, QueryWrapper wrapper);
}
