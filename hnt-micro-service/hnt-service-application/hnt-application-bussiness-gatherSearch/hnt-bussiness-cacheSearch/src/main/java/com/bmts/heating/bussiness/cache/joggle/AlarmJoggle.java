package com.bmts.heating.bussiness.cache.joggle;

import com.bmts.heating.commons.redis.service.RedisAlarmSubUsersService;
import com.bmts.heating.commons.utils.restful.Response;
//import com.bmts.heating.middleware.el.HistoryTdClient;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author naming
 * @description
 * @date 2021/1/12 17:40
 **/
@Api(tags = "报警")
@RequestMapping("alarm")
@RestController
public class AlarmJoggle {
    @Autowired
    RedisAlarmSubUsersService redisAlarmSubUsersService;

    @GetMapping("/queryUserAlarm/{id}")
    public Response queryUserAlarm(@PathVariable int id) {
        return Response.success(redisAlarmSubUsersService.queryUser(id));
    }

    @GetMapping("/userAlarm/{userid}/{status}")
    public Response setAlarm(@PathVariable boolean status, @PathVariable int userid) {
        return Response.success(redisAlarmSubUsersService.set(userid, status));
    }

//    @Autowired
//    HistoryTdClient historyTdClient;
//
//    @GetMapping("/test1")
//    public Response test() {
//        historyTdClient.send();
//        return Response.success();
//    }
}
