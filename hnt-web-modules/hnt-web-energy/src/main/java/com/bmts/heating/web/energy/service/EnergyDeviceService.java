package com.bmts.heating.web.energy.service;

import com.bmts.heating.commons.basement.model.db.entity.EnergyDevice;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.entiy.baseInfo.request.energy.EnergyDeviceDto;
import com.bmts.heating.commons.entiy.baseInfo.response.CommonTree;
import com.bmts.heating.commons.utils.restful.Response;

import java.util.List;

public interface EnergyDeviceService {
    Response insertEnergyDevice(EnergyDevice energyDevice);

    Response updateEnergyDevice(EnergyDevice energyDevice);

    Response deleteEnergyDevice(Integer id);

    Response searchEnergyDevice(EnergyDeviceDto energyDeviceDto);

    Response searchEnergyDeviceById(Integer id);

    List<CommonTree> sourceSystemTree(BaseDto dto);
}
