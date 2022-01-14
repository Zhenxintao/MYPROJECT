package com.bmts.heating.monitor.dirver.common;

import com.bmts.heating.commons.utils.container.SpringBeanFactory;
import com.bmts.heating.distribution.adapter.DistributionCenterAdapter;
import com.bmts.heating.monitor.dirver.config.MonitorMuster;
import com.bmts.heating.monitor.dirver.config.MonitorProtery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MonitorTaskUtils {

    @Autowired
    @Qualifier("monitorCenterAdapter")
    private DistributionCenterAdapter governCenterAdapter;

    /**
     * 获取所有任务实例{["JK",{jk集合}],["PVSS",{pvss集合}]}
     *
     * @return
     */
    public List<MonitorProtery> getAllMonitorProtery() {
        return (List<MonitorProtery>) SpringBeanFactory.getBean("MonitorProtery_list");
    }

    /**
     * 获取所有可执行的任务实例{["JK",{jk集合可执行}],["PVSS",{pvss集合可执行}]}
     *
     * @return
     */
    public List<MonitorProtery> getRunMonitorProtery() {
        List<MonitorProtery> mplist = getAllMonitorProtery();
        String monitorpath = SpringBeanFactory.getBean("monitorPath").toString();

        List<MonitorProtery> runList = new ArrayList<MonitorProtery>();
        for (MonitorProtery mp : mplist) {
            //连接consul服务检测可采集服务实例
            // List list = governCenter.getMonitorLiving(monitorpath + "/" + mp.getPattern().getModel(), MonitorMuster.Plugin.class);
            List list = governCenterAdapter.getFiles(monitorpath + "/" + mp.getPattern().getModel(), MonitorMuster.Plugin.class);

            //获取当前服务(JK)下的所有实例(10.0.0.1)
            List<MonitorMuster.Plugin> live_list = mp.getPluginList();
            List<MonitorMuster.Plugin> run_list = new ArrayList<MonitorMuster.Plugin>();//可采集实例
            MonitorProtery mm = new MonitorProtery();
            for (Object object : list) {
                MonitorMuster.Plugin pl = (MonitorMuster.Plugin) object;
                int status = pl.getModel_status();
                //判断consul返回的实例是否可采集
                if (status != 0) {
                    run_list.add(pl);
                }
            }
            mm.setPattern(mp.getPattern());
            mm.setPluginList(run_list);
            runList.add(mm);
        }
        return runList;
    }

    /**
     * 获取所有不可执行的任务实例{["JK",{jk集合不可执行}],["PVSS",{pvss集合不可执行}]}
     *
     * @return
     */
    public List<MonitorProtery> getBanRunMonitorProtery() {
        List<MonitorProtery> mplist = getAllMonitorProtery();
        String monitorpath = SpringBeanFactory.getBean("monitorPath").toString();

        List<MonitorProtery> banRunList = new ArrayList<MonitorProtery>();
        for (MonitorProtery mp : mplist) {
            //连接consul服务检测可采集服务实例
            //List list = governCenter.getMonitorLiving(monitorpath + "/" + mp.getPattern().getModel(), MonitorMuster.Plugin.class);
            List list = governCenterAdapter.getFiles(monitorpath + "/" + mp.getPattern().getModel(), MonitorMuster.Plugin.class);

            //获取当前服务(JK)下的所有实例(10.0.0.1)
            List<MonitorMuster.Plugin> live_list = mp.getPluginList();
            List<MonitorMuster.Plugin> ban_list = new ArrayList<MonitorMuster.Plugin>();//不可采集实例
            MonitorProtery mm = new MonitorProtery();
            for (Object object : list) {
                MonitorMuster.Plugin pl = (MonitorMuster.Plugin) object;
                int status = pl.getModel_status();
                //判断consul返回的实例是否可采集
                if (status == 0) {
                    ban_list.add(pl);
                }
            }
            mm.setPattern(mp.getPattern());
            mm.setPluginList(ban_list);
            banRunList.add(mm);
        }
        return banRunList;
    }

    /**
     * 获取某个采集服务实例(JK/PVSS)下的所有实例[{JK所有集合}/{PVSS所有集合}]
     *
     * @param model
     * @return
     */
    public List<MonitorMuster.Plugin> getAllMonitorPlugin(String model) {
        String monitorpath = SpringBeanFactory.getBean("monitorPath").toString();
        //return governCenter.getMonitorLiving(monitorpath + "/" + model, MonitorMuster.Plugin.class);

        return (List<MonitorMuster.Plugin>) governCenterAdapter.getFiles(monitorpath + "/" + model, MonitorMuster.Plugin.class);
    }

    /**
     * 获取某个采集服务实例(JK/PVSS)下的所有可执行实例[{JK可执行集合}/{PVSS可执行集合}]
     *
     * @param model
     * @return
     */
    public List<MonitorMuster.Plugin> getRunMonitorPlugin(String model) {
        String monitorpath = SpringBeanFactory.getBean("monitorPath").toString();
        //List<MonitorMuster.Plugin> list =governCenter.getMonitorLiving(monitorpath + "/" + model, MonitorMuster.Plugin.class);
        List<MonitorMuster.Plugin> list = (List<MonitorMuster.Plugin>) governCenterAdapter.getFiles(monitorpath + "/" + model, MonitorMuster.Plugin.class);

        for (MonitorMuster.Plugin pl : list) {
            if (pl.getModel_status() == 0)
                list.remove(pl);
        }
        return list;
    }

    /**
     * 获取某个采集服务实例(JK/PVSS)下的所有不可执行实例[{JK不可执行集合}/{PVSS不可执行集合}]
     *
     * @param model
     * @return
     */
    public List<MonitorMuster.Plugin> getBanRunMonitorPlugin(String model) {
        String monitorpath = SpringBeanFactory.getBean("monitorPath").toString();
        //List<MonitorMuster.Plugin> list =governCenter.getMonitorLiving(monitorpath + "/" + model, MonitorMuster.Plugin.class);
        List<MonitorMuster.Plugin> list = (List<MonitorMuster.Plugin>) governCenterAdapter.getFiles(monitorpath + "/" + model, MonitorMuster.Plugin.class);

        List<MonitorMuster.Plugin> blist = new ArrayList<MonitorMuster.Plugin>();
        for (MonitorMuster.Plugin pl : list) {
            if (pl.getModel_status() == 0)
                blist.add(pl);
        }
        return blist;
    }
}
