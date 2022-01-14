package com.bmts.heating.commons.db.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.cache.FirstNetBase;
import com.bmts.heating.commons.basement.model.db.entity.HeatSystem;
import com.bmts.heating.commons.basement.model.db.response.station.HeatSystemCSResponse;
import com.bmts.heating.commons.db.mapper.HeatSystemMapper;
import com.bmts.heating.commons.db.service.HeatSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author naming
 * @since 2020-11-10
 */
@Service
public class HeatSystemServiceImpl extends ServiceImpl<HeatSystemMapper, HeatSystem> implements HeatSystemService {

	@Autowired
	HeatSystemMapper heatSystemMapper;

	@Override
	public List<FirstNetBase> querySystem(QueryWrapper queryWrapper) {
		return heatSystemMapper.querySystem(queryWrapper);
	}

	@Override
	public Map<String, Object> querySystemNameByStationId(QueryWrapper queryWrapper) {
        JSONObject jsonObject = new JSONObject();
		List<HeatSystemCSResponse> list = heatSystemMapper.querySystemNameByStationId(queryWrapper);
		Map<String, List<HeatSystemCSResponse>> collect = list.stream().collect(Collectors.groupingBy(HeatSystemCSResponse::getStationName));
		for (Map.Entry<String, List<HeatSystemCSResponse>> entry : collect.entrySet()) {
			List<JSONObject> childrenLis = new ArrayList<>();
            Map<String, List<HeatSystemCSResponse>> cab = entry.getValue().stream().collect(Collectors.groupingBy(HeatSystemCSResponse::getCabinetName));
            for (Map.Entry<String, List<HeatSystemCSResponse>> map : cab.entrySet()) {
				JSONObject children = new JSONObject();
                children.put("cabinetName",map.getKey());
                children.put("children",map.getValue());
				childrenLis.add(children);
            }
            jsonObject.put("stationName",entry.getKey());
            jsonObject.put("children",childrenLis);
		}
		return jsonObject;
	}

	@Override
	public List<Integer> querySystemIdByHeatSourceId(QueryWrapper queryWrapper) {
		return heatSystemMapper.querySystemIdByHeatSourceId(queryWrapper);
	}



}
