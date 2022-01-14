package com.bmts.heating.web.forecast.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceCron;
import com.bmts.heating.commons.container.quartz.service.JobService;
import com.bmts.heating.commons.db.mapper.ForecastSourceCronMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/4/7 14:46
 **/
@Slf4j
@Component
public class ForeCastStart implements ApplicationListener<ContextRefreshedEvent> {


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ForecastSourceCronMapper bean = contextRefreshedEvent.getApplicationContext().getBean(ForecastSourceCronMapper.class);
        List<ForecastSourceCron> forecastSourceCrons = bean.selectList(Wrappers.<ForecastSourceCron>lambdaQuery().gt(ForecastSourceCron::getId, 0));
        if (forecastSourceCrons.size() > 0) {
            ForecastSourceCron forecastSourceCron = forecastSourceCrons.get(0);
            JobService jobService = contextRefreshedEvent.getApplicationContext().getBean(JobService.class);
            try {
                jobService.addCronJob("forecast_hour", "forecast", forecastSourceCron.getCorn(), HourJob.class, null);
                log.info("add forecast_hour_job success");
            } catch (Exception e) {
                log.error("添加小时预测任务失败");
            }

            if (StringUtils.isNotBlank(forecastSourceCron.getWeekCron())) {
                try {
                    jobService.addCronJob("forecast_week", "forecast", forecastSourceCron.getWeekCron(), WeekJob.class, null);
                    log.info("add forecast_week_job success");
                } catch (Exception e) {
                    log.error("添加周预测任务失败");
                }
            }

        }

    }


}
