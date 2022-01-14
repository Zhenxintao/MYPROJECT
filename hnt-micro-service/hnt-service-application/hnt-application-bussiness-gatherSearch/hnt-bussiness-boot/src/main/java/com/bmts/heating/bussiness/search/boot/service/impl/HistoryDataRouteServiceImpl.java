package com.bmts.heating.bussiness.search.boot.service.impl;

import com.bmts.heating.bussiness.search.boot.service.BaseDataRouteService;
import com.bmts.heating.commons.entiy.gathersearch.request.DataRouteParam;
import com.bmts.heating.commons.utils.restful.Response;
import org.springframework.stereotype.Service;

/**
 * @Description 历史数据业务逻辑
 * @Author fei.chang
 * @Date 2020/9/10 16:36
 * @Version 1.0
 */
@Service
public class HistoryDataRouteServiceImpl extends BaseDataRouteService {

    private final long time = 1000 * 60 * 60 * 2;

    @Override
    public boolean rule(DataRouteParam param) {
        Long startTime = param.getStartTime();
        Long endTime = param.getEndTime();
        if (startTime != null && endTime != null) {
            return endTime - startTime >= time;
        }
        return false;
    }

    @Override
    public Response handler(DataRouteParam param) {
        return null;
    }


}
