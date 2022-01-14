package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.EnergyConfig;
import com.bmts.heating.commons.basement.model.db.response.EnergyConfigResponse;

import java.util.List;

public interface EnergyConfigService  extends IService<EnergyConfig> {

	List<EnergyConfigResponse> queryConfig(Wrapper wrapper);

	IPage page(IPage page, Wrapper wrapper);
}
