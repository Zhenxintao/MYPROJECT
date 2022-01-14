package com.bmts.heating.service.job.handler;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.QuartzJob;
import com.bmts.heating.commons.container.quartz.service.JobService;
import com.bmts.heating.commons.db.service.QuartzJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/3/17 10:06
 **/
@Component
@Slf4j
public class JobCore implements ApplicationListener<ContextRefreshedEvent> {


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        JobService jobService = contextRefreshedEvent.getApplicationContext().getBean(JobService.class);
        QuartzJobService quartzJobService = contextRefreshedEvent.getApplicationContext().getBean(QuartzJobService.class);
        List<QuartzJob> list = quartzJobService.list(Wrappers.<QuartzJob>lambdaQuery().eq(QuartzJob::getStatus, true));
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(x -> {
                try {
                    Job test_job = contextRefreshedEvent.getApplicationContext().getBean(x.getBeanName(), Job.class);
                    jobService.addCronJob(x.getName(), x.getGroupName(), x.getCron(), test_job.getClass(), null);
                } catch (Exception e) {
                    log.error("job start error from db args:{}", JSONObject.toJSON(x));
                }
            });

        }
    }
}
