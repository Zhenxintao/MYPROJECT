package com.bmts.heating.monitor.dirver.handler;

import com.bmts.heating.commons.utils.msmq.PointL;

import java.util.List;

public interface IWorkEntrance {

    public void invoke(List<PointL> taskArray, int flag);	//无返回值调用

    public Object invokeForBack(List<PointL> taskArray, int flag);	//返回值调用
    public abstract void config(Object... objs);
    public int getGather_count();
}
