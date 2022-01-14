package com.bmts.heating.web.energy.service.energyByTd;

import com.bmts.heating.commons.utils.restful.Response;

public interface EnergySystemHomeService {
    Response energyData();
    Response energyDataOverall();
    Response mapEnergyData(Integer id);
    Response energyRankData();
}
