package com.bmts.heating.monitor.dirver.adapter;

import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.monitor.dirver.config.MonitorProtery;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ConstructionGenerator {

    public void workGather();    //开启采集工作进程
    public void init(MonitorProtery monitorProtery); //初始化任务
    public void workIssue();    //开启下发工作进程

    public List<PointL> handIssue(Object... objects) throws ExecutionException, InterruptedException;    //手动下发任务
}
