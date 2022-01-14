package com.bmts.heating.commons.entiy.balance.pojo.balanceNet.history;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

//@EqualsAndHashCode(callSuper = true)
@ApiModel("全网平衡历史列表查询类")
@Data
public class BalanceNetHistoryDto extends BaseDto {
	@ApiModelProperty("开始时间")
	private LocalDateTime start;
	@ApiModelProperty("结束时间")
	private LocalDateTime end;
	@ApiModelProperty("虚拟网Id")
	private Integer balanceNetId;
}
