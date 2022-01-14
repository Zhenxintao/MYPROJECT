package com.bmts.heating.monitor.dirver.adapter;

import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.monitor.dirver.config.MonitorMuster;
import com.bmts.heating.monitor.dirver.config.MonitorType;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface HandlerGenerator {

    public void workGahter(MonitorType.Pattern pattern);

    public void setWork_plugin(MonitorMuster.Plugin plugin);

    public void setWork_pluginList(List<MonitorMuster.Plugin> pluginList);

    public List<List<PointL>> getTaskForCache(MonitorMuster.Plugin plugin, int casenum);

    public List<PointL> workIssue(MonitorType.Pattern pattern, List<PointL> pointLS) throws ExecutionException, InterruptedException;
}
