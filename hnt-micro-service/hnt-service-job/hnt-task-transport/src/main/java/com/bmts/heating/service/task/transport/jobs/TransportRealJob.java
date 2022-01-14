package com.bmts.heating.service.task.transport.jobs;

import com.alibaba.fastjson.JSONArray;
import com.bmts.heating.commons.entiy.second.request.point.TransportPoint;
import com.bmts.heating.commons.redis.second.RedisTransportService;
import com.bmts.heating.service.task.transport.service.TransportRealService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component("transport_point_real")
public class TransportRealJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(TransportRealJob.class);
    @Autowired
    private RedisTransportService redisTransportService;
    @Autowired
    private TransportRealService transportRealService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        // 查询所有的数据
        Map<String, Map<String, Object>> transportDtoMap = redisTransportService.queryTransportMap();
        if (!CollectionUtils.isEmpty(transportDtoMap)) {
            // 进行采集数据和赋值
            transportDtoMap.forEach((k, v) -> {
                if (Optional.ofNullable(v.get("points")).isPresent()) {
                    Object points = v.get("points");
                    List<TransportPoint> transportPoints = JSONArray.parseArray(points.toString(), TransportPoint.class);
                    // 进行数据采集
                    transportRealService.setRealData(transportPoints);
                    // 重新进行赋值
                    v.put("points", transportPoints);

                }
            });
        }
        // 推送到 redis 数据同步
        redisTransportService.setTransportMap(transportDtoMap);
        logger.info("---完成长输实时数据入库！---");

    }
}
