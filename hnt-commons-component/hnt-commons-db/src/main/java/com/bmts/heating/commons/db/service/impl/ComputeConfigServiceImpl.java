package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.ComputeConfig;
import com.bmts.heating.commons.db.mapper.ComputeConfigMapper;
import com.bmts.heating.commons.db.service.ComputeConfigService;
import com.bmts.heating.commons.basement.model.db.response.ComputeConfigResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComputeConfigServiceImpl extends ServiceImpl<ComputeConfigMapper, ComputeConfig> implements ComputeConfigService {

	@Autowired
	private ComputeConfigMapper mapper;

	public List<ComputeConfigResponse> getPointStandardByComputeConfigId(Integer computeId){
		return mapper.getPointStandardByComputeConfigId(computeId);
	}
}
