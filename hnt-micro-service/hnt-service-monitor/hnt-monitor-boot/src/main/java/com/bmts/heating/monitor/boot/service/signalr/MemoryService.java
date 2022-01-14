package com.bmts.heating.monitor.boot.service.signalr;

import com.bmts.heating.commons.auth.entity.response.UserDataPerms;
import com.bmts.heating.commons.auth.service.AuthrityService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@EnableScheduling
@Slf4j
public class MemoryService  {
    @Autowired
    AuthrityService authrityService;

    public static List<UserDataPerms> userDataPerms=new ArrayList<>();

    @Scheduled(fixedRate = 1000 * 60 * 5)
    private void loadUserPerms() {
        log.info("周期加载用户站权限开始执行*************************************************");
        List<UserDataPerms> userDataPerms = authrityService.queryStationsByAllUsers();
        this.userDataPerms = userDataPerms;
        log.info("周期加载用户站权限结束*************************************************");
    }
}
