package com.bmts.heating.commons.entiy.gathersearch.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author naming
 * @description
 * @date 2021/4/20 20:28
 **/
@Data
@ApiModel("预测流量/实际流量 对比统计")
public class CurveXTextResponse {
    @ApiModelProperty("横坐标 为预测网名称")
    List<String> xLabels;
    @ApiModelProperty("y坐标")
    Map<String, List<BigDecimal>> yValues;
}
