package com.bmts.heating.bussiness.cache.service.impl;

import com.bmts.heating.bussiness.cache.service.RealDataService;
import com.bmts.heating.commons.basement.model.cache.PointCache;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.middleware.cache.services.RedisCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RealDataImpl implements RealDataService {

	@Autowired
	private RedisCacheService redisCacheService;


//	@Override
//	public Response getBaseCache() {
//		try {
//			return Response.success(redisCacheService.queryFirstNetBase());
//		} catch (MicroException e) {
//			e.printStackTrace();
//		}
//		return Response.fail();
//	}

	public Response queryRealDataBySystems(Map<Integer,String[]> map){
		List<PointCache> pointCaches;
		try{
			long start = System.currentTimeMillis();

			pointCaches = redisCacheService.queryRealDataBySystems(map, TreeLevel.HeatSystem.level());
			log.info("查询缓存库耗时: {} ms",System.currentTimeMillis()-start);
			return Response.success(pointCaches);
		}catch(Exception e){
			e.printStackTrace();
		}
		return Response.fail();
	}


}
