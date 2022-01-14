package com.bmts.heating.web.balance.service.impl;

import com.bmts.heating.commons.entiy.gathersearch.request.BalanceCurveDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.balance.service.BalanceIndexInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BalanceIndexInfoServiceImpl implements BalanceIndexInfoService {
    @Autowired
    private TSCCRestTemplate tsccRestTemplate;

    private final String applicationNetBalance = "application_netbalance";

    //查询全网平衡温度差距值饼图
    @Override
    public Response selNetBalanceTempGap(int id) {
        return tsccRestTemplate.get("/netbalance/selNetBalanceTempGap/"+id, applicationNetBalance, Response.class);
    }

    //查询全网平衡阀门开度区间柱状图
    @Override
    public Response selNetBalanceValveSection(int id) {
        return tsccRestTemplate.get("/netbalance/selNetBalanceValveSection/"+id, applicationNetBalance, Response.class);
    }

    //查询全网平衡实时曲线
    @Override
    public Response selNetBalanceTargetCurve(BalanceCurveDto balanceCurveDto) {
        return tsccRestTemplate.doHttp("/netbalance/selNetBalanceTargetCurve",balanceCurveDto, applicationNetBalance, Response.class, HttpMethod.POST);
    }
    //启动全网平衡
    @Override
    public Response netBalanceStart(int id) {
        return tsccRestTemplate.get("/job/start/"+id, applicationNetBalance, Response.class);
    }
   //关闭全网平衡
    @Override
    public Response netBalanceStop(int id) {
        return tsccRestTemplate.get("/job/delete/"+id, applicationNetBalance, Response.class);
    }

    @Override
    public Response queryScatterDiagram() {
        return tsccRestTemplate.get("/netbalance/queryScatterDiagram", applicationNetBalance, Response.class);
    }
}
