package com.bmts.heating.monitor.dirver.common;

import com.bmts.heating.commons.utils.msmq.PointL;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: ReloadComputation
 * @Description: 线程信息常量
 * @Author: pxf
 * @Date: 2021/4/20 19:51
 * @Version: 1.0
 */
@Component
public class ReloadComputation {

    public static Map<String, Thread> threadMap = new ConcurrentHashMap<String, Thread>();

    public static ConcurrentHashMap<String, List<List<PointL>>> monitorMap = new ConcurrentHashMap<>();


    public void setThreadMap(String threadName, Thread thread) {
        threadMap.put(threadName, thread);
    }

    public void clearThreadMap() {
        threadMap.clear();
    }

    public Map<String, Thread> getThreadMap() {
        return threadMap;
    }


    public void setMonitorMap(String key, List<List<PointL>> list) {
        if (StringUtils.isNotBlank(key)) {
            monitorMap.put(key, list);
        }
    }

    public List<List<PointL>> getMonitorMap(String key) {
        return monitorMap.get(key);
    }

}
