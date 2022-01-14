package com.bmts.heating.bussiness.cache.service;

import com.bmts.heating.commons.utils.restful.Response;

import java.util.Map;

/**
 * @Author: naming
 * @Description:
 * @Date: Create in 2020/9/27 18:27
 * @Modified by
 */
public interface RealDataService {

//	Response getBaseCache();

	Response queryRealDataBySystems(Map<Integer,String[]> map);
}
