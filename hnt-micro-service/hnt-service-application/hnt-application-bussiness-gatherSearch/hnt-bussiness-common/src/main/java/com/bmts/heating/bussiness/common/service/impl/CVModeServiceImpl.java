package com.bmts.heating.bussiness.common.service.impl;

import com.bmts.heating.bussiness.common.constant.IssueConstants;
import com.bmts.heating.bussiness.common.converter.IssueConverter;
import com.bmts.heating.bussiness.common.service.CVModeService;
import com.bmts.heating.bussiness.common.service.LogRecordService;
import com.bmts.heating.commons.basement.model.cache.PointCache;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.CVModeDto;
import com.bmts.heating.commons.entiy.gathersearch.response.issue.CVModeResponse;
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
public class CVModeServiceImpl implements CVModeService {

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private GrpcIssueService grpcIssueService;

    @Autowired
    private LogRecordService logRecordService;

    @Override
    public Response query(Integer systemId) {
        CVModeResponse cvModeResponse = new CVModeResponse();
        int level = TreeLevel.HeatSystem.level();
        Map<Integer, String[]> map = new HashMap<>();

        String[] arr = new String[22];
        arr[0] = IssueConstants.CVMode.CV_SET_OPEN;
        arr[1] = IssueConstants.CVMode.CV_REAL_OPEN;
        arr[2] = IssueConstants.CVMode.WORK_CV_Mode;
        arr[3] = IssueConstants.CVMode.WORK_CV_SET_OPEN;
        arr[4] = IssueConstants.CVMode.WORK_CV_SET_T2GSP;
        arr[5] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_0;
        arr[6] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_1;
        arr[7] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_2;
        arr[8] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_3;
        arr[9] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_4;
        arr[10] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_5;
        arr[11] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_6;
        arr[12] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_7;
        arr[13] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_8;
        arr[14] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_9;
        arr[15] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_10;
        arr[16] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_11;
        arr[17] = IssueConstants.CVMode.WORK_CV_SET_FLOW;
        arr[18] = IssueConstants.CVMode.WORK_CV_SET_STEP;
        arr[19] = IssueConstants.CVMode.WORK_CV_SET_MIN;
        arr[20] = IssueConstants.CVMode.WORK_CV_SET_MAX;
        arr[21] = IssueConstants.Snd.SND_T2G;

        map.put(systemId, arr);
        try {
            List<PointCache> pointCaches = redisCacheService.queryRealDataBySystems(map, level);
            if (CollectionUtils.isEmpty(pointCaches)) {
                return Response.warn("数据查询为空！");
            }
            int size = pointCaches.size();
            CVModeResponse.CVTg cvTg = new CVModeResponse.CVTg();
            pointCaches.forEach(e -> {
                        if (e != null && StringUtils.isNotBlank(e.getPointName())) {

                            if (Objects.equals(e.getPointName(), IssueConstants.CVMode.CV_SET_OPEN) && StringUtils.isNotBlank(e.getValue())) {
                                cvModeResponse.setCvSV(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.CVMode.CV_REAL_OPEN) && StringUtils.isNotBlank(e.getValue())) {
                                cvModeResponse.setCvU(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.CVMode.WORK_CV_Mode) && StringUtils.isNotBlank(e.getValue())) {
                                cvModeResponse.setOptions(Integer.parseInt(e.getValue()));
                            }

                            if (Objects.equals(e.getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP) && StringUtils.isNotBlank(e.getValue())) {
                                cvTg.setCvTgSP(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_0) && StringUtils.isNotBlank(e.getValue())) {
                                cvTg.setTgSPTime0(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_1) && StringUtils.isNotBlank(e.getValue())) {
                                cvTg.setTgSPTime1(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_2) && StringUtils.isNotBlank(e.getValue())) {
                                cvTg.setTgSPTime2(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_3) && StringUtils.isNotBlank(e.getValue())) {
                                cvTg.setTgSPTime3(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_4) && StringUtils.isNotBlank(e.getValue())) {
                                cvTg.setTgSPTime4(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_5) && StringUtils.isNotBlank(e.getValue())) {
                                cvTg.setTgSPTime5(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_6) && StringUtils.isNotBlank(e.getValue())) {
                                cvTg.setTgSPTime6(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_7) && StringUtils.isNotBlank(e.getValue())) {
                                cvTg.setTgSPTime7(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_8) && StringUtils.isNotBlank(e.getValue())) {
                                cvTg.setTgSPTime8(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_9) && StringUtils.isNotBlank(e.getValue())) {
                                cvTg.setTgSPTime9(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_10) && StringUtils.isNotBlank(e.getValue())) {
                                cvTg.setTgSPTime10(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_11) && StringUtils.isNotBlank(e.getValue())) {
                                cvTg.setTgSPTime11(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.Snd.SND_T2G) && StringUtils.isNotBlank(e.getValue())) {
                                cvTg.setT2g(Double.parseDouble(e.getValue()));
                            }


                            if (Objects.equals(e.getPointName(), IssueConstants.CVMode.WORK_CV_SET_FLOW) && StringUtils.isNotBlank(e.getValue())) {
                                cvModeResponse.setCvFSP(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.CVMode.WORK_CV_SET_STEP) && StringUtils.isNotBlank(e.getValue())) {
                                cvModeResponse.setCvStep(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.CVMode.WORK_CV_SET_MIN) && StringUtils.isNotBlank(e.getValue())) {
                                cvModeResponse.setCvMin(Double.parseDouble(e.getValue()));
                            }
                            if (Objects.equals(e.getPointName(), IssueConstants.CVMode.WORK_CV_SET_MAX) && StringUtils.isNotBlank(e.getValue())) {
                                cvModeResponse.setCvMax(Double.parseDouble(e.getValue()));
                            }


                        }

                    }

            );

            cvModeResponse.setCvTg(cvTg);

        } catch (Exception e) {
            log.error("CVModeServiceImpl--RedisCache--{}", e.getMessage());
            e.printStackTrace();
        }
        return Response.success(cvModeResponse);
    }

