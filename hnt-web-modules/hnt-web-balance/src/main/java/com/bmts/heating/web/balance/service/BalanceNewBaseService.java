package com.bmts.heating.web.balance.service;

import com.bmts.heating.commons.basement.model.db.entity.LogOperationBalance;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.BalanceNetAddDto;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.BalanceNetSystemListDto;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.BalanceTableQueryDto;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.basement.BalanceLogDto;
import com.bmts.heating.commons.entiy.baseInfo.request.monitor.MonitorDto;
import com.bmts.heating.commons.utils.restful.Response;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface BalanceNewBaseService {

    Response insert(BalanceNetAddDto balanceNetAddDto);

    Response delete(Integer id);

    Response query(int id);

    Response query(Integer userId);

    Response querySystemList(BalanceNetSystemListDto dto);

    Map<String, Object> table(BalanceTableQueryDto dto);

    //获取全网平衡基础信息
    Response selNetBalanceInfo(int id);

    Response list();

    Response insertLog(LogOperationBalance dto);

    Response insertLogById(BalanceLogDto dto);

    Response insertLogByIds(BalanceLogDto dto);
}
