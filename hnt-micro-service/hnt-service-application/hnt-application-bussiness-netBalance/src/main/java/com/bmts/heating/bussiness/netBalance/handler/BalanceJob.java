package com.bmts.heating.bussiness.netBalance.handler;

import com.bmts.heating.bussiness.netBalance.service.BalanceService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/1/30 15:19
 **/
@Slf4j
public class BalanceJob implements Job {
    @Autowired
   private BalanceService balanceService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        Object balanceNetId = jobExecutionContext.getJobDetail().getJobDataMap().get("balanceNetId");

       System.out.println(jobExecutionContext.getJobDetail().getKey().getName());
       String[] resultInfo = jobExecutionContext.getJobDetail().getKey().getName().split("_");
       List<String> strList= Arrays.asList(resultInfo);
        balanceService.start(Integer.parseInt(strList.get(0)),1);
        log.info("balanceControl job start ~~~~~~~~~~");
    }
}
