package com.bmts.heating.service.task.transport.jobs;

import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.middleware.cache.services.RedisCacheService;
import com.bmts.heating.service.task.transport.service.HeatSourceMonitorService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Component("heatSource_point_monitor")
public class HeatSourceMonitorJob implements Job {

    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private HeatSourceMonitorService heatSourceMonitorService;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        List<PointL> pointLS = redisCacheService.querySourcePoints();
        // 进行数据采集
        if (!CollectionUtils.isEmpty(pointLS)) {
            // 进行数据采集
            heatSourceMonitorService.monitorData(pointLS);
        }

        // 进行采集
        log.info("---完成热源实时数据查询！---");

    }
}
