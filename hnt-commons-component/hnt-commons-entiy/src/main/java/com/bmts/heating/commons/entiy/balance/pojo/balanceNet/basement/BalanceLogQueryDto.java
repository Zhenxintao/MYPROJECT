package com.bmts.heating.commons.entiy.balance.pojo.balanceNet.basement;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel("查询全网平衡记录日志实体类")
@Data
public class BalanceLogQueryDto extends BaseDto {
    @ApiModelProperty("开始时间")
    private String  start;
    @ApiModelProperty("结束时间")
    private String end;
}
