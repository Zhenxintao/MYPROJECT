package com.bmts.heating.web.backStage.service;

import com.bmts.heating.commons.basement.model.db.entity.EnergyConsumption;
import com.bmts.heating.commons.entiy.baseInfo.request.EnergyConsumptionDto;
import com.bmts.heating.commons.utils.restful.Response;
import org.springframework.web.multipart.MultipartFile;


public interface EnergyConsumptionService {

	Response importExcel(MultipartFile multipartFile, EnergyConsumptionDto energyConsumptionDto);

	Response downLoad(String id);

	Response findAll(EnergyConsumptionDto energyConsumptionDto);
	Response delete(String id);

	Response update(EnergyConsumption energyConsumption);

}
