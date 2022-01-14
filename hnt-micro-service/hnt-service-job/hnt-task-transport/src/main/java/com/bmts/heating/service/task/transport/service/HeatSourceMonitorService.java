package com.bmts.heating.service.task.transport.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.WebPageConfig;
import com.bmts.heating.commons.db.service.WebPageConfigService;
import com.bmts.heating.commons.entiy.second.request.point.TransportPointTemp;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.service.task.transport.service.async.HeatSourceAsync;
import com.bmts.heating.service.task.transport.utils.UrlConnUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: HeatSourceRealService
 * @Description: 处理 热源 数据采集业务
 * @Author: pxf
 * @Date: 2021/11/12 11:36
 * @Version: 1.0
 */
@Slf4j
@Service
public class HeatSourceMonitorService {

    @Autowired
    private WebPageConfigService webPageConfigService;

    @Autowired
    private HeatSourceAsync heatSourceAsync;

    @Value("${transport.serverUrl}")
    private String serverUrl;

    /**
     * 热源数据采集
     *
     * @return void
     * @Method monitorData
     * @Param [list]
     * @Description
     */
    public void monitorData(List<PointL> pointLS) {
        Map<String, PointL> pointMap = new HashMap<>();
        JSONArray jsonArray = new JSONArray();
        pointLS.stream().forEach(e -> {
            if (StringUtils.isNotBlank(e.getSyncNumber())) {
                jsonArray.add(e.getSyncNumber());
                pointMap.put(e.getSyncNumber(), e);
            }
        });
        List<PointL> listPointL = new ArrayList<>();
        try {
            // 进行请求 数据   接口请求数据
            long starTime = System.currentTimeMillis();
            log.info("monitorData---开始请求热源数据：时间= {}", starTime);
            String pvssStr = UrlConnUtil.postUrl(this.serverUrl, jsonArray.toJSONString());
            long endTime = System.currentTimeMillis();
            log.info("monitorData---请求结束：时间= {}, 请求耗时--{} 毫秒", endTime, (endTime - starTime));

            if (pvssStr == null) {
                log.error("monitorData---请求数据失败---{ }", pvssStr);
            } else {
                // 处理业务
                JSONObject responseJson = JSONObject.parseObject(pvssStr);
                // 返回的code 码
                String code = responseJson.getString("code");
                if (StringUtils.isBlank(code)) {
                    return;
                }
                if (Objects.equals(code, "200")) {
                    // 返回的数据
                    String data = responseJson.getString("data");
                    List<TransportPointTemp> transportPoints = JSONArray.parseArray(data, TransportPointTemp.class);
                    // 处理数据
                    transportPoints.stream().forEach(e -> {
                        // 进行赋值操作
                        if (StringUtils.isNotBlank(e.getTagName())) {
                            PointL pointL = pointMap.get(e.getTagName());
                            if (pointL != null) {
                                if (StringUtils.isNotBlank(e.getTimeStamp())) {
                                    // 把 string 类型的时间转为 long 类型的时间戳
                                    LocalDateTime parse = LocalDateTime.parse(e.getTimeStamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                    pointL.setTimeStrap(parse.toEpochSecond(ZoneOffset.of("+8")));
                                }
                                if (StringUtils.isNotBlank(e.getValue())) {
                                    pointL.setOldValue(e.getValue());
                                }
                                listPointL.add(pointL);
                            }
                        }
                    });
                }
            }
            if (!CollectionUtils.isEmpty(listPointL)) {
                sendReal(listPointL);
            }

            pointMap.clear();

        } catch (Exception e) {
            e.printStackTrace();
            log.error("处理 热源采集数据异常---{ }", e);
        }

    }


    private void sendReal(List<PointL> listPointL) {
        // 按照系统进行分组
        Map<Integer, List<PointL>> groupMap = listPointL.stream().collect(Collectors.groupingBy(e -> e.getDataType() + e.getLevel() + e.getRelevanceId()));
        // 请求 水电热的比较配置
        Map<String, Object> eneryConfigMap = null;
        WebPageConfig eneryConfig = webPageConfigService.getOne(Wrappers.<WebPageConfig>lambdaQuery().eq(WebPageConfig::getConfigKey, "eneryConfig"));
        if (eneryConfig != null && StringUtils.isNotBlank(eneryConfig.getJsonConfig())) {
            eneryConfigMap = JSON.parseObject(eneryConfig.getJsonConfig(), Map.class);
        }
        Map<String, Object> finalEneryConfigMap = eneryConfigMap;
        groupMap.forEach((k, v) -> {
            if (!CollectionUtils.isEmpty(v)) {
                // 推送到 redis 实时库
                heatSourceAsync.sendReal(v, finalEneryConfigMap);
            }
        });


    }


}
