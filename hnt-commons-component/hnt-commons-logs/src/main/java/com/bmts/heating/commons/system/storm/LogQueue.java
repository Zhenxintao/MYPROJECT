package com.bmts.heating.commons.system.storm;

import com.bmts.heating.commons.basement.model.db.entity.StormLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author naming
 * @description
 * @date 2021/4/21 17:23
 **/
@Slf4j
@Component
public class LogQueue {
    protected static Queue<StormLog> queueStormLogs = new ConcurrentLinkedQueue<>();


    public static void record(StormLog stormLog) {
        try {
            queueStormLogs.add(stormLog);

        } catch (Exception e) {
            log.error("添加日志到队列失败{}", e.getMessage());
        }
    }
}
