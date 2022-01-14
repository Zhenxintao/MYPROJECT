package com.bmts.heating.commons.entiy.baseInfo.request.pointConfig;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ApiModel("col_采集点关联参量查询类")
@Data
public class PointConfigStandardQueryDto extends BaseDto {

	@ApiModelProperty("关联Id")
	private Integer heatSystemId;

	@ApiModelProperty("层级")
	private Integer level;

}
