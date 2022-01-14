package com.bmts.heating.web.energy.pojo;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.es.LocalTimeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@ApiModel("能耗明细表格")
@EqualsAndHashCode(callSuper = true)
@Data
public class EnergyInfoDto extends BaseDto {

	//1:热源 2:热网 3:热力站
	@ApiModelProperty("1:热源 2:热网 3:热力站")
	private Integer type;

	//相关id
	@ApiModelProperty("相关id")
	private Integer relevanceId;

	//1:水 2:电 3:热
	@ApiModelProperty("1:水 2:电 3:热")
	private Integer energyType;

	//1:小时 2:天 3:月
	@ApiModelProperty("1:小时 2:天 3:月")
	private Integer dateType;

	//时间
	private Long startTime = LocalTimeUtils.getHour(-1);

	private Long endTime = LocalTimeUtils.getHour(0);

	@ApiModelProperty("日期")
	private LocalDate date;
}
