package com.bmts.heating.web.auth.controller;

import com.bmts.heating.commons.auth.service.AuthrityService;
import com.bmts.heating.commons.basement.model.db.entity.StormLog;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.db.mapper.HeatSystemMapper;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
//import com.bmts.heating.commons.system.storm.LogQueue;
//import com.bmts.heating.commons.system.storm.LogQueue;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/auth")
@Api(tags = "获取用户权限")
public class AuthController extends SavantServices {


    @Autowired
    private AuthrityService authrityService;
    @Autowired
    HeatSystemMapper heatSystemMapper;

    @GetMapping("/authControl/{stationId}")
    @ApiOperation("获取下发控制权限")
    public Response authControl(@PathVariable int stationId, HttpServletRequest request) {
        if (!authrityService.queryStationPerm(JwtUtils.getUserId(request), stationId))
            return Response.success(false);
        return Response.success(true);
    }

////    @Autowired
////    LogQueue logQueue;
//
//    @GetMapping
//    @PassToken
//    public void test() {
////        StormLog stormLog = new StormLog();
////        stormLog.setCreateTime(LocalDateTime.now());
////        stormLog.setDescription("测试" + 1);
////        stormLog.setLot(2 + "");
////        stormLog.setDuration(1);
////        stormLog.setNode("节点" + 2);
////        stormLog.setTimestamp(LocalDateTime.now().getSecond());
////        stormLog.setType(1);
////        logQueue.record(stormLog);
//        for (int i = 0; i < 5; i++) {
//            new Thread(new testTread()).start();
//        }
//    }
//
//    class testTread implements Runnable {
//
//        @SneakyThrows
//        @Override
//        public void run() {
//            long lot = System.currentTimeMillis();
//            for (int i = 0; i < 10000; i++) {
//                StormLog stormLog = new StormLog();
//                stormLog.setCreateTime(LocalDateTime.now());
//                stormLog.setDescription("测试" + i);
//                stormLog.setLot(lot + "");
//                stormLog.setDuration(i);
//                stormLog.setNode("节点" + i);
//                stormLog.setTimestamp(LocalDateTime.now().getSecond());
//                stormLog.setType(1);
//                logQueue.record(stormLog);
//            }
//        }
//    }
}
