package com.bmts.heating.monitor.plugins.pvss.issue;

import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.monitor.dirver.config.MonitorMuster;
import com.bmts.heating.monitor.plugins.pvss.constructors.pvssUtil.PassValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PvssWorkAware {

    public static List<PointL> issueWorker(MonitorMuster.Plugin plugin, List<PointL> taskArray) {
        List<PointL> listReturn = new ArrayList<>();
        if (!CollectionUtils.isEmpty(taskArray)) {
            listReturn = PassValue.downHairValue(taskArray, plugin.getIssue_url());//下发接口调用
            log.info("PvssWorkAware---任务下发失败的点集合长度:---" + listReturn.size());
        }
        return listReturn;
    }

}
