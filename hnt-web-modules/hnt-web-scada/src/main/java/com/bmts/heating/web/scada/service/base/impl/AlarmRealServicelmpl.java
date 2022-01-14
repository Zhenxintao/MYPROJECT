package com.bmts.heating.web.scada.service.base.impl;
import com.bmts.heating.commons.entiy.baseInfo.request.alarm.AlarmRealBarDto;
import com.bmts.heating.commons.entiy.baseInfo.response.AlarmRealBarResponse;
import com.bmts.heating.commons.entiy.gathersearch.response.charts.BarChartData;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.scada.service.base.AlarmRealService;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AlarmRealServicelmpl implements AlarmRealService {

    @Autowired
    private TSCCRestTemplate template;

    private final String baseServer = "bussiness_baseInfomation";

    /**
     * 获取首页报警实时柱状图数据
     * @return
     */
    @Override
    public Response realAlarmbarIndex() {
        try {
            AlarmRealBarDto alarmRealBarDto =new AlarmRealBarDto();
            alarmRealBarDto.setBarChartCount(10);
            alarmRealBarDto.setSortType("DESC");
            Response response =  template.post("/alarmReal/queryAlarmRealBar", alarmRealBarDto, baseServer, Response.class);
            List<AlarmRealBarResponse> alarmRealBarList = JSON.parseArray(JSON.toJSONString(response.getData()), AlarmRealBarResponse.class);
            BarChartData  barChartData = new BarChartData();
            List<Object> stationNameList = Lists.newArrayList();
            List<Object> stationAlarmList = Lists.newArrayList();
            for (AlarmRealBarResponse obj:alarmRealBarList) {
                stationNameList.add(obj.getStationName());
                stationAlarmList.add(obj.getStationAlarmCount());
            }
            barChartData.setXData(stationNameList);
            barChartData.setYData(stationAlarmList);
            return  Response.success(barChartData);
        }
        catch (Exception e)
        {
            return Response.fail();
        }

    }
}
