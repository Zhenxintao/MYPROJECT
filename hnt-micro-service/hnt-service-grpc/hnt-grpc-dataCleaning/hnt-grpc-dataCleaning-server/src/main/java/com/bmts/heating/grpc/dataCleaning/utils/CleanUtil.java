package com.bmts.heating.grpc.dataCleaning.utils;

import com.bmts.heating.grpc.dataCleaning.service.strategy.DataCleanStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CleanUtil {

    /**
     * 清洗策略类型对应相关执行类
     */
    public static Map<Integer, DataCleanStrategy<?>> cleanMap = new HashMap<>();

    public static void addCleanStrategy(Integer cleanType, DataCleanStrategy dataCleanStrategy) {
        cleanMap.put(cleanType, dataCleanStrategy);
    }

    public static DataCleanStrategy getCleanStrategy(Integer cleanType) {
        return cleanMap.get(cleanType);
    }
}
