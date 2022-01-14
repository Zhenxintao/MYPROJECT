package com.bmts.heating.commons.system.storm;

import com.bmts.heating.commons.basement.model.db.entity.StormLog;
import com.bmts.heating.commons.db.service.StormLogService;
import com.bmts.heating.commons.utils.container.SpringBeanFactory;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author naming
 * @description
 * @date 2021/5/17 10:41
 **/
@Component
public class StormLogConsumer implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

//        LogQueue logQueue = contextRefreshedEvent.getApplicationContext().getBean(LogQueue.class);
        for (int i = 0; i < 4; i++) {
            new Thread(new StormLogThread()).start();
        }
//        for (int i = 0; i < 10000; i++) {
//            StormLog log = new StormLog();
//            log.setType(1);
//            log.setNode("node" + 1);
//            log.setLot("test");
//            log.setCreateTime(LocalDateTime.now());
//            log.setDuration(new Long(i));
//            log.setTimestamp(new Long(i));
//            logQueue.record(log);
//        }

    }

    public class StormLogThread implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                StormLog poll = LogQueue.queueStormLogs.poll();
                if (poll == null) {
                    Thread.sleep(500);
                    continue;
                }
                try {
//                    System.out.print("开始消费");
                    StormLogService bean = SpringBeanFactory.getBean(StormLogService.class);
                    bean.save(poll);
                } catch (Exception e) {
                    e.printStackTrace();
//                    System.out.print("消费出错".concat(e.getMessage()));
                }
            }
        }
    }
}
