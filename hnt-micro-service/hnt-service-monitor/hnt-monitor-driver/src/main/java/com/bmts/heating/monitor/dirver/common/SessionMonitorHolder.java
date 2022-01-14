package com.bmts.heating.monitor.dirver.common;

import com.bmts.heating.monitor.dirver.config.MonitorMuster;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class SessionMonitorHolder {

    private static final Map<String, MonitorMuster.Plugin> SESSION_MAP = new ConcurrentHashMap<>(16);

    public static void saveSession(String model_host,MonitorMuster.Plugin plugin){
        SESSION_MAP.put(model_host, plugin);
    }

    public static void removeSession(String model_host){
        SESSION_MAP.remove(model_host) ;
    }

    public static MonitorMuster.Plugin get(String model_host){
        return SESSION_MAP.get(model_host);
    }

//    public static void remove(NioSocketChannel nioSocketChannel) {
//        CHANNEL_MAP.entrySet().stream().filter(entry -> entry.getValue() == nioSocketChannel).forEach(entry -> CHANNEL_MAP.remove(entry.getKey()));
//    }

    /**
     * 获取注册的客户端信息
     * @param nioSocketChannel
     * @return
     */
//    public static Heart_Message getClientInfo(NioSocketChannel nioSocketChannel){
//        for (Map.Entry<Long, NioSocketChannel> entry : CHANNEL_MAP.entrySet()) {
//            NioSocketChannel value = entry.getValue();
//            if (nioSocketChannel == value){
//                Long spring_application_id = entry.getKey();
//                return SESSION_MAP.get(spring_application_id);
//            }
//        }
//        return null;
//    }
}
