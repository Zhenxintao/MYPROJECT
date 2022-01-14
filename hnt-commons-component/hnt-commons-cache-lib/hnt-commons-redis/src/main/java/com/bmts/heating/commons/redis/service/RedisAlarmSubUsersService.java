package com.bmts.heating.commons.redis.service;

import com.bmts.heating.commons.redis.utils.RedisKeyConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author naming
 * @description
 * @date 2021/1/12 17:29
 **/
@Service
@Slf4j
public class RedisAlarmSubUsersService {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 查询用户是否为禁用报警状态
     *
     * @param userId
     * @return true 当前为禁用报警状态  false 为启用报警状态
     */
    public boolean queryUser(int userId) {
        return redisTemplate.opsForSet().isMember(RedisKeyConst.UN_ALARM_USERS, userId);
    }
    public Set<Integer> queryForbiddenUsers()
    {
        Set<Object> members = redisTemplate.opsForSet().members(RedisKeyConst.UN_ALARM_USERS);
        return members.stream().map(x->Integer.valueOf(x.toString())).collect(Collectors.toSet());
    }
    /**
     * 是否开启
     *
     * @param userId
     * @param forbidden 是否禁用
     */
    public boolean set(int userId, boolean forbidden) {
        try {
            if (forbidden) {
                Long add = redisTemplate.opsForSet().add(RedisKeyConst.UN_ALARM_USERS, userId);
            } else
                redisTemplate.opsForSet().remove(RedisKeyConst.UN_ALARM_USERS, userId);
            return true;
        } catch (Exception e) {
            log.error("set user alarm status caused exception {}", e);
            return false;
        }

    }
}
