package com.bmts.heating.web.backStage.service.equipment.impl;

import com.bmts.heating.commons.basement.model.db.entity.AlarmConfig;
import com.bmts.heating.commons.basement.model.db.entity.EquipmentInfo;
import com.bmts.heating.commons.basement.model.db.entity.EquipmentInfoPointStandard;
import com.bmts.heating.commons.basement.model.db.request.InsertEquipmentDto;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.AlarmConfigDto;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.AlarmConfigService;
import com.bmts.heating.web.backStage.service.equipment.EquipmentInfoService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EquipmentServiceImpl extends SavantServices implements EquipmentInfoService {

    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";


    @Override
    public Response insertEquipmentInfo(List<EquipmentInfo> dto) {
        return backRestTemplate.post("equipmentInfo/insertEquipmentInfo", dto, baseServer);
    }

    @Override
    public Response updateEquipmentInfo(EquipmentInfo dto) {
        return backRestTemplate.put("equipmentInfo/updateEquipmentInfo", dto, baseServer);
    }

    @Override
    public Response deleteEquipmentInfo(Integer id) {
        return backRestTemplate.delete("equipmentInfo/deleteEquipmentInfo?id="+id, baseServer);
    }

    @Override
    public Response queryEquipmentInfo(BaseDto dto) {
        return backRestTemplate.post("equipmentInfo/queryEquipmentInfo",dto, baseServer);
    }

    @Override
    public Response queryEquipmentInfoPointStandard(Integer id) {
        return backRestTemplate.get("equipmentInfo/queryEquipmentInfoPointStandard?id="+id, baseServer);
    }

    @Override
    public Response queryEquipmentInfoPointStandardNot(Integer id) {
        return backRestTemplate.get("equipmentInfo/queryEquipmentInfoPointStandardNot?id="+id, baseServer);
    }

    @Override
    public Response insertEquipmentInfoPointStandard(List<EquipmentInfoPointStandard> dto) {
        return backRestTemplate.post("equipmentInfo/insertEquipmentInfoPointStandard",dto, baseServer);
    }

    @Override
    public Response deleteEquipmentInfoPointStandard(List<Integer> ids) {
        return backRestTemplate.post("equipmentInfo/deleteEquipmentInfoPointStandard",ids, baseServer);
    }

    @Override
    public Response insertSyncEquipmentInfo(InsertEquipmentDto dto) {
        return backRestTemplate.post("equipmentInfo/insertSyncEquipmentInfo",dto, baseServer);
    }

    @Override
    public Response queryEquipmentCodeStatus(String code) {
        return backRestTemplate.get("equipmentInfo/queryEquipmentCodeStatus?code="+code, baseServer);
    }
}
