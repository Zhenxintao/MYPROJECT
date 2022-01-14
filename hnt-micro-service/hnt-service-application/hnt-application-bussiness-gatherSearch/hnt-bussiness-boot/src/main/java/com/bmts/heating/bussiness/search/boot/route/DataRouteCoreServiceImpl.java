package com.bmts.heating.bussiness.search.boot.route;

import com.bmts.heating.bussiness.search.boot.service.BaseDataRouteService;
import com.bmts.heating.commons.entiy.gathersearch.request.DataRouteParam;
import com.bmts.heating.commons.utils.restful.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description 数据路由核心业务类
 * @Author fei.chang
 * @Date 2020/9/14 16:25
 * @Version 1.0
 */
@Slf4j
@Service
public class DataRouteCoreServiceImpl implements IDataRouteCoreService {

    @Autowired
    private List<BaseDataRouteService> dataRouteServices;

    @Override
    public Response dataRoute(DataRouteParam param) {
        Response result = null;
        try {
            // 1.验证参数
            if (!valid(param)) {
                return Response.paramError();
            }

            // 2.验证规则
            BaseDataRouteService coreService = null;
            for (BaseDataRouteService dataRouteService : dataRouteServices) {
                if (dataRouteService.rule(param)) {
                    coreService = dataRouteService;
                    break;
                }
            }
            if (coreService == null) {
                log.info("Data Route not proper rule!");
                return Response.notData();
            }

            result = coreService.handler(param);
        } catch (Exception e) {
            log.info("Data Route Exception e {0}", e);
        }
        return result;
    }

    public Response handlerResult(Object page) {
        return Response.success(page);
    }

    private boolean valid(DataRouteParam param) {
        if (param == null) {
            log.info("Data Route Param is null!");
            return false;
        }
        Map<Integer,String[]> map = param.getMap();
        if (map == null || map.size() == 0) {
            log.info("Data Route Param pointIds is empty!");
            return false;
        }

        Long startTime = param.getStartTime();
        Long endTime = param.getEndTime();
        if (startTime != null && endTime != null) {
            if (endTime > startTime) {
                log.info("Data Route Param date is illegal!");
                return false;
            }
        }
        return true;
    }

}
