package com.bmts.heating.grpc.dataCleaning.app;

import com.bmts.heating.commons.basement.model.cache.PointUnitAndParamTypeResponse;
import com.bmts.heating.commons.redis.service.RedisPointService;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.middleware.cache.services.RedisCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("app")
public class TestRedisController {

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private RedisPointService redisPointService;

    @GetMapping("/redis")
    public Response redis() {
        /// int relevanceId, String pointName, int level
        PointUnitAndParamTypeResponse ph = redisPointService.querySingle(564, "Ph", 1);
        PointUnitAndParamTypeResponse ph1 = redisPointService.querySingle(567, "Pg", 1);
        PointUnitAndParamTypeResponse ph2 = redisPointService.querySingle(227, "Pg", 1);

        return Response.success(ph);
    }

    @GetMapping("/test")
    public List<PointL> test(@RequestParam String device) {
        //获取点表信息
        try {
//            String deviceId = "pvss-device-1";
            List<PointL> pointLS = redisCacheService.getPoints(device);

            return pointLS;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用Grpc 服务获取点表数据异常 -----  { }", e);
        }
        return new ArrayList<PointL>();

//        return " kafka 消息发送成功：  ====Success!====" + true;

//        return "服务部署状态：  ====Success!====" + true;
    }


    @GetMapping("/all")
    public List<PointL> all() {
        //获取点表信息
        try {
            String deviceId1 = "pvss-device-1";
            List<PointL> pointList = new ArrayList<>();
            List<PointL> pointLS1 = redisCacheService.getPoints(deviceId1);
            String deviceId2 = "pvss-device-2";
            List<PointL> pointLS2 = redisCacheService.getPoints(deviceId2);
            pointList.addAll(pointLS1);
            pointList.addAll(pointLS2);

            Set<Integer> setP = new HashSet<>();
            for (PointL pl : pointList) {
                if (setP.contains(pl.getPointId())) {
                    System.out.println("重复的point " + pl.toString());
                } else {
                    setP.add(pl.getPointId());
                }
            }

            Map<Integer, List<PointL>> collect1 = pointList.stream().collect(Collectors.groupingBy(e -> e.getLevel() + e.getRelevanceId()));

            return pointList;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用Grpc 服务获取点表数据异常 -----  { }", e);
        }
        return new ArrayList<PointL>();

//        return " kafka 消息发送成功：  ====Success!====" + true;

//        return "服务部署状态：  ====Success!====" + true;
    }


    @GetMapping("/id")
    public List<Integer> testId(@RequestParam String device) {
        //获取点表信息
        try {
            List<PointL> pointLS = redisCacheService.getPoints(device);
            List<Integer> collect = pointLS.stream().map(p -> p.getPointId()).collect(Collectors.toList());
            System.out.println("------id 集合长度-----------------    " + collect.size());

            return collect;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用Grpc 服务获取点表数据异常 -----  { }", e);
        }
        return new ArrayList<Integer>();
    }

    @GetMapping("/query")
    public List<PointL> query() {
        //获取点表信息
        try {
            return redisCacheService.queryComputePoints();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用Grpc 服务获取点表数据异常 -----  { }", e);
        }
        return new ArrayList<PointL>();

//        return " kafka 消息发送成功：  ====Success!====" + true;

//        return "服务部署状态：  ====Success!====" + true;
    }


}
