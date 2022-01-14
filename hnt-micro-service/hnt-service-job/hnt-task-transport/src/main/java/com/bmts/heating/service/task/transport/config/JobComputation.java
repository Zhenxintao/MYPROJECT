package com.bmts.heating.service.task.transport.config;

import com.bmts.heating.commons.utils.msmq.PointL;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @ClassName: Computation
 * @Description: 加载计算量
 * @Author: pxf
 * @Date: 2021/4/20 19:51
 * @Version: 1.0
 */
@Component
public class JobComputation {

    public static Map<Integer, List<PointL>> conMap = new HashMap<Integer, List<PointL>>();


    public static ConcurrentHashMap<Integer, PointL> eneryMap = new ConcurrentHashMap<>();


    public void setMap(List<PointL> list) {
        if (conMap.size() > 0) {
            conMap.clear();
        }
        Map<Integer, List<PointL>> collect = list.stream().collect(Collectors.groupingBy(e -> e.getDataType() + e.getLevel() + e.getRelevanceId()));
        conMap.putAll(collect);
    }

    public List<PointL> getMap(Integer key) {
        if (key != null) {
            List<PointL> pointLS = conMap.get(key);
            return pointLS;
        }
        return Collections.emptyList();
    }


    public void setEneryMap(List<PointL> list) {
        if (!CollectionUtils.isEmpty(list)) {
            list.stream().forEach(e -> {
                if (e != null && e.getPointId() != null) {
                    eneryMap.put(e.getPointId(), e);
                }
            });
        }
    }

    public PointL getEneryMap(Integer key) {
        if (key != null) {
            return eneryMap.get(key);
        }
        return null;
    }

}
