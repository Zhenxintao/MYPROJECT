package com.bmts.heating.web.backStage.service.impl;

import com.bmts.heating.commons.basement.model.db.entity.EnergyConsumption;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.EnergyConsumptionDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.EnergyConsumptionService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@Slf4j
public class EnergyConsumptionServiceImpl extends SavantServices implements EnergyConsumptionService {

	@Autowired
	private BackRestTemplate backRestTemplate;

	private final String baseServer = "bussiness_baseInfomation";

	@Override
	public Response importExcel(MultipartFile file, EnergyConsumptionDto energyConsumptionDto) {
		return backRestTemplate.file("/energyConsumption/import",file, energyConsumptionDto,baseServer);
	}

	@Override
	public Response downLoad(String id) {
		return backRestTemplate.get("/energyConsumption/download?id="+id,baseServer);
	}

	@Override
	public Response findAll(EnergyConsumptionDto energyConsumptionDto) {
		return backRestTemplate.post("/energyConsumption/findAll",energyConsumptionDto,baseServer);
	}

	@Override
	public Response delete(String id) {
		return backRestTemplate.delete("/energyConsumption/delete?id="+id,baseServer);
	}

	@Override
	public Response update(EnergyConsumption energyConsumption) {
		return backRestTemplate.put("/energyConsumption/update",energyConsumption,baseServer);
	}

}
