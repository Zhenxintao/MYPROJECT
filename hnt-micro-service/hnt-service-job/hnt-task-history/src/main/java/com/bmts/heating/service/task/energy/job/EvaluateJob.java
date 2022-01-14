package com.bmts.heating.service.task.energy.job;

import com.bmts.heating.service.task.energy.handler.EvaluateHandler;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author naming
 * @description
 * @date 2021/4/27 16:29
 **/
@Component("energy_evaluate_job")
@Slf4j
public class EvaluateJob implements Job {
    @Autowired
    private EvaluateHandler evaluateHandler;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("评价开始执行");
        evaluateHandler.start();
    }
}
