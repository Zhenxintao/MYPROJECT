package com.bmts.heating.commons.entiy.balance.pojo.balanceNet;

import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.basement.BalanceNet;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.basement.BalanceNetLimit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("配置全网平衡基础信息添加类")
@Data
public class BalanceNetAddDto {

	@ApiModelProperty("基础信息")
	private BalanceNet balanceNet;

	@ApiModelProperty("上下限列表")
	private List<BalanceNetLimit> balanceNetLimits;
}
