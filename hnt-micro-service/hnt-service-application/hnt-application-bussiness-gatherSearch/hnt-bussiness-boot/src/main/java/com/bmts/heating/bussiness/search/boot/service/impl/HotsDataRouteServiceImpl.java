package com.bmts.heating.bussiness.search.boot.service.impl;

import com.bmts.heating.bussiness.search.boot.service.BaseDataRouteService;
import com.bmts.heating.commons.entiy.gathersearch.request.DataRouteParam;
import com.bmts.heating.commons.utils.restful.Response;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author fei.chang
 * @Date 2020/9/14 16:40
 * @Version 1.0
 */
@Service
public class HotsDataRouteServiceImpl extends BaseDataRouteService {

    private long time = 1000 * 60 * 60 * 2;

    @Override
    public boolean rule(DataRouteParam param) {
        Long startTime = param.getStartTime();
        Long endTime = param.getEndTime();
        if (startTime != null && endTime != null) {
            if (endTime - startTime < time) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Response handler(DataRouteParam param) {
        System.out.println("2小时内业务逻辑");
        return null;
    }

}
