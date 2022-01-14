package com.bmts.heating.grpc.dataCleaning.service.strategy;

import com.bmts.heating.commons.utils.msmq.PointL;

/**
 * 清洗策略
 *
 * @param <T>
 */
public interface DataCleanStrategy<T> {

    // 数据清洗策略
    T dataClean(PointL pointL);
}
