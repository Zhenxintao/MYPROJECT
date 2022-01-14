package com.bmts.heating.commons.entiy.baseInfo.request;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("查询天气预报数据")
public class QueryWeatherDto  extends BaseDto {
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;
    @ApiModelProperty("获取数据标识(1 小时实时;2 小时预测 3天预测)")
    private int type;
}
