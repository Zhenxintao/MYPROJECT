package com.bmts.heating.web.energy.service.impl;

import com.bmts.heating.commons.basement.model.db.entity.EnergyDevice;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.entiy.baseInfo.request.energy.EnergyDeviceDto;
import com.bmts.heating.commons.entiy.baseInfo.response.CommonTree;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.energy.service.EnergyDeviceService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class EnergyDeviceServiceImpl implements EnergyDeviceService {

    @Autowired
    private TSCCRestTemplate tsccRestTemplate;


    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response insertEnergyDevice(EnergyDevice energyDevice) {
        return tsccRestTemplate.post("/energy/device/insert", energyDevice, baseServer);
    }

    @Override
    public Response updateEnergyDevice(EnergyDevice energyDevice) {
        return tsccRestTemplate.put("/energy/device/update", energyDevice, baseServer);
    }

    @Override
    public Response deleteEnergyDevice(Integer id) {
        return tsccRestTemplate.delete("/energy/device/delete/" + id, null, baseServer);
    }

    @Override
    public Response searchEnergyDevice(EnergyDeviceDto energyDeviceDto) {
        return tsccRestTemplate.post("/energy/device/search", energyDeviceDto, baseServer);
    }

    @Override
    public Response searchEnergyDeviceById(Integer id) {
        return tsccRestTemplate.get("/energy/device/searchById/" + id, null, baseServer);
    }

    @Override
    public List<CommonTree> sourceSystemTree(BaseDto dto) {
        Response post = tsccRestTemplate.post("monitor/sourceSystemTree", dto, baseServer, Response.class);
        if (post.getData() != null) {
            Gson gson = new Gson();
            CommonTree[] commonTrees = gson.fromJson(gson.toJson(post.getData()), CommonTree[].class);
            return Arrays.asList(commonTrees);
        }
        return Collections.emptyList();
    }

}
