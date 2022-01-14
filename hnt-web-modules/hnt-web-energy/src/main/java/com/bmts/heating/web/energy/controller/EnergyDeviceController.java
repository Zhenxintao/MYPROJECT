package com.bmts.heating.web.energy.controller;

import com.bmts.heating.commons.basement.model.db.entity.EnergyDevice;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.entiy.baseInfo.request.energy.EnergyDeviceDto;
import com.bmts.heating.commons.entiy.baseInfo.response.CommonTree;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.energy.service.EnergyDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/energy/device")
@Api(tags = "能耗设备配置")
public class EnergyDeviceController {
    @Autowired
    private EnergyDeviceService energyDeviceService;

    @PostMapping("/insert")
    @ApiOperation("添加能耗设备配置")
    public Response insertEnergyDevice(@RequestBody EnergyDevice energyDevice) {
        return energyDeviceService.insertEnergyDevice(energyDevice);
    }

    @PutMapping("/update")
    @ApiOperation("修改能耗设备配置")
    public Response updateEnergyDevice(@RequestBody EnergyDevice energyDevice) {
        return energyDeviceService.updateEnergyDevice(energyDevice);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除能耗设备")
    public Response deleteEnergyDevice(@PathVariable Integer id) {
        return energyDeviceService.deleteEnergyDevice(id);
    }


    @PostMapping("/search")
    @ApiOperation("查询能耗设备")
    public Response searchEnergyDevice(@RequestBody EnergyDeviceDto energyDeviceDto) {
        return energyDeviceService.searchEnergyDevice(energyDeviceDto);
    }

    @GetMapping("/searchById/{id}")
    @ApiOperation("根据id查询单条")
    public Response searchEnergyDeviceById(@PathVariable Integer id) {
        return energyDeviceService.searchEnergyDeviceById(id);
    }

    @ApiOperation(value = "获取热源-系统树", response = CommonTree.class)
    @PostMapping("/sourceSystemTree")
    public Response sourceSystemTree(@RequestBody BaseDto dto, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) dto.setUserId(userId);
        try {
            List<CommonTree> res = energyDeviceService.sourceSystemTree(dto);
            return Response.success(res);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.success();
        }
    }


}
