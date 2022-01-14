package com.bmts.heating.bussiness.common.service.impl;

import com.bmts.heating.bussiness.common.constant.IssueConstants;
import com.bmts.heating.bussiness.common.converter.IssueConverter;
import com.bmts.heating.bussiness.common.service.LogRecordService;
import com.bmts.heating.bussiness.common.service.XSVModeService;
import com.bmts.heating.commons.basement.model.cache.PointCache;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.XSVModeDto;
import com.bmts.heating.commons.entiy.gathersearch.response.issue.XSVModeResponse;
import com.bmts.heating.commons.entiy.gathersearch.response.issue.read.ReadXSVModeDto;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.middleware.cache.services.RedisCacheService;
import com.bmts.heating.middleware.monitor.service.GrpcIssueService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class XSVModeServiceImpl implements XSVModeService {

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private GrpcIssueService grpcIssueService;

    @Autowired
    private LogRecordService logRecordService;

    @Override
    public Response query(Integer systemId) {
        XSVModeResponse xsvModeResponse = new XSVModeResponse();
        int level = TreeLevel.HeatSystem.level();
        Map<Integer, String[]> map = new HashMap<>();

        String[] arr = new String[6];
        arr[0] = IssueConstants.XSVMode.XSV_RUN_STATUS;
        arr[1] = IssueConstants.Snd.SND_P2H;
        arr[2] = IssueConstants.XSVMode.WORK_XSV_Mode;
        arr[3] = IssueConstants.XSVMode.WORK_XSV_SET_HAND;
        arr[4] = IssueConstants.XSVMode.WORK_XSV_SET_P2H_H;
        arr[5] = IssueConstants.XSVMode.WORK_XSV_SET_P2H_L;

        map.put(systemId, arr);
        try {
            List<PointCache> pointCaches = redisCacheService.queryRealDataBySystems(map, level);
            if (CollectionUtils.isEmpty(pointCaches)) {
                return Response.warn("数据查询为空！");
            }
            int size = pointCaches.size();
            ReadXSVModeDto dto = new ReadXSVModeDto();

            pointCaches.forEach(e -> {
                        if (e != null && StringUtils.isNotBlank(e.getPointName())) {
                            if (Objects.equals(e.getPointName(), IssueConstants.XSVMode.XSV_RUN_STATUS) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setStatus(Integer.parseInt(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.Snd.SND_P2H) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setP2h(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.XSVMode.WORK_XSV_Mode) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setOptions(Integer.parseInt(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.XSVMode.WORK_XSV_SET_HAND) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setHandStatus(Integer.parseInt(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.XSVMode.WORK_XSV_SET_P2H_H) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setXsvP2hH(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.XSVMode.WORK_XSV_SET_P2H_L) && StringUtils.isNotBlank(e.getValue())) {
                                dto.setXsvP2hL(Double.parseDouble(e.getValue()));
                            }

                        }

                    }

            );

            xsvModeResponse.setRead(dto);

        } catch (Exception e) {
            log.error("XSVModeServiceImpl--RedisCache--{}", e.getMessage());
            e.printStackTrace();
        }
        return Response.success(xsvModeResponse);
    }

    @Override
    public Response down(XSVModeDto dto) {
        int level = TreeLevel.HeatSystem.level();
        try {
            // 泄压电磁阀工作方式 0:手动
            if (dto.getOptions() == 0) {
                Response response = xsvModeHand(dto, level);
                logRecordService.recordXSVMode(dto);
                return response;
            }
            // 泄压电磁阀工作方式 1:自动
            if (dto.getOptions() == 1) {
                Response response = xsvModeAuto(dto, level);
                logRecordService.recordXSVMode(dto);
                return response;
            }

        } catch (Exception e) {
            log.error("XSVModeServiceImpl--down--RedisCache--{}", e.getMessage());
            e.printStackTrace();
        }
        return Response.fail("控制下发失败！");
    }

    private Response xsvModeAuto(XSVModeDto dto, int level) throws Exception {
        Map<Integer, String[]> map = new HashMap<>();
        String[] arr = new String[3];
        arr[0] = IssueConstants.XSVMode.WORK_XSV_Mode;
        arr[1] = IssueConstants.XSVMode.WORK_XSV_SET_P2H_H;
        arr[2] = IssueConstants.XSVMode.WORK_XSV_SET_P2H_L;

        map.put(dto.getSystemId(), arr);
        List<PointCache> pointCaches = redisCacheService.queryRealDataBySystems(map, level);
        if (CollectionUtils.isEmpty(pointCaches)) {
            return Response.fail("数据查询为空，控制下发失败！");
        }
        if (pointCaches.size() < 3) {
            return Response.fail("服务参数配置错误！");
        }

        if (!Objects.equals(pointCaches.get(0).getPointName(), IssueConstants.XSVMode.WORK_XSV_Mode) || dto.getOptions() == null) {
            return Response.fail("工作方式参数错误！");
        }
        if (!Objects.equals(pointCaches.get(1).getPointName(), IssueConstants.XSVMode.WORK_XSV_SET_P2H_H) || dto.getXsvP2hH() == null) {
            return Response.fail("高限设定值参数错误！");
        }
        if (!Objects.equals(pointCaches.get(2).getPointName(), IssueConstants.XSVMode.WORK_XSV_SET_P2H_L) || dto.getXsvP2hL() == null) {
            return Response.fail("低限设定值参数错误！");
        }
        pointCaches.get(0).setValue(String.valueOf(dto.getOptions()));
        pointCaches.get(1).setValue(String.valueOf(dto.getXsvP2hH()));
        pointCaches.get(2).setValue(String.valueOf(dto.getXsvP2hL()));

        // 后期要加上数据的有效性    要在正确的量程范围内
        List<PointL> pointLS = IssueConverter.INSTANCE.toList(pointCaches);
        return grpcIssueService.setGrpc(pointLS);
    }

    private Response xsvModeHand(XSVModeDto dto, int level) throws Exception {
        Map<Integer, String[]> map = new HashMap<>();
        String[] arr = new String[2];
        arr[0] = IssueConstants.XSVMode.WORK_XSV_Mode;
        arr[1] = IssueConstants.XSVMode.WORK_XSV_SET_HAND;

        map.put(dto.getSystemId(), arr);
        List<PointCache> pointCaches = redisCacheService.queryRealDataBySystems(map, level);
        if (CollectionUtils.isEmpty(pointCaches)) {
            return Response.fail("数据查询为空，控制下发失败！");
        }
        if (pointCaches.size() < 2) {
            return Response.fail("服务参数配置错误！");
        }

        if (!Objects.equals(pointCaches.get(0).getPointName(), IssueConstants.XSVMode.WORK_XSV_Mode) || dto.getOptions() == null) {
            return Response.fail("工作方式参数错误！");
        }
        if (!Objects.equals(pointCaches.get(1).getPointName(), IssueConstants.XSVMode.WORK_XSV_SET_HAND) || dto.getXsvSS() == null) {
            return Response.fail("手动控制参数错误！");
        }

        pointCaches.get(0).setValue(String.valueOf(dto.getOptions()));
        pointCaches.get(1).setValue(String.valueOf(dto.getXsvSS()));

        // 后期要加上数据的有效性    要在正确的量程范围内
        List<PointL> pointLS = IssueConverter.INSTANCE.toList(pointCaches);
        return grpcIssueService.setGrpc(pointLS);
    }
}
