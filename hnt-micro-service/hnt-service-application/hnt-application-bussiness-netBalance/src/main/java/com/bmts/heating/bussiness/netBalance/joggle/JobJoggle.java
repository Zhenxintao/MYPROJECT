package com.bmts.heating.bussiness.netBalance.joggle;

import com.bmts.heating.bussiness.netBalance.handler.BalanceJob;
import com.bmts.heating.bussiness.netBalance.handler.ComputeJob;
import com.bmts.heating.bussiness.netBalance.service.BalanceService;
import com.bmts.heating.commons.basement.model.db.entity.BalanceNet;
import com.bmts.heating.commons.container.quartz.JobTimeStageType;
import com.bmts.heating.commons.container.quartz.service.impl.JobServiceImpl;
import com.bmts.heating.commons.container.signalr.service.SignalRTemplate;
import com.bmts.heating.commons.db.service.BalanceNetService;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author naming
 * @description
 * @date 2021/1/30 15:17
 **/
@RestController
@RequestMapping("job")
@Slf4j
public class JobJoggle {

    @Autowired
    JobServiceImpl jobService;
    @Autowired
    BalanceNetService balanceNetService;
    @Autowired
    BalanceService balanceService;

    @ApiModelProperty("启动热网")
    @GetMapping(value = "start/{id}")
    public Response start(@PathVariable int id) {
        BalanceNet balanceNet = balanceNetService.getById(id);
        Boolean controlResult = jobService.addJob(balanceNet.getId() + "_" + balanceNet.getName() + "_control", "Balance", balanceNet.getCycle(), BalanceJob.class, null, JobTimeStageType.MINUTE.type());
        if (controlResult) {
            //1.更新时间
            balanceNet.setNextControlTime(LocalDateTime.now().plusMinutes(balanceNet.getCycle()));
            //2.更新状态
            balanceNet.setStatus(true);
            balanceNetService.updateById(balanceNet);
            Boolean computeResult = jobService.addJob(balanceNet.getId() + "_" + balanceNet.getName() + "_compute", "Balance", 10, ComputeJob.class, null,JobTimeStageType.MINUTE.type());
            if (computeResult)
                log.info("启动状态下全网平衡空算周期启动成功-- ", balanceNet.getName());
            else
                log.info("启动状态下全网平衡空算周期启动失败-- ", balanceNet.getName());
            return Response.success(controlResult);
        } else {
            return Response.fail();
        }

    }

    @GetMapping(value = "stop/{balanceId}")
    public Response stop(@PathVariable int balanceId) {
        return Response.success();
    }

    @ApiModelProperty("关闭热网")
    @GetMapping(value = "delete/{id}")
    public Response delete(@PathVariable int id) {
        BalanceNet balanceNet = balanceNetService.getById(id);
        Boolean controlResult = jobService.deleteJob(balanceNet.getId() + "_" + balanceNet.getName() + "_control", "Balance");
        if (controlResult) {
            balanceNet.setNextControlTime(LocalDateTime.now());
            balanceNet.setStatus(false);
            Boolean result = balanceNetService.updateById(balanceNet);
            if (result) {
                return Response.success();
            } else {
                return Response.fail();
            }

        } else {
            return Response.fail();
        }
    }

//    @ApiModelProperty("测试热网")
//    @GetMapping(value = "netbalanceTest")
//    public Response netbalanceTest() {
//        return balanceService.queryBasic(3, 1);
//    }

    @Autowired
    SignalRTemplate signalRTemplate;

    @GetMapping("test")
    public void test() {
        signalRTemplate.send2AllServerSpecial("Subscribe", new int[]{980});
    }
}