    @Override
    public Response down(CVModeDto dto) {
        int level = TreeLevel.HeatSystem.level();
        try {
            // 一次调节阀工作方式 0:定值
            if (dto.getOptions() == 0) {
                Response response = cvModeFixed(dto, level);
                logRecordService.recordCVMode(dto);
                return response;
            }
            // 一次调节阀工作方式 1:二次供温恒定
            if (dto.getOptions() == 1) {
                Response response = cvModeTg(dto, level);
                logRecordService.recordCVMode(dto);
                return response;
            }
            // 一次调节阀工作方式 4:恒流
            if (dto.getOptions() == 4) {
                Response response = cvModeFsp(dto, level);
                logRecordService.recordCVMode(dto);
                return response;
            }

        } catch (Exception e) {
            log.error("CVModeServiceImpl--down--RedisCache--{}", e.getMessage());
            e.printStackTrace();
        }
        return Response.fail("控制下发失败！");
    }

    private Response cvModeFsp(CVModeDto dto, int level) throws Exception {
        Map<Integer, String[]> map = new HashMap<>();
        String[] arr = new String[5];
        arr[0] = IssueConstants.CVMode.WORK_CV_Mode;
        arr[1] = IssueConstants.CVMode.WORK_CV_SET_FLOW;
        arr[2] = IssueConstants.CVMode.WORK_CV_SET_STEP;
        arr[3] = IssueConstants.CVMode.WORK_CV_SET_MIN;
        arr[4] = IssueConstants.CVMode.WORK_CV_SET_MAX;
        map.put(dto.getSystemId(), arr);
        List<PointCache> pointCaches = redisCacheService.queryRealDataBySystems(map, level);
        if (CollectionUtils.isEmpty(pointCaches)) {
            return Response.fail("数据查询为空，控制下发失败！");
        }
        if (pointCaches.size() < 5) {
            return Response.fail("服务参数配置错误！");
        }
        if (!Objects.equals(pointCaches.get(0).getPointName(), IssueConstants.CVMode.WORK_CV_Mode) || dto.getOptions() == null) {
            return Response.fail("工作方式参数错误！");
        }
        if (!Objects.equals(pointCaches.get(1).getPointName(), IssueConstants.CVMode.WORK_CV_SET_FLOW) || dto.getCvFs().getCvFSP() == null) {
            return Response.fail("恒流设定值参数错误！");
        }
        if (!Objects.equals(pointCaches.get(2).getPointName(), IssueConstants.CVMode.WORK_CV_SET_STEP)) {
            return Response.fail("步进值参数配置错误！");
        }
        if (!Objects.equals(pointCaches.get(3).getPointName(), IssueConstants.CVMode.WORK_CV_SET_MIN)) {
            return Response.fail("一次调节阀最小开度参数配置错误！");
        }
        if (!Objects.equals(pointCaches.get(4).getPointName(), IssueConstants.CVMode.WORK_CV_SET_MAX)) {
            return Response.fail("一次调节阀最大开度参数配置错误！");
        }

        pointCaches.get(0).setValue(String.valueOf(dto.getOptions()));
        pointCaches.get(1).setValue(String.valueOf(dto.getCvFs().getCvFSP()));
        if (dto.getCvFs().getCvStep() != null) {
            pointCaches.get(2).setValue(String.valueOf(dto.getCvFs().getCvStep()));
        }
        if (dto.getCvFs().getCvMin() != null) {
            pointCaches.get(3).setValue(String.valueOf(dto.getCvFs().getCvMin()));
        }
        if (dto.getCvFs().getCvMax() != null) {
            pointCaches.get(4).setValue(String.valueOf(dto.getCvFs().getCvMax()));
        }

        // 后期要加上数据的有效性    要在正确的量程范围内
        List<PointL> pointLS = IssueConverter.INSTANCE.toList(pointCaches);
        return grpcIssueService.setGrpc(pointLS);
    }

