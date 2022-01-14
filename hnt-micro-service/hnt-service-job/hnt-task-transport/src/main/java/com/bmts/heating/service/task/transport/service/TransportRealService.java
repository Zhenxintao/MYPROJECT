package com.bmts.heating.service.task.transport.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bmts.heating.commons.entiy.second.request.point.TransportPoint;
import com.bmts.heating.commons.entiy.second.request.point.TransportPointTemp;
import com.bmts.heating.service.task.transport.utils.UrlConnUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName: TransportRealService
 * @Description: 处理长输入 实时库业务
 * @Author: pxf
 * @Date: 2021/11/12 11:36
 * @Version: 1.0
 */
@Service
public class TransportRealService {

    private static Logger logger = LoggerFactory.getLogger(TransportRealService.class);

    @Value("${transport.serverUrl}")
    private String serverUrl;

    /**
     * 长输采集点入实时库
     *
     * @return void
     * @Method setRealData
     * @Param [points]
     * @Description
     */
    public void setRealData(List<TransportPoint> points) {
        JSONArray jsonArray = new JSONArray();
        points.stream().forEach(e -> {
            jsonArray.add(e.getPointAddress());
        });
        // 转为Map
        Map<String, TransportPoint> collectMap = points.stream().collect(Collectors.toMap(TransportPoint::getPointAddress, TransportPoint -> TransportPoint));
        try {
            // 进行请求 数据   接口请求数据
            String pvssStr = UrlConnUtil.postUrl(this.serverUrl, jsonArray.toJSONString());

            if (pvssStr == null) {
                System.out.println("请求 PVSS 错误！请求数据格式错误！");
                logger.error("请求 PVSS 服务器失败---{ }", pvssStr);
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
                            TransportPoint transportPoint = collectMap.get(e.getTagName());
                            if (StringUtils.isNotBlank(e.getTimeStamp())) {
                                transportPoint.setTimeStamp(e.getTimeStamp());
                            }
                            if (StringUtils.isNotBlank(e.getValue())) {
                                transportPoint.setValue(e.getValue());
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("处理 PVSS 长输采集数据异常---{ }", e);
        }

    }


}
