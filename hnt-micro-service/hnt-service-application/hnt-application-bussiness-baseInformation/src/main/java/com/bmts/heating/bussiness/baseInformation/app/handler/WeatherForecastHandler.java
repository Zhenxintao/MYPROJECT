package com.bmts.heating.bussiness.baseInformation.app.handler;

import com.bmts.heating.bussiness.baseInformation.app.joggle.basic.WeatherForecastJogger;
import com.bmts.heating.commons.container.quartz.service.impl.JobServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WeatherForecastHandler implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private WeatherForecastJogger weatherForecastJogger;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        weatherForecastJogger.weatherForecastRegister();
//        JobServiceImpl jobService = event.getApplicationContext().getBean(JobServiceImpl.class);
//        Boolean res = jobService.addJob("weatherforecastcheck", "weatherforecast", 60, WeatherForecastCheckJob.class, null, JobTimeStageType.MINUTE.type());
//        if (res) {
//            log.info("已成功注册天气预报数据job");
//        }
//        else {
//            log.error("注册天气预报job失败");
//        }
    }
}
