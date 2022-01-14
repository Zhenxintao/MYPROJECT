package com.bmts.heating.monitor.boot.service.signalr;

import com.bmts.heating.commons.auth.entity.response.UserDataPerms;
import com.bmts.heating.commons.container.signalr.service.SignalRTemplate;
import com.bmts.heating.commons.redis.service.RedisAlarmSubUsersService;
import com.bmts.heating.commons.redis.service.RedisSignalrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PushAlarmToWeb {
    @Autowired
    RedisSignalrService redisSignalrService;
    @Autowired
    MemoryService memoryService;
    @Autowired
    RedisAlarmSubUsersService redisAlarmSubUsersService;
    @Autowired
    SignalRTemplate signalRTemplate;

    /**
     * 发送报警数据
     *
     * @param stationId 站点id
     * @param content   推送内容
     */
    public void send(Integer stationId, String content) {
        log.info("接收到推送消息指令，站点id{},推送内容{}", stationId, content);
        List<Integer> onlineUsers = redisSignalrService.queryOnlineUsers();
        Set<Integer> integers = redisAlarmSubUsersService.queryForbiddenUsers();
        //删除禁用报警的用户
        onlineUsers.removeAll(integers);
        //判断点报警该推给哪些在线用户
        List<UserDataPerms> collectUser = memoryService.userDataPerms.stream().filter(e -> e.getStations().contains(stationId)).collect(Collectors.toList());
        // 给有权限的用户推送
        collectUser.stream().forEach(x -> {
            if (onlineUsers.contains(x.getUserId())) {
                //在线 推送memoryService.userDataPerms = {LinkedList@18864}  size = 14
                signalRTemplate.send2AllServerSpecial("Alarm", x.getUserId() + "", content);
                log.info("发送报警 用户id：{}，数据：{}", x.getUserId(), content);
            }
        });
        if (onlineUsers.contains(-1)) {
            signalRTemplate.send2AllServerSpecial("Alarm", "-1" + "", content);
            log.info("发送报警 用户id：{}，数据：{}", "管理员", content);
        }


    }

    /**
     * 发送报警数据
     *
     * @param sourceId 热源id
     * @param content  推送内容
     */
    public void sendSource(Integer sourceId, String content) {
        log.info("接收到推送消息指令，热源id{},推送内容{}", sourceId, content);
        List<Integer> onlineUsers = redisSignalrService.queryOnlineUsers();
        Set<Integer> integers = redisAlarmSubUsersService.queryForbiddenUsers();
        //删除禁用报警的用户
        onlineUsers.removeAll(integers);
        //判断点报警该推给哪些在线用户
        List<UserDataPerms> collectUser = memoryService.userDataPerms.stream().filter(e -> e.getStations().contains(sourceId)).collect(Collectors.toList());

        //判断点报警该推给哪些在线用户
        collectUser.stream().forEach(x -> {
            if (onlineUsers.contains(x.getUserId())) {
                //在线 推送
                signalRTemplate.send2AllServerSpecial("Alarm", x.getUserId() + "", content);
                log.info("发送报警 用户id：{}，数据：{}", x.getUserId(), content);
            }
        });
        if (onlineUsers.contains(-1)) {

            signalRTemplate.send2AllServerSpecial("Alarm", "-1" + "", content);
            log.info("发送报警 用户id：{}，数据：{}", "管理员", content);
        }


    }
}
