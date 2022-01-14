package com.bmts.heating.service.core;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.RealTemperature;
import com.bmts.heating.commons.basement.model.db.entity.WeatherForecast;
import com.bmts.heating.commons.container.performance.config.ConnectionToken;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.db.service.RealTemperatureService;
import com.bmts.heating.commons.db.service.WeatherForecastService;
import com.bmts.heating.commons.entiy.baseInfo.request.WeatherForecastRegisterDto;
import com.bmts.heating.service.config.WeatherForecastConfig;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.bmts.heating.commons.utils.common.DateTimeUtil.dateDiff;

/**
 * @author naming
 * @description
 * @date 2021/4/8 9:43
 **/
@Slf4j
@Component("weather_job")
public class WeatherJob extends SavantServices implements Job {
    @Autowired
    WeatherForecastService weatherForecastService;
    @Autowired
    RealTemperatureService realTemperatureService;
    @Autowired
    WeatherForecastConfig weatherForecastConfig;
    @Autowired
    RestTemplate restTemplate;

    String baseServer = "bussiness_baseInfomation";
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

            try {
                WeatherForecast weatherForecast = weatherForecastService.getOne(Wrappers.<WeatherForecast>lambdaQuery().orderByDesc(WeatherForecast::getCreateTime).last("limit 1"));
                RealTemperature realTemperature = realTemperatureService.getOne(Wrappers.<RealTemperature>lambdaQuery().orderByDesc(RealTemperature::getCreateTime).last("limit 1"));
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String startTime = df.format(weatherForecast.getCreateTime());
                String startTimeReal = df.format(realTemperature.getCreateTime());
                String endTime = df.format(LocalDateTime.now());
                String format = "yyyy-MM-dd HH:mm:ss";
                long resultCount = dateDiff(startTime, endTime, format, 2);
                long resultRealCount = dateDiff(startTimeReal, endTime, format, 2);
                if (resultCount >= 2 || resultRealCount >= 2) {
                    weatherForecastRegister();
                }

            } catch (Exception e) {
                log.error("weaher job start error {}",e.getMessage());
            }

    }

    //注册天气预报服务端
    private void weatherForecastRegister() {
        ConnectionToken cd = null;
        try {
            cd = super.getToken(baseServer);
            WeatherForecastRegisterDto dto = new WeatherForecastRegisterDto();
            dto.setUrl(cd.getHost().concat(":").concat(cd.getPort()));
            dto.setCode(weatherForecastConfig.getCitycode());
            Object result= restTemplate.postForObject(weatherForecastConfig.getServerUrl(), dto, Object.class);
            log.info("天气预报数据注册成功!", result);
        } catch (Exception e) {
            super.clearToken(baseServer, e);
            e.printStackTrace();
            log.error("weaher regist error {}",e.getMessage());
        } finally {
            super.backToken(baseServer, cd);
        }

    }
}
