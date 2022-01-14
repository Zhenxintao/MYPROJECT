package com.bmts.heating.commons.entiy.balance.pojo.balanceNet;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@ApiModel("配置全网平衡系统列表查询类")
@Data
public class BalanceNetSystemListDto extends BaseDto{
	private Integer balanceNetId;
}