    private Response cvModeTg(CVModeDto dto, int level) throws Exception {
        Map<Integer, String[]> map = new HashMap<>();
        String[] arr = new String[17];
        arr[0] = IssueConstants.CVMode.WORK_CV_Mode;
        arr[1] = IssueConstants.CVMode.WORK_CV_SET_T2GSP;
        arr[2] = IssueConstants.CVMode.WORK_CV_SET_STEP;
        arr[3] = IssueConstants.CVMode.WORK_CV_SET_MIN;
        arr[4] = IssueConstants.CVMode.WORK_CV_SET_MAX;
        arr[5] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_0;
        arr[6] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_1;
        arr[7] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_2;
        arr[8] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_3;
        arr[9] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_4;
        arr[10] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_5;
        arr[11] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_6;
        arr[12] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_7;
        arr[13] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_8;
        arr[14] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_9;
        arr[15] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_10;
        arr[16] = IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_11;

        map.put(dto.getSystemId(), arr);
        List<PointCache> pointCaches = redisCacheService.queryRealDataBySystems(map, level);
        if (CollectionUtils.isEmpty(pointCaches)) {
            return Response.fail("数据查询为空，控制下发失败！");
        }
        if (pointCaches.size() < 17) {
            return Response.fail("服务参数配置错误！");
        }
        if (!Objects.equals(pointCaches.get(0).getPointName(), IssueConstants.CVMode.WORK_CV_Mode) || dto.getOptions() == null) {
            return Response.fail("工作方式参数错误！");
        }
        if (!Objects.equals(pointCaches.get(1).getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP) || dto.getCvTg().getCvTgSP() == null) {
            return Response.fail("恒定二次供温设定值参数错误！");
        }
        if (!Objects.equals(pointCaches.get(2).getPointName(), IssueConstants.CVMode.WORK_CV_SET_STEP)) {
            return Response.fail("步进值参数配置错误！");
        }
        if (!Objects.equals(pointCaches.get(3).getPointName(), IssueConstants.CVMode.WORK_CV_SET_MIN)) {
            return Response.fail("一次调节阀最小开度参数配置错误！");
        }
        if (!Objects.equals(pointCaches.get(4).getPointName(), IssueConstants.CVMode.WORK_CV_SET_MAX)) {
            return Response.fail("一次调节阀最大开度参数配置错误！");
        }

        pointCaches.get(0).setValue(String.valueOf(dto.getOptions()));
        pointCaches.get(1).setValue(String.valueOf(dto.getCvTg().getCvTgSP()));

        if (dto.getCvTg().getCvStep() != null) {
            pointCaches.get(2).setValue(String.valueOf(dto.getCvTg().getCvStep()));
        }
        if (dto.getCvTg().getCvMin() != null) {
            pointCaches.get(3).setValue(String.valueOf(dto.getCvTg().getCvMin()));
        }
        if (dto.getCvTg().getCvMax() != null) {
            pointCaches.get(4).setValue(String.valueOf(dto.getCvTg().getCvMax()));
        }

        if (!Objects.equals(pointCaches.get(5).getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_0) || dto.getCvTg().getTgSPTime0() == null) {
            return Response.fail("二次供温0时段偏差设定值参数错误！");
        }
        if (!Objects.equals(pointCaches.get(6).getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_1) || dto.getCvTg().getTgSPTime1() == null) {
            return Response.fail("二次供温2时段偏差设定值参数错误！");
        }
        if (!Objects.equals(pointCaches.get(7).getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_2) || dto.getCvTg().getTgSPTime2() == null) {
            return Response.fail("二次供温4时段偏差设定值参数错误！");
        }
        if (!Objects.equals(pointCaches.get(8).getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_3) || dto.getCvTg().getTgSPTime3() == null) {
            return Response.fail("二次供温6时段偏差设定值参数错误！");
        }
        if (!Objects.equals(pointCaches.get(9).getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_4) || dto.getCvTg().getTgSPTime4() == null) {
            return Response.fail("二次供温8时段偏差设定值参数错误！");
        }
        if (!Objects.equals(pointCaches.get(10).getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_5) || dto.getCvTg().getTgSPTime5() == null) {
            return Response.fail("二次供温10时段偏差设定值参数错误！");
        }
        if (!Objects.equals(pointCaches.get(11).getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_6) || dto.getCvTg().getTgSPTime6() == null) {
            return Response.fail("二次供温12时段偏差设定值参数错误！");
        }
        if (!Objects.equals(pointCaches.get(12).getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_7) || dto.getCvTg().getTgSPTime7() == null) {
            return Response.fail("二次供温14时段偏差设定值参数错误！");
        }
        if (!Objects.equals(pointCaches.get(13).getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_8) || dto.getCvTg().getTgSPTime8() == null) {
            return Response.fail("二次供温16时段偏差设定值参数错误！");
        }
        if (!Objects.equals(pointCaches.get(14).getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_9) || dto.getCvTg().getTgSPTime9() == null) {
            return Response.fail("二次供温18时段偏差设定值参数错误！");
        }
        if (!Objects.equals(pointCaches.get(15).getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_10) || dto.getCvTg().getTgSPTime10() == null) {
            return Response.fail("二次供温20时段偏差设定值参数错误！");
        }
        if (!Objects.equals(pointCaches.get(16).getPointName(), IssueConstants.CVMode.WORK_CV_SET_T2GSP_TIME_11) || dto.getCvTg().getTgSPTime11() == null) {
            return Response.fail("二次供温22时段偏差设定值参数错误！");
        }
        pointCaches.get(5).setValue(String.valueOf(dto.getCvTg().getTgSPTime0()));
        pointCaches.get(6).setValue(String.valueOf(dto.getCvTg().getTgSPTime1()));
        pointCaches.get(7).setValue(String.valueOf(dto.getCvTg().getTgSPTime2()));
        pointCaches.get(8).setValue(String.valueOf(dto.getCvTg().getTgSPTime3()));
        pointCaches.get(9).setValue(String.valueOf(dto.getCvTg().getTgSPTime4()));
        pointCaches.get(10).setValue(String.valueOf(dto.getCvTg().getTgSPTime5()));
        pointCaches.get(11).setValue(String.valueOf(dto.getCvTg().getTgSPTime6()));
        pointCaches.get(12).setValue(String.valueOf(dto.getCvTg().getTgSPTime7()));
        pointCaches.get(13).setValue(String.valueOf(dto.getCvTg().getTgSPTime8()));
        pointCaches.get(14).setValue(String.valueOf(dto.getCvTg().getTgSPTime9()));
        pointCaches.get(15).setValue(String.valueOf(dto.getCvTg().getTgSPTime10()));
        pointCaches.get(16).setValue(String.valueOf(dto.getCvTg().getTgSPTime11()));


        // 后期要加上数据的有效性    要在正确的量程范围内
        List<PointL> pointLS = IssueConverter.INSTANCE.toList(pointCaches);
        return grpcIssueService.setGrpc(pointLS);
    }

