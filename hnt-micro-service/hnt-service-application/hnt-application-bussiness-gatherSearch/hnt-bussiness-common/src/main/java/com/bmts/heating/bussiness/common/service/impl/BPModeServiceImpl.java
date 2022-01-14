package com.bmts.heating.bussiness.common.service.impl;

import com.bmts.heating.bussiness.common.constant.IssueConstants;
import com.bmts.heating.bussiness.common.converter.IssueConverter;
import com.bmts.heating.bussiness.common.service.BPModeService;
import com.bmts.heating.bussiness.common.service.LogRecordService;
import com.bmts.heating.commons.basement.model.cache.PointCache;
import com.bmts.heating.commons.basement.model.db.entity.HeatCabinet;
import com.bmts.heating.commons.basement.model.db.entity.HeatSystem;
import com.bmts.heating.commons.basement.model.db.entity.RecordDeviceUpDown;
import com.bmts.heating.commons.db.service.HeatCabinetService;
import com.bmts.heating.commons.db.service.HeatSystemService;
import com.bmts.heating.commons.db.service.RecordDeviceUpDownService;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.BPModeDto;
import com.bmts.heating.commons.entiy.gathersearch.response.issue.BPModeResponse;
import com.bmts.heating.commons.entiy.gathersearch.response.issue.read.ReadBPModeDto;
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
public class BPModeServiceImpl implements BPModeService {

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
        BPModeResponse bpModeResponse = new BPModeResponse();
        int level = TreeLevel.HeatSystem.level();
        Map<Integer, String[]> map = new HashMap<>();

        String[] arr = new String[14];
        arr[0] = IssueConstants.BPMode.BP_LONG_STATUS;
        arr[1] = IssueConstants.BPMode.BP_RUN_STATUS;
        arr[2] = IssueConstants.BPMode.BP_FAULT_STATUS;
        arr[3] = IssueConstants.BPMode.BP_SET_HZ;
        arr[4] = IssueConstants.BPMode.BP_REAL_HZ;
        arr[5] = IssueConstants.BPMode.BP_REAL_ELECTRIC;
        arr[6] = IssueConstants.BPMode.WORK_BP_Mode;
        arr[7] = IssueConstants.BPMode.WORK_BP_SET_HZ;
        arr[8] = IssueConstants.BPMode.WORK_BP_SET_P2H;
        arr[9] = IssueConstants.BPMode.WORK_BP_SET_P2H_H;
        arr[10] = IssueConstants.BPMode.WORK_BP_SET_P2H_L;
        arr[11] = IssueConstants.Snd.SND_P2H;
        arr[12] = IssueConstants.BPMode.WORK_BP1_START;
        arr[13] = IssueConstants.BPMode.WORK_BP1_STOP;

