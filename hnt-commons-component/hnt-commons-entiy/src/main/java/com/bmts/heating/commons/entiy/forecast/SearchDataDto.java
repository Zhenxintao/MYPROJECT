package com.bmts.heating.commons.entiy.forecast;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("查询负荷预测数据")
public class SearchDataDto extends BaseDto {
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;
    @ApiModelProperty("1.阶段 2.小时 3.天")
    private Integer forecastType;
    @ApiModelProperty("预测配置信息名称Id,参数0为全部信息")
    private Integer forecastSourceCoreId;
    @ApiModelProperty("1.预测数据，2历史数据")
    private Integer dataType;
}
