package com.bmts.heating.web.balance.service;

import com.bmts.heating.commons.entiy.gathersearch.request.BalanceCurveDto;
import com.bmts.heating.commons.utils.restful.Response;
import org.springframework.web.bind.annotation.PathVariable;

public interface BalanceIndexInfoService {
    //查询全网平衡温度差距值饼图
    Response selNetBalanceTempGap(int id);
    //查询全网平衡阀门开度区间柱状图
    Response selNetBalanceValveSection(int id);
    //查询全网平衡实时曲线
    Response selNetBalanceTargetCurve(BalanceCurveDto balanceCurveDto);
    //全网平衡启动
    Response netBalanceStart(int id);
   //全网平衡停止
    Response netBalanceStop(int id);
    //查询全网平衡散点图
    Response queryScatterDiagram();
}
