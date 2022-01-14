package com.bmts.heating.web.backStage.service.equipment;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.db.entity.AlarmConfig;
import com.bmts.heating.commons.basement.model.db.entity.EquipmentInfo;
import com.bmts.heating.commons.basement.model.db.entity.EquipmentInfoPointStandard;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.request.InsertEquipmentDto;
import com.bmts.heating.commons.entiy.baseInfo.request.AlarmConfigDto;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface EquipmentInfoService {

    Response insertEquipmentInfo(List<EquipmentInfo> dto);

    Response updateEquipmentInfo(EquipmentInfo dto);

    Response deleteEquipmentInfo(Integer id);

    Response queryEquipmentInfo(BaseDto dto);

    Response queryEquipmentInfoPointStandard(Integer id);

    Response queryEquipmentInfoPointStandardNot(Integer id);

    Response insertEquipmentInfoPointStandard(List<EquipmentInfoPointStandard> dto);

    Response deleteEquipmentInfoPointStandard(List<Integer> ids);

    Response insertSyncEquipmentInfo(InsertEquipmentDto dto);

    Response queryEquipmentCodeStatus(String code);

}
