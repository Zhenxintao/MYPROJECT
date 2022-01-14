package com.bmts.heating.commons.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RedisSignalrService {
    @Autowired
    SetOperations<String, Object> setOperations;

    public List<Integer> queryOnlineUsers() {
        Set<Object> members = setOperations.members("tscc:signalr:user");
        return members.stream().map(x -> Integer.valueOf(x.toString())).collect(Collectors.toList());
    }
}
