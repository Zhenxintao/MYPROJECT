package com.bmts.heating.bussiness.baseInformation.app.handler;

import com.bmts.heating.bussiness.baseInformation.app.joggle.basic.WeatherForecastJogger;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class WeatherForecastCheckJob implements Job {
    @Autowired
    private WeatherForecastJogger weatherForecastJogger;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        weatherForecastJogger.checkWeatherForecast();
    }
}
