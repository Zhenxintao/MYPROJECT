package com.bmts.heating.bussiness.search.boot.service;

import com.bmts.heating.commons.entiy.gathersearch.request.DataRouteParam;
import com.bmts.heating.commons.utils.restful.Response;

import java.util.concurrent.ExecutionException;

/**
 * @Description
 * @Author fei.chang
 * @Date 2020/9/10 16:23
 * @Version 1.0
 */
public abstract class BaseDataRouteService {

    /**
     * 不满意返回结果集子类可以去复写
     * @param page
     * @return
     */
    public Response handlerResult(Object page) {
        return Response.success(page);
    }

    public abstract boolean rule(DataRouteParam param);

    public abstract Response handler(DataRouteParam param) throws ExecutionException, InterruptedException;

}
