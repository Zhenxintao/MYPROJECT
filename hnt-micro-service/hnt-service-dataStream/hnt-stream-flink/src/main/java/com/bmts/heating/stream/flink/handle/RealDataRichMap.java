package com.bmts.heating.stream.flink.handle;

import com.alibaba.fastjson.JSONObject;
import com.bmts.heating.commons.container.signalr.service.SignalRTemplate;
import com.bmts.heating.commons.redis.service.RedisPointService;
import com.bmts.heating.commons.utils.container.SpringBeanFactory;
import com.bmts.heating.commons.utils.msmq.Message_Point_Gather;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.stream.flink.app.SpringBootFlinkApplication;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: TestFlatMap
 * @Description: 测试 flink
 * @Author: pxf
 * @Date: 2021/7/21 16:59
 * @Version: 1.0
 */

public class RealDataRichMap extends RichMapFunction<Message_Point_Gather, Message_Point_Gather> {

    private static Logger logger = LoggerFactory.getLogger(RealDataRichMap.class);

    private SignalRTemplate signalRTemplate;

    private RedisPointService redisPointService;

    @Override
    public void open(Configuration parameters) throws Exception {
        // 开启 spring 容器
        SpringBootFlinkApplication.run();
        this.signalRTemplate = SpringBeanFactory.getBean(SignalRTemplate.class);
        this.redisPointService = SpringBeanFactory.getBean(RedisPointService.class);
    }

    @Override
    public Message_Point_Gather map(Message_Point_Gather pointGather) throws Exception {
        Map<String, PointL> pointMap = pointGather.getPointLS();
        sendReal(pointMap);
        return pointGather;
    }


    public void sendReal(Map<String, PointL> pointMap) {
        List<PointL> plList = pointMap.values().stream().collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(plList)) {
            // 推送 SignalR
            plList.forEach(pl -> {
                if (pl != null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("pointId", pl.getPointId());
                    if (StringUtils.isNotBlank(pl.getValue())) {
                        jsonObject.put("value", pl.getValue());
                    } else {
                        jsonObject.put("value", 0);
                    }
                    jsonObject.put("timeStrap", pl.getTimeStrap());
                    try {
                        // 推送 signal
                        signalRTemplate.send2AllServerSpecial("PushRealValue", jsonObject.toJSONString());
                    } catch (Exception e) {
                        logger.error("SignalR---推送异常数据：name={},level={},releverceId={},type={},timeStrap={}",
                                pl.getPointName(), pl.getLevel(), pl.getRelevanceId(), pl.getType(), pl.getTimeStrap());
                        e.printStackTrace();
                    }
                }
            });

            // 推送到实时库
            try {
                redisPointService.setCachePoint(plList);
                logger.info("Actual---推送到实时库---数据：releverceId={}, total ={} ",
                        plList.get(0).getRelevanceId(), plList.size());
            } catch (Exception e) {
                logger.error("ERRORActual---异常数据---:releverceId={},批号 =={},total={},e ={}",
                        plList.get(0).getRelevanceId(), plList.get(0).getLotNo(), plList.size(), e);
                e.printStackTrace();
            }

        }

    }


}
