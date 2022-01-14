package com.bmts.heating.bussiness.netBalance.handler;

import com.bmts.heating.bussiness.netBalance.service.BalanceService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class ComputeJob implements Job {
    @Autowired
    private BalanceService balanceService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String[] resultInfo = jobExecutionContext.getJobDetail().getKey().getName().split("_");
        List<String> strList= Arrays.asList(resultInfo);
        balanceService.start(Integer.parseInt(strList.get(0)),2);
    }
}