        map.put(systemId, arr);
        try {
            List<PointCache> pointCaches = redisCacheService.queryRealDataBySystems(map, level);
            if (CollectionUtils.isEmpty(pointCaches)) {
                return Response.warn("数据查询为空！");
            }
            int size = pointCaches.size();
            ReadBPModeDto dto = new ReadBPModeDto();
            pointCaches.forEach(e -> {
                        if (e != null && StringUtils.isNotBlank(e.getPointName())) {

                            if (Objects.equals(e.getPointName(), IssueConstants.BPMode.BP_LONG_STATUS) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setLongStatus(Integer.parseInt(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.BPMode.BP_RUN_STATUS) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setRunStatus(Integer.parseInt(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.BPMode.BP_FAULT_STATUS) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setFaultStatus(Integer.parseInt(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.BPMode.BP_SET_HZ) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setBpSV(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.BPMode.BP_REAL_HZ) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setBpHz(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.BPMode.BP_REAL_ELECTRIC) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setBpA(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.BPMode.WORK_BP_Mode) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setOptions(Integer.parseInt(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.BPMode.WORK_BP_SET_HZ) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setBpMsP(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.BPMode.WORK_BP_SET_P2H) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setBpPhSP(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.BPMode.WORK_BP_SET_P2H_H) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setBpP2hH(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.BPMode.WORK_BP_SET_P2H_L) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setBpP2hL(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.Snd.SND_P2H) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setP2h(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.BPMode.WORK_BP1_START) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setStart(Integer.parseInt(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.BPMode.WORK_BP1_STOP) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setStop(Integer.parseInt(e.getValue()));
                            }

                        }
                    }

            );

            bpModeResponse.setRead(dto);
        } catch (Exception e) {
            log.error("BPModeServiceImpl--RedisCache--{}", e.getMessage());
            e.printStackTrace();
        }
        return Response.success(bpModeResponse);
    }

    @Override
    public Response down(BPModeDto dto) {
        Response response = Response.fail("控制下发失败！");
        int level = TreeLevel.HeatSystem.level();
        try {
            if (dto.getOptions() != null) {
                // 二次补水变频工作方式 0:定值
                if (dto.getOptions() == 0) {
                    response = bpModeFixed(dto, level, IssueConstants.BPMode.WORK_BP_SET_HZ, dto.getBpMsP());
                    logRecordService.recordBPMode(dto, 0);
                    if (response.getCode() != 200) {
                        return response;
                    }


                }
                // 二次补水变频工作方式 1:恒定二次回压
                if (dto.getOptions() == 1) {
                    response = bpModeFixed(dto, level, IssueConstants.BPMode.WORK_BP_SET_P2H, dto.getBpPhSP());
                    logRecordService.recordBPMode(dto, 1);
                    if (response.getCode() != 200) {
                        return response;
                    }

                }
                // 二次补水变频工作方式 2:高低限自动（电节点压力表方式）
                if (dto.getOptions() == 2) {
                    response = bpModeAuto(dto, level);
                    logRecordService.recordBPMode(dto, 2);
                    if (response.getCode() != 200) {
                        return response;
                    }

                }
            }

            // 二次补水变频泵的启停
            if (dto.getStart() != null && dto.getStop() != null) {
                if (dto.getStart() == 1 && dto.getStop() == 0) {
                    // 启动动作
                    response = bpModeStart(dto, level, IssueConstants.BPMode.WORK_BP1_START, IssueConstants.BPMode.WORK_BP1_STOP);
                    Response recordResponse = addRecord(dto, true, 2, response);
                    logRecordService.recordBPMode(dto, 3);
                    if (recordResponse != null) {
                        return recordResponse;
                    }
                    return response;
                } else if (dto.getStart() == 0 && dto.getStop() == 1) {
                    // 停止动作
                    response = bpModeStart(dto, level, IssueConstants.BPMode.WORK_BP1_START, IssueConstants.BPMode.WORK_BP1_STOP);
                    Response recordResponse = addRecord(dto, false, 2, response);
                    logRecordService.recordBPMode(dto, 3);
                    if (recordResponse != null) {
                        return recordResponse;
                    }
                    return response;
                } else {
                    return Response.fail("二次补水变频泵启停动作参数错误！");
                }

            }

        } catch (Exception e) {
            log.error("BPModeServiceImpl--down--RedisCache--{}", e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    private Response addRecord(BPModeDto dto, boolean operation, int type, Response response) {
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
            joiner.add("二次补水变频泵");
            recordDeviceUpDown.setContent(joiner.toString());
            recordDeviceUpDownService.save(recordDeviceUpDown);
        }
        return null;
    }

    private Response bpModeStart(BPModeDto dto, int level, String workBp1Start, String workBp1Stop) throws Exception {
        Map<Integer, String[]> map = new HashMap<>();
        String[] arr = new String[2];
        arr[0] = workBp1Start;
        arr[1] = workBp1Stop;
        map.put(dto.getSystemId(), arr);
        List<PointCache> pointCaches = redisCacheService.queryRealDataBySystems(map, level);
        if (CollectionUtils.isEmpty(pointCaches)) {
            return Response.fail("数据查询为空，控制下发失败！");
        }
        if (pointCaches.size() < 2) {
            return Response.fail("服务参数配置错误！");
        }
        if (!Objects.equals(pointCaches.get(0).getPointName(), workBp1Start)) {
            return Response.fail("补水启动令参数配置错误！");
        }
        if (!Objects.equals(pointCaches.get(1).getPointName(), workBp1Stop)) {
            return Response.fail("补水停止令参数配置错误！");
        }

        pointCaches.get(0).setValue(String.valueOf(dto.getStart()));
        pointCaches.get(1).setValue(String.valueOf(dto.getStop()));

        // 后期要加上数据的有效性    要在正确的量程范围内
        List<PointL> pointLS = IssueConverter.INSTANCE.toList(pointCaches);
        return grpcIssueService.setGrpc(pointLS);
    }

    private Response bpModeAuto(BPModeDto dto, int level) throws Exception {
        Map<Integer, String[]> map = new HashMap<>();
        String[] arr = new String[3];
        arr[0] = IssueConstants.BPMode.WORK_BP_Mode;
        arr[1] = IssueConstants.BPMode.WORK_BP_SET_P2H_H;
        arr[2] = IssueConstants.BPMode.WORK_BP_SET_P2H_L;
        map.put(dto.getSystemId(), arr);
        List<PointCache> pointCaches = redisCacheService.queryRealDataBySystems(map, level);
        if (CollectionUtils.isEmpty(pointCaches)) {
            return Response.fail("数据查询为空，控制下发失败！");
        }
        if (pointCaches.size() < 3) {
            return Response.fail("服务参数配置错误！");
        }
        if (!Objects.equals(pointCaches.get(0).getPointName(), IssueConstants.BPMode.WORK_BP_Mode) || dto.getOptions() == null) {
            return Response.fail("工作方式参数错误！");
        }
        if (!Objects.equals(pointCaches.get(1).getPointName(), IssueConstants.BPMode.WORK_BP_SET_P2H_H) || dto.getBpP2hH() == null) {
            return Response.fail("自动高限设定值参数错误！");
        }
        if (!Objects.equals(pointCaches.get(2).getPointName(), IssueConstants.BPMode.WORK_BP_SET_P2H_L) || dto.getBpP2hL() == null) {
            return Response.fail("自动低限设定值参数错误！");
        }
        pointCaches.get(0).setValue(String.valueOf(dto.getOptions()));
        pointCaches.get(1).setValue(String.valueOf(dto.getBpP2hH()));
        pointCaches.get(2).setValue(String.valueOf(dto.getBpP2hL()));

        // 后期要加上数据的有效性    要在正确的量程范围内
        List<PointL> pointLS = IssueConverter.INSTANCE.toList(pointCaches);
        return grpcIssueService.setGrpc(pointLS);
    }

    private Response bpModeFixed(BPModeDto dto, int level, String workBpSetHz, Double bpMsP) throws Exception {
        Map<Integer, String[]> map = new HashMap<>();
        String[] arr = new String[2];
        arr[0] = IssueConstants.BPMode.WORK_BP_Mode;
        arr[1] = workBpSetHz;
        map.put(dto.getSystemId(), arr);
        List<PointCache> pointCaches = redisCacheService.queryRealDataBySystems(map, level);
        if (CollectionUtils.isEmpty(pointCaches)) {
            return Response.fail("数据查询为空，控制下发失败！");
        }
        if (pointCaches.size() < 2) {
            return Response.fail("服务参数配置错误！");
        }
        if (!Objects.equals(pointCaches.get(0).getPointName(), IssueConstants.BPMode.WORK_BP_Mode) || dto.getOptions() == null) {
            return Response.fail("工作方式参数错误！");
        }
        if (!Objects.equals(pointCaches.get(1).getPointName(), workBpSetHz) || bpMsP == null) {
            return Response.fail("设定值参数错误！");
        }

        pointCaches.get(0).setValue(String.valueOf(dto.getOptions()));
        pointCaches.get(1).setValue(String.valueOf(bpMsP));

        // 后期要加上数据的有效性    要在正确的量程范围内
        List<PointL> pointLS = IssueConverter.INSTANCE.toList(pointCaches);
        return grpcIssueService.setGrpc(pointLS);
    }

}
