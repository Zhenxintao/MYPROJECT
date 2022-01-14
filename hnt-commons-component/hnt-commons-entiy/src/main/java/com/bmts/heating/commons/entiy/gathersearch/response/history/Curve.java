package com.bmts.heating.commons.entiy.gathersearch.response.history;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author naming
 * @date 2021/1/8 8:53
 * 曲线
 **/
@Data
public class Curve {
    @ApiModelProperty("时间")
    private long time;
    @ApiModelProperty("系统id")
    private Integer heatSystemId;
    @ApiModelProperty("点 key为点标签名 value为值")
    private Map<String, BigDecimal> point;
}
