package com.bmts.heating.commons.entiy.baseInfo.request.alarm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;

@ApiModel("批量启停")
@Data
public class BatchAlarmDto {

	@ApiModelProperty("采集量Ids")
	private Collection<Integer> ids;

	@ApiModelProperty("启停状态值")
	private Boolean state;
}
