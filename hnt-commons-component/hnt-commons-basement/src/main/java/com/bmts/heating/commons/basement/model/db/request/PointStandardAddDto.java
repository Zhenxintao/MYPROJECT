package com.bmts.heating.commons.basement.model.db.request;

import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("关联参量添加类")
public class PointStandardAddDto {

	@ApiModelProperty("关联参量")
	private PointStandard pointStandard;

	@ApiModelProperty("计算量所需关联参量入参集合")
	private Integer[] pointStandardIds;
}
