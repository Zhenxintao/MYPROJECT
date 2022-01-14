package com.bmts.heating.web.scada.service.base;

import com.bmts.heating.commons.entiy.gathersearch.response.charts.BarChartData;
import com.bmts.heating.commons.utils.restful.Response;

import java.util.List;

public interface AlarmRealService {
    //获取首页报警实时柱状图数据接口
    Response realAlarmbarIndex();
}
