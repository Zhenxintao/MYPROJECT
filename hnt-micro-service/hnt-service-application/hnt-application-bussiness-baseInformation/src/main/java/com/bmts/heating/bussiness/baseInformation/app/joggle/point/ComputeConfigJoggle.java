package com.bmts.heating.bussiness.baseInformation.app.joggle.point;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.ComputeConfig;
import com.bmts.heating.commons.db.service.ComputeConfigService;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.ComputeConfigAddDto;
import com.bmts.heating.commons.basement.model.db.response.ComputeConfigResponse;
import com.bmts.heating.commons.utils.restful.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class ComputeConfigJoggle {

	@Autowired
	private ComputeConfigService computeConfigService;

	public Response add(ComputeConfigAddDto dto){
		List<ComputeConfig> configList = new LinkedList<>();
		for (Integer pointStandardId : dto.getPointStandardId()) {
			if (pointStandardId != 0)
				configList.add(new ComputeConfig(pointStandardId,dto.getCompoteId()));
		}
		return computeConfigService.saveBatch(configList) ? Response.success() : Response.fail();
	}

	public List<ComputeConfigResponse> query(Integer computeId){
		return computeConfigService.getPointStandardByComputeConfigId(computeId);
	}

	public Boolean delete(Integer computeId){
		return computeConfigService.remove(Wrappers.<ComputeConfig>lambdaQuery().eq(ComputeConfig::getComputeId, computeId));
	}
}
