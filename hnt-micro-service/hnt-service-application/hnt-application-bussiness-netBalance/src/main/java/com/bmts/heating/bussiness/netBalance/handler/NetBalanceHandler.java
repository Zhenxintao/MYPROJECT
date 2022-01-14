package com.bmts.heating.bussiness.netBalance.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.BalanceNet;
import com.bmts.heating.commons.container.quartz.JobTimeStageType;
import com.bmts.heating.commons.container.quartz.service.impl.JobServiceImpl;
import com.bmts.heating.commons.db.service.BalanceNetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/1/31 10:09
 **/
@Slf4j
@Component
public class NetBalanceHandler implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        BalanceNetService balanceNetService = event.getApplicationContext().getBean(BalanceNetService.class);
        JobServiceImpl jobService = event.getApplicationContext().getBean(JobServiceImpl.class);
        List<BalanceNet> list = balanceNetService.list(Wrappers.<BalanceNet>lambdaQuery().eq(BalanceNet::getStatus, true));
        for (BalanceNet balanceNet : list) {
            Boolean controlResult = jobService.addJob(balanceNet.getId() + "_" + balanceNet.getName() + "_control", "Balance", balanceNet.getCycle(), BalanceJob.class,null, JobTimeStageType.MINUTE.type());
            if (controlResult)
            {
                log.info("启动状态下全网平衡控制下发周期启动成功---热网名称{}", balanceNet.getName());
                Boolean computeResult = jobService.addJob(balanceNet.getId() + "_" + balanceNet.getName() + "_compute", "Balance", 2, ComputeJob.class, null,JobTimeStageType.MINUTE.type());
                if (computeResult)
                    log.info("启动状态下全网平衡空算周期启动成功--- 热网名称{}",balanceNet.getName());
                else
                    log.error("启动状态下全网平衡空算周期启动失败--- 热网名称{}", balanceNet.getName());
            }
            else
                log.error("启动状态下全网平衡控制下发周期启动失败---热网名称{}，循环周期为:{}", balanceNet.getName(),balanceNet.getCycle());
        }
    }
}
