package com.bmts.heating.monitor.plugins.jk.issue;

import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.monitor.dirver.config.MonitorMuster;
import com.bmts.heating.monitor.plugins.jk.constructors.jkUtil.JKValueTwo;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JKWorkAware {


    public static List<PointL> issueWorker(MonitorMuster.Plugin plugin, List<PointL> taskArray) throws IOException {
        List<PointL> listReturn = new ArrayList<>();
        // 下发
        Socket client = new Socket(plugin.getModel_host(), plugin.getModel_port());
        log.info("JKWorkAware---下发任务---连接地址：--{}", plugin.getModel_host() + ":" + plugin.getModel_port());
        OutputStream ous = client.getOutputStream();
        InputStream ins = client.getInputStream();

        // JKValue.putJKValue(taskArray, ous, ins);//遍历请求，每个点都做请求
        JKValueTwo.putValue(taskArray, ous, ins);//bool类型统一请求修改
        ous.close();
        ins.close();
        client.close();


        return listReturn;
    }

}
