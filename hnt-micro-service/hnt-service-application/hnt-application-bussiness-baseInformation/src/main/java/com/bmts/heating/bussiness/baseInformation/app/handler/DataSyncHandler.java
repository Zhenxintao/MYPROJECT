package com.bmts.heating.bussiness.baseInformation.app.handler;

import com.bmts.heating.bussiness.baseInformation.app.service.*;
import com.bmts.heating.commons.entiy.baseInfo.sync.InitDto;
import com.bmts.heating.commons.entiy.baseInfo.sync.add.AddHeatOrganizationInitDto;
import com.bmts.heating.commons.entiy.baseInfo.sync.add.SyncHeatDicDto;
import com.bmts.heating.commons.entiy.baseInfo.sync.update.*;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.commons.utils.restful.ResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: DataSyncHandler
 * @Description: 数据同步处理
 * @Author: pxf
 * @Date: 2021/6/3 10:58
 * @Version: 1.0
 */
@Service
public class DataSyncHandler {

    @Autowired
    private SyncOrganizationService syncOrganizationService;

    @Autowired
    private SyncHeatNetService syncHeatNetService;

    @Autowired
    private SyncHeatSourceService syncHeatSourceService;

    @Autowired
    private SyncHeatStationService syncHeatStationService;

    @Autowired
    private SyncHeatCabinetService syncHeatCabinetService;

    @Autowired
    private SyncHeatSystemService syncHeatSystemService;

    @Autowired
    private SyncPointConfigService syncPointConfigService;

    @Autowired
    private SyncHeatDicService syncHeatDicService;

    /**
     * @Description 初始化数据同步
     */
    public Response initSync(InitDto dtos) {
        StringBuffer buffer = new StringBuffer();
        //if (!CollectionUtils.isEmpty(dtos.getHeatNetInitDtos())) {
        // 热网数据同步
        Response responseHeatNet = addHeatNet(dtos.getHeatNetInitDtos());
        if (responseHeatNet.getCode() == ResponseCode.FAIL.getCode()) {
            if (StringUtils.isNotBlank(responseHeatNet.getMsg())) {
                buffer.append(responseHeatNet.getMsg());
            }
        }
        // 热源数据同步
        Response responseHeatSource = addHeatSource(dtos.getHeatSourceInitDtos());
        if (responseHeatSource.getCode() == ResponseCode.FAIL.getCode()) {
            if (StringUtils.isNotBlank(responseHeatSource.getMsg())) {
                buffer.append(responseHeatSource.getMsg());
            }
        }
        // 热力站数据同步
        Response responseHeatStation = addHeatStation(dtos.getHeatStationInitDtos());
        if (responseHeatStation.getCode() == ResponseCode.FAIL.getCode()) {
            if (StringUtils.isNotBlank(responseHeatStation.getMsg())) {
                buffer.append(responseHeatStation.getMsg());
            }
        }
        // 控制柜数据同步
        Response responseHeatCabinet = addHeatCabinet(dtos.getHeatCabinetInitDtos());
        if (responseHeatCabinet.getCode() == ResponseCode.FAIL.getCode()) {
            if (StringUtils.isNotBlank(responseHeatCabinet.getMsg())) {
                buffer.append(responseHeatCabinet.getMsg());
            }
        }
        // 系统数据同步
        Response responseHeatSystem = addHeatSystem(dtos.getHeatSystemInitDtos());
        if (responseHeatSystem.getCode() == ResponseCode.FAIL.getCode()) {
            if (StringUtils.isNotBlank(responseHeatSystem.getMsg())) {
                buffer.append(responseHeatSystem.getMsg());
            }
        }
        // 点数据同步
        Response responseHeatPoint = addHeatPoint(dtos.getHeatPointInitDtos());
        if (responseHeatPoint.getCode() == ResponseCode.FAIL.getCode()) {
            if (StringUtils.isNotBlank(responseHeatPoint.getMsg())) {
                buffer.append(responseHeatPoint.getMsg());
            }
        }

        if (StringUtils.isNotBlank(buffer.toString())) {
            return Response.fail(buffer.toString());
        }
        //}
        return Response.success();
    }


