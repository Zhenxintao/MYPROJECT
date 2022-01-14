package com.bmts.heating.monitor.plugins.second.joggle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bmts.heating.commons.entiy.second.request.device.PointSecondDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.monitor.plugins.second.pojo.MongoInserDto;
import com.bmts.heating.monitor.plugins.second.pojo.MongoRoomDto;
import com.bmts.heating.monitor.plugins.second.service.MongoService;
import com.bmts.heating.monitor.plugins.second.service.MonitorSecondService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: MongoDBJoggle
 * @Description: 测试查询MongDB
 * @Author: pxf
 * @Date: 2022/1/5 16:19
 * @Version: 1.0
 */
@Slf4j
@Api(tags = "处理二网数据接口")
@RestController
@RequestMapping("/second")
public class MonitorSecondJoggle {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MonitorSecondService monitorSecondService;

    @Autowired
    private MongoService mongoService;

    @PostMapping("/receive")
    @ApiOperation("接收数据")
    public Response receive(@RequestBody List<PointSecondDto> list) {
        // 进行查询，根据 deviceCode 进行查询 根据视图进行查询得出
        // devices.rt.deviceCode="30"


        monitorSecondService.updatList(list);
        return Response.success();
    }

    @GetMapping("/test")
    @ApiOperation("查询数据")
    public Response query() {
        List<MongoRoomDto> all = mongoTemplate.findAll(MongoRoomDto.class);
        all.stream().forEach(e -> {
            Object devices = e.getDevices();
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(devices));
            Set<String> sets = jsonObject.keySet();
            sets.stream().forEach(x -> {
                String x1 = x;
                Object o = jsonObject.get(x);

            });

        });
        return Response.success();
    }

    private final static AtomicInteger num = new AtomicInteger(1);

    @GetMapping("/add")
    @ApiOperation("查询数据")
    public Response add() {

        Integer index = 1;

        // 2个亿数据
        for (int i = 0; i < 40000; i++) {


            List<MongoInserDto> list = new ArrayList<>();
            for (int j = 1; j < 5001; j++) {
                MongoInserDto dto = new MongoInserDto();
                dto.setDeviceCode(String.valueOf(num.get()));
                dto.setGropuId(String.valueOf(new Random().nextInt(999999)));
                long timeStamp = System.currentTimeMillis();
                dto.setTimeStamp(timeStamp);

                // 组装数据
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("time", timeStamp);
                jsonObject.put("value", new Random().nextInt(100));

                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("time", timeStamp);
                jsonObject1.put("value", new Random().nextInt(100));


                JSONObject jsonObjectadd2 = new JSONObject();
                jsonObjectadd2.put("timeStamp", timeStamp);
                jsonObjectadd2.put("ph", jsonObject1);
                jsonObjectadd2.put("tg", jsonObject);

                dto.setRealData(jsonObjectadd2);
                list.add(dto);
                index++;
                num.set(index);
            }

            mongoTemplate.insert(list, MongoInserDto.class);

        }

        return Response.success();
    }

    @GetMapping("/all")
    @ApiOperation("查询所有数据")
    public Response queryAll() {
        long startTime = System.currentTimeMillis();
        List<MongoInserDto> all = mongoTemplate.findAll(MongoInserDto.class);
        //System.out.println("查询全部耗时：---" + (System.currentTimeMillis() - startTime));

        return Response.success();
    }

    @GetMapping("/one")
    @ApiOperation("查询单个数据")
    public Response queryOne() {
        long startTime = System.currentTimeMillis();
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is("61d810e24d098555bf86f5e0"));
        MongoInserDto one = mongoTemplate.findOne(query, MongoInserDto.class);
        System.out.println("查询单个耗时：---" + (System.currentTimeMillis() - startTime));

        long endTime = System.currentTimeMillis();
        MongoInserDto byId = mongoTemplate.findById("61d810e24d098555bf86f5e0", MongoInserDto.class);
        System.out.println("byId查询耗时：---" + (System.currentTimeMillis() - endTime));
        // 按条件查询
        long endTime2 = System.currentTimeMillis();
        Query queryList = new Query();
        queryList.addCriteria(Criteria.where("gropuId").is("720581"));
        List<MongoInserDto> mongoInserDtos = mongoTemplate.find(queryList, MongoInserDto.class);
        System.out.println("按条件查询耗时：---" + (System.currentTimeMillis() - endTime2));

        // 按条件查询
        long endTime3 = System.currentTimeMillis();
        Query queryList3 = new Query();
        queryList3.addCriteria(Criteria.where("realData.tg.value").is("81"));
        mongoTemplate.find(queryList3, MongoInserDto.class);
        System.out.println("按条件查询耗时：---" + (System.currentTimeMillis() - endTime3));

        return Response.success();
    }

    @GetMapping("/insertTdTest")
    @ApiOperation("测试插入历史数据")
    public Response insertTdTest() {
        return Response.success(mongoService.insertTdHistory());
    }

}
