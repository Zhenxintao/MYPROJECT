package com.bmts.heating.web.forecast.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.BatchDelete;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceCore;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceCron;
import com.bmts.heating.commons.container.quartz.service.JobService;
import com.bmts.heating.commons.db.mapper.ForecastSourceCronMapper;
import com.bmts.heating.commons.db.service.ForecastSourceCoreService;
import com.bmts.heating.commons.entiy.forecast.CronDto;
import com.bmts.heating.commons.utils.cron.CronUtil;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.forecast.handler.HourJob;
import com.bmts.heating.web.forecast.handler.WeekJob;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author naming
 * @description
 * @date 2021/4/20 15:54
 **/
@RestController
@Api(tags = "负荷预测自动预测配置")
@RequestMapping("forecast/corn")
@Slf4j
public class ForecastCronController {
    @Autowired
    ForecastSourceCronMapper forecastSourceCronMapper;
    @Autowired
    ForecastSourceCoreService forecastSourceCoreService;
    @Autowired
    JobService jobService;

    @ApiOperation("预测配置")
    @PostMapping
    public Response save(@RequestBody CronDto cron) throws ParseException {
        String cronResult = CronUtil.formatDateByPattern(cron.getCron());
        ForecastSourceCron forecastSourceCron = new ForecastSourceCron();
        forecastSourceCron.setCorn(cronResult);
        // 处理星期
        if (StringUtils.isNotBlank(cron.getWeekCron()) && cron.getWeek() != null) {
            String cronWeek = CronUtil.formatDateWeekByPattern(cron.getWeekCron(), cron.getWeek());
            forecastSourceCron.setWeekCron(cronWeek);
        }
        forecastSourceCronMapper.delete(Wrappers.<ForecastSourceCron>lambdaQuery().ne(ForecastSourceCron::getId, 0));


        if (forecastSourceCronMapper.insert(forecastSourceCron) > 0) {
            try {
                jobService.addCronJob("forecast_hour", "forecast", forecastSourceCron.getCorn(), HourJob.class, null);
                log.info("add forecast_hour_job success");
            } catch (Exception e) {
                log.error("添加小时预测任务失败");
            }
            if (StringUtils.isNotBlank(cron.getWeekCron()) && cron.getWeek() != null) {
                try {
                    jobService.addCronJob("forecast_week", "forecast", forecastSourceCron.getWeekCron(), WeekJob.class, null);
                    log.info("add forecast_week_job success");
                } catch (Exception e) {
                    log.error("添加周预测任务失败");
                }
            }
            return Response.success();
        } else return Response.fail("保存失败");
    }

    @ApiOperation("批量停止预测")
    @RequestMapping("/stop/batch")
    public Response stop(BatchDelete batchDelete) {
        ForecastSourceCore cron = new ForecastSourceCore();
        cron.setIsValid(false);
        UpdateWrapper<ForecastSourceCore> updateWrapper = new UpdateWrapper<ForecastSourceCore>();
        updateWrapper.in("id", batchDelete.getIds());
        if (forecastSourceCoreService.update(updateWrapper))
            return Response.success();
        return Response.fail("操作失败");
    }

    @ApiOperation(value = "预测配置查询", response = CronDto.class)
    @GetMapping
    public Response query() {
        ForecastSourceCron forecastSourceCron = forecastSourceCronMapper.selectOne(Wrappers.<ForecastSourceCron>lambdaQuery().gt(ForecastSourceCron::getId, 0));
        if (forecastSourceCron == null)
            return Response.success();
        Map<String, Object> maps = new HashMap<>();
        String[] s = forecastSourceCron.getCorn().split(" ");
        maps.put("cron", s[2].concat(":").concat(s[1]));
        if (StringUtils.isNotBlank(forecastSourceCron.getWeekCron())) {
            String[] weeks = forecastSourceCron.getWeekCron().split(" ");
            maps.put("weekCron", weeks[2].concat(":").concat(weeks[1]));
            maps.put("week", Integer.valueOf(weeks[weeks.length - 1]));
        }
        return Response.success(maps);
    }
}
