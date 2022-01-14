package com.bmts.heating.commons.basement.model.db.response;

import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("标准点表详情响应类")
public class PointStandardSingleResponse {
	private PointStandard pointStandard;
	private List<ComputeConfigResponse> configResponses;
}
