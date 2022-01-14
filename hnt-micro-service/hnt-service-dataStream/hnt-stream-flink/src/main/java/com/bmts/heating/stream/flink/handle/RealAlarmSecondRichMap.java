package com.bmts.heating.stream.flink.handle;

import com.alibaba.fastjson.JSONObject;
import com.bmts.heating.commons.entiy.baseInfo.enums.AbnormalLevel;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.Abnormal;
import com.bmts.heating.commons.redis.service.RedisPointService;
import com.bmts.heating.commons.utils.container.SpringBeanFactory;
import com.bmts.heating.commons.utils.enums.HealthStatus;
import com.bmts.heating.commons.utils.msmq.Message_Point_Gather;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.commons.utils.msmq.PointType;
import com.bmts.heating.middleware.td.HistoryTdGrpcClient;
import com.bmts.heating.stream.flink.app.SpringBootFlinkApplication;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: RealAlarmSecondRichMap
 * @Description: 处理数据超出合理范围
 * @Author: pxf
 * @Date: 2021/7/21 16:59
 * @Version: 1.0
 */

public class RealAlarmSecondRichMap extends RichMapFunction<Message_Point_Gather, Message_Point_Gather> {

    private static Logger logger = LoggerFactory.getLogger(RealAlarmSecondRichMap.class);


    private RedisPointService redisPointService;

    private HistoryTdGrpcClient historyTdGrpcClient;

    @Override
    public void open(Configuration parameters) throws Exception {
        // 开启 spring 容器
        SpringBootFlinkApplication.run();
        this.redisPointService = SpringBeanFactory.getBean(RedisPointService.class);
        this.historyTdGrpcClient = SpringBeanFactory.getBean(HistoryTdGrpcClient.class);
    }

    @Override
    public Message_Point_Gather map(Message_Point_Gather pointGather) throws Exception {
        Map<String, PointL> pointMap = pointGather.getPointLS();
        List<PointL> plList = pointMap.values().stream().collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(plList)) {
            for (PointL pointL : plList) {
                realAlarm(pointL);
            }
        }

        return pointGather;
    }


    private void realAlarm(PointL pointL) {
        // 判断 超出合理范围数据
        if (pointL.getHealthSign() == HealthStatus.HEALTH_DATA.status() && StringUtils.isNotBlank(pointL.getHightLower()) && pointL.getType() != PointType.POINT_BOOL.getType()) {
            // 根据 pointL 里面的 高限或低限字段进行判断
            String hightLower = pointL.getHightLower();
            JSONObject jsonObject = JSONObject.parseObject(hightLower);
            String runningHigh = jsonObject.getString("runningHigh");
            String runningLower = jsonObject.getString("runningLower");
            String oldValue = pointL.getValue();
            BigDecimal value = null;
            BigDecimal high = null;
            BigDecimal lower = null;
            Integer qualityStamp = null;
            if (StringUtils.isNotBlank(oldValue)) {
                value = new BigDecimal(oldValue);
            }
            // 运行高限
            if (StringUtils.isNotBlank(runningHigh)) {
                high = new BigDecimal(runningHigh);
            }
            // 运行低限
            if (StringUtils.isNotBlank(runningLower)) {
                lower = new BigDecimal(runningLower);
            }

            if (value != null) {
                if (high != null && value.compareTo(high) == 1) {
                    qualityStamp = 194;
                } else if (lower != null && value.compareTo(lower) == -1) {
                    qualityStamp = 193;
                }

            }
            if (qualityStamp != null) {
                pointL.setQualityStrap(qualityStamp);
                pointL.setHealthSign(HealthStatus.OUT_REASONABLE_RANGE.status());
                insertAbnormal(pointL);
            }

            logger.info("RealAlarmSecondRichMap---获得数据：name={},level={},releverceId={},type={},timeStrap={}",
                    pointL.getPointName(), pointL.getLevel(), pointL.getRelevanceId(), pointL.getType(), pointL.getTimeStrap());

        }


    }

    private void insertAbnormal(PointL pointL) {
        Abnormal dto = new Abnormal();
        dto.setGroupId(pointL.getRelevanceId());
        dto.setType(HealthStatus.OUT_REASONABLE_RANGE.status());
        dto.setMsg(HealthStatus.OUT_REASONABLE_RANGE.getName());
        if (pointL.getHeatType() == 1) {
            dto.setLevel(AbnormalLevel.HEAT_STATION.getLevel());
        }
        if (pointL.getHeatType() == 2) {
            dto.setLevel(AbnormalLevel.HEAT_SOURCE.getLevel());
        }
        dto.setPoint(pointL.getPointName());
        dto.setValue(pointL.getValue());
        dto.setTs(System.currentTimeMillis());
        historyTdGrpcClient.insertAbnormal(dto);
    }

}
