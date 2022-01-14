package com.bmts.heating.commons.entiy.baseInfo.request;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ApiModel("控制量查询类")
@Data
public class PointControlConfigDto extends BaseDto {

	@ApiModelProperty("换热站Id")
	private Integer heatStationId;

	@ApiModelProperty("控制柜Id")
	private Integer heatCabinetId;

	@ApiModelProperty("机组Id")
	private Integer heatSystemId;

	@ApiModelProperty("类型")
	private Integer type;

	@ApiModelProperty("存储代码")
	private String columnName;

}
