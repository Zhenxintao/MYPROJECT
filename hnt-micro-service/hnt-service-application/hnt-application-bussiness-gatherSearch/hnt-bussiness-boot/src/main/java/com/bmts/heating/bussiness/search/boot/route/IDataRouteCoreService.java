package com.bmts.heating.bussiness.search.boot.route;

import com.bmts.heating.commons.entiy.gathersearch.request.DataRouteParam;
import com.bmts.heating.commons.utils.restful.Response;

/**
 * @Description
 * @Author fei.chang
 * @Date 2020/9/10 15:33
 * @Version 1.0
 */
public interface IDataRouteCoreService {

    /**
     * @Description 核心路由方法
     * @Author fei.chang
     * @Param [param] 路由参数
     * @return com.bmts.heating.bussiness.common.common.Response 返回值
     **/
    Response dataRoute(DataRouteParam param);

}
