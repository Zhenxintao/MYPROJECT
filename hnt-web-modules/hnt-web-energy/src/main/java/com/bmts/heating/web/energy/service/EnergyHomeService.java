package com.bmts.heating.web.energy.service;

import com.bmts.heating.commons.entiy.baseInfo.request.energy.EnergyHomeChartDto;
import com.bmts.heating.commons.entiy.baseInfo.request.energy.EnergyHomeDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.energy.pojo.HomeRateDto;

public interface EnergyHomeService {

	Response baseData(Integer type,Integer id);

	Response radarCharts(Integer id);

	Response synthesizeEnergy();

	Response rate(HomeRateDto dto);

	Response energyRank(EnergyHomeChartDto dto);

	/**
	 * 换热站能耗
	 * @param id 换热站id
	 * @return Response
	 */
	Response stationEnergy(int id);
}
