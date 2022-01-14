package com.bmts.heating.bussiness.baseInformation.app.handler;

import com.bmts.heating.commons.basement.model.db.entity.EsDocConfig;
import com.bmts.heating.commons.basement.model.db.entity.PointConfig;
import com.bmts.heating.commons.db.service.EsDocConfigService;
import com.bmts.heating.commons.db.service.PointCollectConfigService;
import com.bmts.heating.commons.db.service.PointConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * mapping配置类
 **/
@AutoConfigureAfter()
@Configuration
@Service
public class TempCollectServer {

    @Autowired
    private EsDocConfigService configService;

    @Autowired
    private EsHandler esHandler;

//    @Bean
    public void onApplicationEvent() {
        List<EsDocConfig> list = configService.list();
        list.forEach(e -> {
            esHandler.configColumn(e.getPointName(),e.getDataType());
        });
    }
}
