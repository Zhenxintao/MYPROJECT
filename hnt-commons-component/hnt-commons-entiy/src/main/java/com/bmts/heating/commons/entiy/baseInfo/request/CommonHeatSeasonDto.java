package com.bmts.heating.commons.entiy.baseInfo.request;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("供暖季查询类")
@Data
public class CommonHeatSeasonDto extends BaseDto {
	@ApiModelProperty("开始时间")
	private String startTime;
	@ApiModelProperty("结束时间")
	private String endTime;
}
