package com.bmts.heating.bussiness.search.boot.service.impl;

import com.bmts.heating.bussiness.cache.service.RealDataService;
import com.bmts.heating.bussiness.search.boot.service.BaseDataRouteService;
import com.bmts.heating.commons.entiy.gathersearch.request.DataRouteParam;
import com.bmts.heating.commons.utils.restful.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 实时数据业务逻辑
 * @Author fei.chang
 * @Date 2020/9/10 16:32
 * @Version 1.0
 */
@Service
public class RealDataRouteServiceImpl extends BaseDataRouteService {

    @Autowired
    private RealDataService realDataService;

	@Override
	public boolean rule(DataRouteParam param) {
        return param.getStartTime() == null && param.getEndTime() == null;
    }

	@Override
	public Response handler(DataRouteParam param){
		return realDataService.queryRealDataBySystems(param.getMap());
	}


}
