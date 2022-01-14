package com.bmts.heating.web.backStage.controller.equipment;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.db.entity.EquipmentInfo;
import com.bmts.heating.commons.basement.model.db.entity.EquipmentInfoPointStandard;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.request.InsertEquipmentDto;
import com.bmts.heating.commons.basement.model.db.response.EquipmentInfoResponse;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.equipment.EquipmentInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Api(tags = "设备信息管理")
@RestController
@RequestMapping("/equipmentInfo")
public class EquipmentInfoController {

    @Autowired
    private EquipmentInfoService equipmentInfoService;

    @ApiOperation(value = "添加设备信息",response = EquipmentInfo.class)
    @PostMapping("/insertEquipmentInfo")
    public Response insertEquipmentInfo(@RequestBody List<EquipmentInfo> dto) {
       return  equipmentInfoService.insertEquipmentInfo(dto);
    }

    @ApiOperation(value = "修改设备信息",response = EquipmentInfo.class)
    @PutMapping("/updateEquipmentInfo")
    public Response updateEquipmentInfo(@RequestBody EquipmentInfo dto) {
        return equipmentInfoService.updateEquipmentInfo(dto);
    }

    @ApiOperation("删除设备信息")
    @DeleteMapping("/deleteEquipmentInfo")
    public Response deleteEquipmentInfo(@RequestParam Integer id) {
      return   equipmentInfoService.deleteEquipmentInfo(id);
    }

    @ApiOperation(value = "查询设备信息",response = EquipmentInfo.class)
    @PostMapping("/queryEquipmentInfo")
    public Response queryEquipmentInfo(@RequestBody BaseDto dto) {
        return   equipmentInfoService.queryEquipmentInfo(dto);
    }

    @ApiOperation(value = "根据设备Id查询已绑定点位信息",response = EquipmentInfoResponse.class)
    @GetMapping("/queryEquipmentInfoPointStandard")
    public Response queryEquipmentInfoPointStandard(@RequestParam Integer id) {
        return equipmentInfoService.queryEquipmentInfoPointStandard(id);
    }

    @ApiOperation(value = "根据设备Id查询未绑定点位信息",response = PointStandard.class)
    @GetMapping("/queryEquipmentInfoPointStandardNot")
    public Response queryEquipmentInfoPointStandardNot(@RequestParam Integer id) {
        return equipmentInfoService.queryEquipmentInfoPointStandardNot(id);
    }


    @ApiOperation(value = "添加设备关联点位信息",response = EquipmentInfoPointStandard.class)
    @PostMapping("/insertEquipmentInfoPointStandard")
    public Response insertEquipmentInfoPointStandard(@RequestBody List<EquipmentInfoPointStandard> dto) {
        return equipmentInfoService.insertEquipmentInfoPointStandard(dto);
    }

    @ApiOperation("删除设备关联点位信息")
    @PostMapping("/deleteEquipmentInfoPointStandard")
    public Response deleteEquipmentInfoPointStandard(@RequestBody List<Integer> ids) {
        return equipmentInfoService.deleteEquipmentInfoPointStandard(ids);
    }

    @ApiOperation("同步添加设备及点位信息")
    @PostMapping("/insertSyncEquipmentInfo")
    public Response insertSyncEquipmentInfo(@RequestBody InsertEquipmentDto dto) {
        return equipmentInfoService.insertSyncEquipmentInfo(dto);
    }

    @ApiOperation("核实设备code码是否存在")
    @GetMapping("/queryEquipmentCodeStatus")
    public Response queryEquipmentCodeStatus(@RequestParam String code) {
        return equipmentInfoService.queryEquipmentCodeStatus(code);
    }

}
