package com.bmts.heating.commons.basement.model.db.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComputeConfigResponse {

	@ApiModelProperty("相关参数标识")
	private Integer id;
	@ApiModelProperty("关联参量id")
	private Integer pointStandardId;
	@ApiModelProperty("关联参量名称")
	private String name;
	@ApiModelProperty("关联参量标识")
	private String columnName;

}
