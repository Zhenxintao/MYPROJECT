package com.bmts.heating.bussiness.common.service.impl;

import com.bmts.heating.bussiness.common.constant.IssueConstants;
import com.bmts.heating.bussiness.common.converter.IssueConverter;
import com.bmts.heating.bussiness.common.service.LogRecordService;
import com.bmts.heating.bussiness.common.service.XPModeService;
import com.bmts.heating.commons.basement.model.cache.PointCache;
import com.bmts.heating.commons.basement.model.db.entity.HeatCabinet;
import com.bmts.heating.commons.basement.model.db.entity.HeatSystem;
import com.bmts.heating.commons.basement.model.db.entity.RecordDeviceUpDown;
import com.bmts.heating.commons.db.service.HeatCabinetService;
import com.bmts.heating.commons.db.service.HeatSystemService;
import com.bmts.heating.commons.db.service.RecordDeviceUpDownService;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.XPModeDto;
import com.bmts.heating.commons.entiy.gathersearch.response.issue.XPModeResponse;
import com.bmts.heating.commons.entiy.gathersearch.response.issue.read.ReadXPModeDto;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.middleware.cache.services.RedisCacheService;
import com.bmts.heating.middleware.monitor.service.GrpcIssueService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class XPModeServiceImpl implements XPModeService {

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private GrpcIssueService grpcIssueService;

    @Autowired
    private RecordDeviceUpDownService recordDeviceUpDownService;

    @Autowired
    private HeatSystemService heatSystemService;

    @Autowired
    private HeatCabinetService heatCabinetService;

    @Autowired
    private LogRecordService logRecordService;

    @Override
    public Response query(Integer systemId) {
        XPModeResponse xpModeResponse = new XPModeResponse();
        int level = TreeLevel.HeatSystem.level();
        Map<Integer, String[]> map = new HashMap<>();

        String[] arr = new String[14];
        arr[0] = IssueConstants.XPMode.XP_LONG_STATUS;
        arr[1] = IssueConstants.XPMode.XP_RUN_STATUS;
        arr[2] = IssueConstants.XPMode.XP_FAULT_STATUS;
        arr[3] = IssueConstants.XPMode.XP_SET_HZ;
        arr[4] = IssueConstants.XPMode.XP_REAL_HZ;
        arr[5] = IssueConstants.XPMode.XP_REAL_ELECTRIC;
        arr[6] = IssueConstants.XPMode.WORK_XP_Mode;
        arr[7] = IssueConstants.XPMode.WORK_XP_SET_HZ;
        arr[8] = IssueConstants.XPMode.WORK_XP_SET_P2D;
        arr[9] = IssueConstants.XPMode.WORK_XP_SET_P2G;
        arr[10] = IssueConstants.Snd.SND_P2D;
        arr[11] = IssueConstants.Snd.SND_P2G;
        arr[12] = IssueConstants.XPMode.WORK_XP1_START;
        arr[13] = IssueConstants.XPMode.WORK_XP1_STOP;

        map.put(systemId, arr);
        try {
            List<PointCache> pointCaches = redisCacheService.queryRealDataBySystems(map, level);
            if (CollectionUtils.isEmpty(pointCaches)) {
                return Response.warn("数据查询为空！");
            }
            int size = pointCaches.size();
            ReadXPModeDto dto = new ReadXPModeDto();
            pointCaches.forEach(e -> {
                        if (e != null && StringUtils.isNotBlank(e.getPointName())) {
                            if (Objects.equals(e.getPointName(), IssueConstants.XPMode.XP_LONG_STATUS) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setLongStatus(Integer.parseInt(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.XPMode.XP_RUN_STATUS) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setRunStatus(Integer.parseInt(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.XPMode.XP_FAULT_STATUS) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setFaultStatus(Integer.parseInt(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.XPMode.XP_SET_HZ) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setXpSV(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.XPMode.XP_REAL_HZ) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setXpHz(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.XPMode.XP_REAL_ELECTRIC) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setXpA(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.XPMode.WORK_XP_Mode) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setOptions(Integer.parseInt(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.XPMode.WORK_XP_SET_HZ) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setXpMSP(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.XPMode.WORK_XP_SET_P2D) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setXpPdSP(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.XPMode.WORK_XP_SET_P2G) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setXpPgSP(Double.parseDouble(e.getValue()));
                            }

                            if (Objects.equals(e.getPointName(), IssueConstants.Snd.SND_P2D) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setP2d(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.Snd.SND_P2G) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setP2g(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.XPMode.WORK_XP1_START) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setStart(Integer.parseInt(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.XPMode.WORK_XP1_STOP) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setStop(Integer.parseInt(e.getValue()));
                            }
                        }
                    }
            );

            xpModeResponse.setRead(dto);

        } catch (Exception e) {
            log.error("XPModeServiceImpl--RedisCache--{}", e.getMessage());
            e.printStackTrace();
        }
        return Response.success(xpModeResponse);
    }

    @Override
    public Response down(XPModeDto dto) {
        Response response = Response.fail("控制下发失败！");
        int level = TreeLevel.HeatSystem.level();
        try {
            if (dto.getOptions() != null) {
                // 二次循环变频工作方式 0:定值
                if (dto.getOptions() == 0) {
                    response = xpModeFixed(dto, level, IssueConstants.XPMode.WORK_XP_SET_HZ, dto.getXpMSP());
                    logRecordService.recordXPMode(dto, 1);
                    if (response.getCode() != 200) {
                        return response;
                    }

                }
                // 二次循环变频工作方式 1:恒定二次供回压差
                if (dto.getOptions() == 1) {
                    response = xpModeFixed(dto, level, IssueConstants.XPMode.WORK_XP_SET_P2D, dto.getXpPdSP());
                    logRecordService.recordXPMode(dto, 1);
                    if (response.getCode() != 200) {
                        return response;
                    }
                }
                // 二次循环变频工作方式 2:恒定二次供压
                if (dto.getOptions() == 2) {
                    response = xpModeFixed(dto, level, IssueConstants.XPMode.WORK_XP_SET_P2G, dto.getXpPgSP());
                    logRecordService.recordXPMode(dto, 1);
                    if (response.getCode() != 200) {
                        return response;
                    }
                }

            }
            // 二次循环变频泵的启停
            if (dto.getStart() != null && dto.getStop() != null) {
                if (dto.getStart() == 1 && dto.getStop() == 0) {
                    // 启动动作
                    response = xpModeStart(dto, level, IssueConstants.XPMode.WORK_XP1_START, IssueConstants.XPMode.WORK_XP1_STOP);
                    Response recordResponse = addRecord(dto, true, 1, response);
                    logRecordService.recordXPMode(dto, 3);
                    if (recordResponse != null) {
                        return recordResponse;
                    }

                    return response;
                } else if (dto.getStart() == 0 && dto.getStop() == 1) {
                    // 停止动作
                    response = xpModeStart(dto, level, IssueConstants.XPMode.WORK_XP1_START, IssueConstants.XPMode.WORK_XP1_STOP);
                    Response recordResponse = addRecord(dto, false, 1, response);
                    logRecordService.recordXPMode(dto, 3);
                    if (recordResponse != null) {
                        return recordResponse;
                    }

                    return response;
                } else {
                    return Response.fail("二次循环变频泵启停动作参数错误！");
                }
            }


        } catch (Exception e) {
            log.error("XPModeServiceImpl--down--RedisCache--{}", e.getMessage());
            e.printStackTrace();
        }
        return response;

    }

    private Response addRecord(XPModeDto dto, boolean operation, int type, Response response) {
        if (response.getCode() == 200) {
            // 添加设备启停记录
            RecordDeviceUpDown recordDeviceUpDown = new RecordDeviceUpDown();
            // 根据  systemId 查询热力站id
            Integer systemId = dto.getSystemId();
            HeatSystem systemById = heatSystemService.getById(systemId);
            if (systemById == null) {
                return Response.fail("系统数据查询为空！");
            }
            HeatCabinet cabinetById = heatCabinetService.getById(systemById.getHeatCabinetId());
            if (cabinetById == null) {
                return Response.fail("控制柜数据查询为空！");
            }
            recordDeviceUpDown.setHeatTransferStationId(cabinetById.getHeatTransferStationId());
            recordDeviceUpDown.setCreateTime(LocalDateTime.now());
            // `type` int(11) '1.循环泵 2.补水泵',
            //  `operation` bit(1)  'true 开启 false停止',
            recordDeviceUpDown.setOperation(operation);
            recordDeviceUpDown.setType(type);
            StringJoiner joiner = new StringJoiner("#");
            joiner.add(systemById.getName());
            joiner.add("二次循环变频泵");
            recordDeviceUpDown.setContent(joiner.toString());
            recordDeviceUpDownService.save(recordDeviceUpDown);
        }
        return null;
    }

    private Response xpModeStart(XPModeDto dto, int level, String workXp1Start, String workXp1Stop) throws Exception {
        Map<Integer, String[]> map = new HashMap<>();
        String[] arr = new String[2];
        arr[0] = workXp1Start;
        arr[1] = workXp1Stop;
        map.put(dto.getSystemId(), arr);
        List<PointCache> pointCaches = redisCacheService.queryRealDataBySystems(map, level);
        if (CollectionUtils.isEmpty(pointCaches)) {
            return Response.fail("数据查询为空，控制下发失败！");
        }
        if (pointCaches.size() < 2) {
            return Response.fail("服务参数配置错误！");
        }

        if (!Objects.equals(pointCaches.get(0).getPointName(), workXp1Start)) {
            return Response.fail("循环启动令参数配置错误！");
        }
        if (!Objects.equals(pointCaches.get(1).getPointName(), workXp1Stop)) {
            return Response.fail("循环停止令参数配置错误！");
        }
        pointCaches.get(0).setValue(String.valueOf(dto.getStart()));
        pointCaches.get(1).setValue(String.valueOf(dto.getStop()));

        // 后期要加上数据的有效性    要在正确的量程范围内
        List<PointL> pointLS = IssueConverter.INSTANCE.toList(pointCaches);
        return grpcIssueService.setGrpc(pointLS);
    }

    private Response xpModeFixed(XPModeDto dto, int level, String workXpSetHz, Double xpMSP) throws Exception {
        Map<Integer, String[]> map = new HashMap<>();
        String[] arr = new String[2];
        arr[0] = IssueConstants.XPMode.WORK_XP_Mode;
        arr[1] = workXpSetHz;

        map.put(dto.getSystemId(), arr);
        List<PointCache> pointCaches = redisCacheService.queryRealDataBySystems(map, level);
        if (CollectionUtils.isEmpty(pointCaches)) {
            return Response.fail("数据查询为空，控制下发失败！");
        }
        if (pointCaches.size() < 2) {
            return Response.fail("服务参数配置错误！");
        }

        if (!Objects.equals(pointCaches.get(0).getPointName(), IssueConstants.XPMode.WORK_XP_Mode) || dto.getOptions() == null) {
            return Response.fail("工作方式参数错误！");
        }
        if (!Objects.equals(pointCaches.get(1).getPointName(), workXpSetHz) || xpMSP == null) {
            return Response.fail("设置值参数错误！");
        }

        pointCaches.get(0).setValue(String.valueOf(dto.getOptions()));
        pointCaches.get(1).setValue(String.valueOf(xpMSP));

        // 后期要加上数据的有效性    要在正确的量程范围内
        List<PointL> pointLS = IssueConverter.INSTANCE.toList(pointCaches);
        return grpcIssueService.setGrpc(pointLS);
    }
}
