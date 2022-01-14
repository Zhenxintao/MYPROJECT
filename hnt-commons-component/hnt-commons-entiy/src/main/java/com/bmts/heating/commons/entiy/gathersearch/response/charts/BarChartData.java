package com.bmts.heating.commons.entiy.gathersearch.response.charts;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "柱状图返回结果实体类")
@Data
public class BarChartData {
    @ApiModelProperty("x轴数据集合")
    private List<Object> xData ;
    @ApiModelProperty("y轴数据集合")
    private List<Object> yData;
}
