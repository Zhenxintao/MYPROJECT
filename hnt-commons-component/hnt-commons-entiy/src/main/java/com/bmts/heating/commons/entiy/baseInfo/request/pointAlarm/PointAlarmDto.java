package com.bmts.heating.commons.entiy.baseInfo.request.pointAlarm;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ApiModel("采集量查询类")
@Data
public class PointAlarmDto extends BaseDto {

	@ApiModelProperty("热源Id")
	private Integer heatSourceId;

	@ApiModelProperty("热网Id")
	private Integer heatNetId;

	@ApiModelProperty("系统分支Id")
	private Integer heatSystemBranchId;

	@ApiModelProperty("换热站Id")
	private Integer heatStationId;

	@ApiModelProperty("控制柜Id")
	private Integer heatCabinetId;

	@ApiModelProperty("机组Id")
	private Integer heatSystemId;

	@ApiModelProperty("类型")
	private Integer type;

	@ApiModelProperty("启用报警")
	private Boolean isAlarm;

	@ApiModelProperty("存储代码")
	private String columnName;

	@ApiModelProperty("层级")
	private Integer level;

	@ApiModelProperty("点类型 区分 1:采集、2:控制量")
	private Integer pointConfigType;

	@ApiModelProperty("数据类型 1:未配置数据  2:已配置数据")
	private Integer dataType;

}
