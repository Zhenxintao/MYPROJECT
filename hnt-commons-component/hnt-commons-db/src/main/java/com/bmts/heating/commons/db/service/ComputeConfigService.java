package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.ComputeConfig;
import com.bmts.heating.commons.basement.model.db.response.ComputeConfigResponse;

import java.util.List;

public interface ComputeConfigService extends IService<ComputeConfig> {

	List<ComputeConfigResponse> getPointStandardByComputeConfigId(Integer computeId);
}
