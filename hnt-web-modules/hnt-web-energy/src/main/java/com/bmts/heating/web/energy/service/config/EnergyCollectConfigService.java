package com.bmts.heating.web.energy.service.config;

import com.bmts.heating.commons.entiy.energy.EnergyCollectConfigAddDto;
import com.bmts.heating.commons.entiy.energy.EnergyCollectConfigParamQueryDto;
import com.bmts.heating.commons.entiy.energy.EnergyCollectConfigQueryDto;
import com.bmts.heating.commons.entiy.energy.EnergyPointResponse;
import com.bmts.heating.commons.utils.restful.Response;

import java.util.List;
import java.util.Map;

public interface EnergyCollectConfigService {

	Response page(EnergyCollectConfigQueryDto dto);

	Map<String, List<EnergyPointResponse>> queryAll(EnergyCollectConfigParamQueryDto dto);

	Response saveOrUpdate(EnergyCollectConfigAddDto addRequest);

	Response info(Integer id);

	Response delete(Integer id);

	Response heatSourceList();
}
