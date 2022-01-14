package com.bmts.heating.monitor.dirver.handler;

import com.bmts.heating.commons.utils.msmq.PointL;

import java.util.List;

public class RunWorkEntranceImpl implements Runnable{

    private IWorkEntrance iWorkEntrance;
    private List<PointL> taskArray;
    private int flag;

    public RunWorkEntranceImpl(IWorkEntrance iWorkEntrance, List<PointL> taskArray, int flag){
        this.iWorkEntrance=iWorkEntrance;
        this.taskArray=taskArray;
        this.flag=flag;
    }

    @Override
    public void run() {
        iWorkEntrance.invoke(taskArray, flag);
    }
}