    /**
     * @Description 同步组织机构数据
     */
    public Response addHeatOrganization(AddHeatOrganizationInitDto heatOrganDto) {
        return syncOrganizationService.addHeatOrganization(heatOrganDto);
    }

    /**
     * @Description 编辑组织机构数据
     */
    public Response updateOrganization(List<HeatOrganizationInitDto> heatOrganList) {
        return syncOrganizationService.updateOrganization(heatOrganList);
    }

    /**
     * @Description 删除组织机构数据
     */
    public Response delOrganization(List<Integer> numList) {
        return syncOrganizationService.delOrganization(numList);
    }


    /**
     * @Description 添加热网数据
     */
    public Response addHeatNet(List<HeatNetInitDto> addList) {
        return syncHeatNetService.addHeatNet(addList);
    }

    /**
     * @Description 编辑热网数据
     */
    public Response updateHeatNet(List<HeatNetInitDto> updateList) {
        return syncHeatNetService.updateHeatNet(updateList);
    }

    /**
     * @Description 删除热网数据
     */
    public Response delHeatNet(List<Integer> numList) {
        return syncHeatNetService.delHeatNet(numList);
    }

    /**
     * @Description 添加热源数据
     */
    public Response addHeatSource(List<HeatSourceInitDto> addList) {
        return syncHeatSourceService.addHeatSource(addList);
    }

    /**
     * @Description 编辑热源数据
     */
    public Response updateHeatSource(List<HeatSourceInitDto> updateList) {
        return syncHeatSourceService.updateHeatSource(updateList);
    }

    /**
     * @Description 删除热源数据
     */
    public Response delHeatSource(List<Integer> numList) {
        return syncHeatSourceService.delHeatSource(numList);
    }


    /**
     * @Description 添加热力站数据
     */
    public Response addHeatStation(List<HeatStationInitDto> addList) {
        return syncHeatStationService.addHeatStation(addList);
    }

    /**
     * @Description 编辑热力站数据
     */
    public Response updateHeatStation(List<HeatStationInitDto> updateList) {
        return syncHeatStationService.updateHeatStation(updateList);
    }

    /**
     * @Description 删除热力站数据
     */
    public Response delHeatStation(List<Integer> numList) {
        return syncHeatStationService.delHeatStation(numList);
    }

    /**
     * @Description 添加控制柜数据
     */
    public Response addHeatCabinet(List<HeatCabinetInitDto> addList) {
        return syncHeatCabinetService.addHeatCabinet(addList);
    }

    /**
     * @Description 编辑控制柜数据
     */
    public Response updateHeatCabinet(List<HeatCabinetInitDto> updateList) {
        return syncHeatCabinetService.updateHeatCabinet(updateList);
    }

    /**
     * @Description 删除控制柜数据
     */
    public Response delHeatCabinet(List<Integer> numList) {
        return syncHeatCabinetService.delHeatCabinet(numList);
    }

    /**
     * @Description 添加系统数据
     */
    public Response addHeatSystem(List<HeatSystemInitDto> addList) {
        return syncHeatSystemService.addHeatSystem(addList);
    }

    /**
     * @Description 编辑系统数据
     */
    public Response updateHeatSystem(List<HeatSystemInitDto> updateList) {
        return syncHeatSystemService.updateHeatSystem(updateList);
    }

    /**
     * @Description 删除系统数据
     */
    public Response delHeatSystem(List<Integer> numList) {
        return syncHeatSystemService.delHeatSystem(numList);
    }

    /**
     * @Description 添加点数据
     */
    public Response addHeatPoint(List<HeatPointInitDto> addList) {
        return syncPointConfigService.addHeatPoint(addList);
    }

    /**
     * @Description 编辑点数据
     */
    public Response updateHeatPoint(List<HeatPointInitDto> updateList) {
        return syncPointConfigService.updateHeatPoint(updateList);
    }

    /**
     * @Description 删除点数据
     */
    public Response delHeatPoint(List<Integer> numList) {
        return syncPointConfigService.delHeatPoint(numList);
    }

    /**
     * @Description 添加字典数据
     */
    public Response addHeatDic(List<SyncHeatDicDto> dto) {
        return syncHeatDicService.saveHeatDic(dto);
    }
}
