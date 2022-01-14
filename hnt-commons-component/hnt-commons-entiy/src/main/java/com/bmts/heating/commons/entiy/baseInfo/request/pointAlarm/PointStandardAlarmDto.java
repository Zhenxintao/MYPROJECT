package com.bmts.heating.commons.entiy.baseInfo.request.pointAlarm;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ApiModel("通用点位报警设置查询类")
@Data
public class PointStandardAlarmDto extends BaseDto {


	@ApiModelProperty("类型")
	private Integer type;

	@ApiModelProperty("存储代码")
	private String columnName;

	@ApiModelProperty("点类型 区分 1:采集、2:控制量")
	private Integer pointConfigType;

}
