package com.bmts.heating.bussiness.baseInformation.app.joggle.sync;

import com.bmts.heating.bussiness.baseInformation.app.ann.ClearCache;
import com.bmts.heating.bussiness.baseInformation.app.handler.DataSyncHandler;
import com.bmts.heating.commons.entiy.baseInfo.sync.InitDto;
import com.bmts.heating.commons.entiy.baseInfo.sync.add.AddHeatOrganizationInitDto;
import com.bmts.heating.commons.entiy.baseInfo.sync.add.SyncHeatDicDto;
import com.bmts.heating.commons.entiy.baseInfo.sync.update.*;
import com.bmts.heating.commons.redis.utils.RedisKeys;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: BasicDataSysncJoggle
 * @Description: 数据初始化同步
 * @Author: pxf
 * @Date: 2021/6/2 16:30
 * @Version: 1.0
 */

@Api(tags = "基础数据同步接口")
@RestController
@RequestMapping("/basicsync")
@Slf4j
public class BasicDataSyncJoggle {

    @Autowired
    private DataSyncHandler dataSyncHandler;


    @ApiOperation(value = "初始化数据同步")
    @PostMapping("/sync")
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response initSync(@RequestBody InitDto dtos) {
        return dataSyncHandler.initSync(dtos);
    }


    @ApiOperation(value = "添加组织结构数据")
    @PostMapping("/organize/add")
    public Response addOrganize(@RequestBody AddHeatOrganizationInitDto addOrganDto) {
        return dataSyncHandler.addHeatOrganization(addOrganDto);
    }

    @ApiOperation(value = "编辑组织结构数据")
    @PutMapping("/organize/update")
    public Response updateOrganize(@RequestBody List<HeatOrganizationInitDto> heatOrganList) {
        return dataSyncHandler.updateOrganization(heatOrganList);
    }

    @ApiOperation(value = "删除组织结构数据")
    @DeleteMapping("/organize/del")
    public Response delOrganize(@RequestBody List<Integer> numList) {
        return dataSyncHandler.delOrganization(numList);
    }


    @ApiOperation(value = "添加热网数据")
    @PostMapping("/heatNet/add")
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response addHeatNet(@RequestBody List<HeatNetInitDto> addList) {
        return dataSyncHandler.addHeatNet(addList);
    }

    @ApiOperation(value = "编辑热网数据")
    @PutMapping("/heatNet/update")
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response updateHeatNet(@RequestBody List<HeatNetInitDto> updateList) {
        return dataSyncHandler.updateHeatNet(updateList);
    }

    @ApiOperation(value = "删除热网数据")
    @DeleteMapping("/heatNet/del")
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response delHeatNet(@RequestBody List<Integer> numList) {
        return dataSyncHandler.delHeatNet(numList);
    }


    @ApiOperation(value = "添加热源数据")
    @PostMapping("/heatSource/add")
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response addHeatSource(@RequestBody List<HeatSourceInitDto> addList) {
        return dataSyncHandler.addHeatSource(addList);
    }

    @ApiOperation(value = "编辑热源数据")
    @PutMapping("/heatSource/update")
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response updateHeatSource(@RequestBody List<HeatSourceInitDto> updateList) {
        return dataSyncHandler.updateHeatSource(updateList);
    }

    @ApiOperation(value = "删除热源数据")
    @DeleteMapping("/heatSource/del")
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response delHeatSource(@RequestBody List<Integer> numList) {
        return dataSyncHandler.delHeatSource(numList);
    }

    @ApiOperation(value = "添加热力站数据")
    @PostMapping("/heatStation/add")
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response addHeatStation(@RequestBody List<HeatStationInitDto> addList) {
        return dataSyncHandler.addHeatStation(addList);
    }

    @ApiOperation(value = "编辑热力站数据")
    @PutMapping("/heatStation/update")
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response updateHeatStation(@RequestBody List<HeatStationInitDto> updateList) {
        return dataSyncHandler.updateHeatStation(updateList);
    }

    @ApiOperation(value = "删除热力站数据")
    @DeleteMapping("/heatStation/del")
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response delHeatStation(@RequestBody List<Integer> numList) {
        return dataSyncHandler.delHeatStation(numList);
    }

    @ApiOperation(value = "添加控制柜数据")
    @PostMapping("/heatCabinet/add")
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response addHeatCabinet(@RequestBody List<HeatCabinetInitDto> addList) {
        return dataSyncHandler.addHeatCabinet(addList);
    }

    @ApiOperation(value = "编辑控制柜数据")
    @PutMapping("/heatCabinet/update")
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response updateHeatCabinet(@RequestBody List<HeatCabinetInitDto> updateList) {
        return dataSyncHandler.updateHeatCabinet(updateList);
    }

    @ApiOperation(value = "删除控制柜数据")
    @DeleteMapping("/heatCabinet/del")
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response delHeatCabinet(@RequestBody List<Integer> numList) {
        return dataSyncHandler.delHeatCabinet(numList);
    }

    @ApiOperation(value = "添加系统数据")
    @PostMapping("/heatSystem/add")
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response addHeatSystem(@RequestBody List<HeatSystemInitDto> addList) {
        return dataSyncHandler.addHeatSystem(addList);
    }

    @ApiOperation(value = "编辑系统数据")
    @PutMapping("/heatSystem/update")
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response updateHeatSystem(@RequestBody List<HeatSystemInitDto> updateList) {
        return dataSyncHandler.updateHeatSystem(updateList);
    }

    @ApiOperation(value = "删除系统数据")
    @DeleteMapping("/heatSystem/del")
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response delHeatSystem(@RequestBody List<Integer> numList) {
        return dataSyncHandler.delHeatSystem(numList);
    }

    @ApiOperation(value = "添加点数据")
    @PostMapping("/heatPoint/add")
    public Response addHeatPoint(@RequestBody List<HeatPointInitDto> addList) {
        return dataSyncHandler.addHeatPoint(addList);
    }

    @ApiOperation(value = "编辑点数据")
    @PutMapping("/heatPoint/update")
    public Response updateHeatPoint(@RequestBody List<HeatPointInitDto> updateList) {
        return dataSyncHandler.updateHeatPoint(updateList);
    }

    @ApiOperation(value = "删除点数据")
    @DeleteMapping("/heatPoint/del")
    public Response delHeatPoint(@RequestBody List<Integer> numList) {
        return dataSyncHandler.delHeatPoint(numList);
    }

    @ApiOperation(value = "添加字典数据")
    @PostMapping("/heatDic/add")
    public Response addHeatDic(@RequestBody List<SyncHeatDicDto> dto) {
        return dataSyncHandler.addHeatDic(dto);
    }
}
