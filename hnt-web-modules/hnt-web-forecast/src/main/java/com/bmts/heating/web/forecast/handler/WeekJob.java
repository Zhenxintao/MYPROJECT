package com.bmts.heating.web.forecast.handler;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceBasic;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceCore;
import com.bmts.heating.commons.basement.model.db.entity.ForecastWeatherCoef;
import com.bmts.heating.commons.basement.model.db.entity.WebPageConfig;
import com.bmts.heating.commons.basement.model.db.request.ForecastRequest;
import com.bmts.heating.commons.db.service.ForecastSourceBasicService;
import com.bmts.heating.commons.db.service.ForecastSourceCoreService;
import com.bmts.heating.commons.db.service.WebPageConfigService;
import com.bmts.heating.commons.entiy.forecast.ForecastWebPageConfig;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.forecast.service.ForecastWeatherCoefService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/4/20 16:44
 **/
public class WeekJob implements Job {

    @Autowired
    private ForecastSourceBasicService forecastSourceBasicService;
    @Autowired
    private ForecastSourceCoreService forecastSourceCoreService;
    @Autowired
    private ForeCastHandler foreCastHandler;
    @Autowired
    private ForecastWeatherCoefService forecastWeatherCoefService;
    @Autowired
    private WebPageConfigService webPageConfigService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 天预测
        LocalDateTime nowTime = LocalDateTime.now();
        // 根据当前时间查询供暖季基础数据
        QueryWrapper<ForecastSourceBasic> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("endTime", nowTime);
        queryWrapper.le("startTime", nowTime);
        ForecastSourceBasic forecastSourceBasic = forecastSourceBasicService.getOne(queryWrapper);
        if (forecastSourceBasic == null) {
            throw new RuntimeException("没有查询到当前供暖季基础数据！");
        }
        // 查询热源基础数据
        List<ForecastSourceCore> list = forecastSourceCoreService.list(Wrappers.<ForecastSourceCore>lambdaQuery().eq(ForecastSourceCore::getIsValid, true));
        if (CollectionUtils.isEmpty(list)) {
            throw new RuntimeException("没有查询到热源基础数据！");
        }
        // 查询系数配置
        ForecastWeatherCoef forecastWeatherCoef = null;
        List<ForecastWeatherCoef> listForecastWeather = forecastWeatherCoefService.list();
        if (!com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isEmpty(listForecastWeather)) {
            forecastWeatherCoef = listForecastWeather.get(0);
        }

        // 查询负荷常量配置
        WebPageConfig webPageConfig = webPageConfigService.getOne(Wrappers.<WebPageConfig>lambdaQuery()
                .eq(WebPageConfig::getConfigKey, "forecastSource"));
        if (webPageConfig == null) {
            throw new RuntimeException("没有查询到负荷预测的常量配置信息！");
        }
        ForecastWebPageConfig forecastWebPageConfig = null;
        if (StringUtils.isNotBlank(webPageConfig.getJsonConfig())) {
            forecastWebPageConfig = JSONObject.parseObject(webPageConfig.getJsonConfig(), ForecastWebPageConfig.class);
        }


        for (ForecastSourceCore forecastSourceCore : list) {
            ForecastRequest forecastRequest = new ForecastRequest();
            // 设置供暖季基础数据
            forecastRequest.setForecastSourceBasic(forecastSourceBasic);
            // 设置热源基础数据
            forecastRequest.setSourceCore(forecastSourceCore);
            Response response = foreCastHandler.foreCastDay(nowTime, forecastRequest, forecastWeatherCoef,forecastWebPageConfig);
        }


    }
}
