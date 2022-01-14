package com.bmts.heating.commons.entiy.balance.pojo.balanceNet;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel("sing-参数汇总查询类")
@EqualsAndHashCode(callSuper = true)
@Data
public class BalanceTableQueryDto extends BaseDto {
	//分公司
	@ApiModelProperty("orgId")
	private Integer orgId;

	@ApiModelProperty("热网Id")
	private Integer balanceNetId;

	@ApiModelProperty("系统level")
	private Integer level;

	@ApiModelProperty("关联参量")
	private String[] columnName;

}
