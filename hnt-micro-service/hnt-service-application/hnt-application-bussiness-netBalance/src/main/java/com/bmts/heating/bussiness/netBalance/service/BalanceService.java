package com.bmts.heating.bussiness.netBalance.service;

import com.bmts.heating.commons.basement.model.cache.PointCache;
import com.bmts.heating.commons.basement.model.db.entity.BalanceNet;
import com.bmts.heating.commons.basement.model.db.entity.BalanceNetControlLog;
import com.bmts.heating.commons.basement.model.db.entity.BalanceRejectHistory;
import com.bmts.heating.commons.basement.model.db.entity.BalanceTargetHistory;
import com.bmts.heating.commons.entiy.balance.pojo.BalanceBasicVo;
import com.bmts.heating.commons.entiy.balance.pojo.ComputDo;
import com.bmts.heating.commons.entiy.balance.pojo.ComputeVo;
import com.bmts.heating.commons.utils.msmq.Message_Point_Issue;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.middleware.monitor.pojo.MonitorGrpcResult;

import java.awt.*;
import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/1/29 11:33
 **/
public interface BalanceService {


    Response start(int balanceId, int controlType);

    /**
     * 第一步 查询全网平衡db数据
     *
     * @param balanceId 全网平衡Id
     * @return
     */
    Response queryBasic(int balanceId, int controlType, int dataSourceType);

    /**
     * 第二步 调用计算服务(平台自用)
     *
     * @param dos
     * @return
     */
    void callCompute(ComputDo dos, int balanceId, int controlType, List<PointCache> pointCacheList, BalanceNet balanceNet);

    /**
     * 第二步 调用计算服务(pvss数据同步)
     *
     * @param
     * @return
     */
    void callCompute(int balanceId, int controlType, BalanceNet balanceNet);

    /**
     * 记录剔除日志
     */
    void recordRejectHistory(List<BalanceRejectHistory> balanceRejectHistoriesList);

    void recordBalanceTargetHistory(BalanceTargetHistory balanceTargetHistoriesList);

    /**
     * 第三步 调用grpc 服务下发
     *
     * @param pointLList
     * @return
     */
    void control(List<PointL> pointLList, int balanceId);

    void recordControlLog(List<BalanceNetControlLog> logsList);
}
