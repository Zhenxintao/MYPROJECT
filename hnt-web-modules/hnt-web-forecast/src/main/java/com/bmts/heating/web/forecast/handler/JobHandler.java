package com.bmts.heating.web.forecast.handler;

import com.bmts.heating.commons.db.service.ForecastSourceCoreService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author naming
 * @description
 * @date 2021/4/7 14:31
 **/
@Service
@Component("forecast_job")
public class JobHandler implements Job {

    @Autowired
    private ForecastSourceCoreService forecastSourceCoreService;
    @Autowired
    private ForeCastHandler foreCastHandler;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 查询热源基础数据
//        List<ForecastSourceCore> list = forecastSourceCoreService.list(Wrappers.<ForecastSourceCore>lambdaQuery().eq(ForecastSourceCore::getIsValid, true));
//        for (ForecastSourceCore forecastSourceCore : list) {
//            foreCastHandler.forecastBasics(forecastSourceCore);
//        }

    }

}