    private Response cvModeFixed(CVModeDto dto, int level) throws Exception {
        Map<Integer, String[]> map = new HashMap<>();
        String[] arr = new String[2];
        arr[0] = IssueConstants.CVMode.WORK_CV_Mode;
        arr[1] = IssueConstants.CVMode.WORK_CV_SET_OPEN;
        map.put(dto.getSystemId(), arr);

        List<PointCache> pointCaches = redisCacheService.queryRealDataBySystems(map, level);
        if (CollectionUtils.isEmpty(pointCaches)) {
            return Response.fail("数据查询为空，控制下发失败！");
        }
        if (pointCaches.size() < 2) {
            return Response.fail("服务参数配置错误！");
        }

        if (!Objects.equals(pointCaches.get(0).getPointName(), IssueConstants.CVMode.WORK_CV_Mode) || dto.getOptions() == null) {
            return Response.fail("工作方式参数错误！");
        }
        if (!Objects.equals(pointCaches.get(1).getPointName(), IssueConstants.CVMode.WORK_CV_SET_OPEN) || dto.getCvMsp() == null) {
            return Response.fail("开度定值参数错误！");
        }
        pointCaches.get(0).setValue(String.valueOf(dto.getOptions()));
        pointCaches.get(1).setValue(String.valueOf(dto.getCvMsp()));

        // 后期要加上数据的有效性    要在正确的量程范围内
        List<PointL> pointLS = IssueConverter.INSTANCE.toList(pointCaches);
        return grpcIssueService.setGrpc(pointLS);
    }
}
