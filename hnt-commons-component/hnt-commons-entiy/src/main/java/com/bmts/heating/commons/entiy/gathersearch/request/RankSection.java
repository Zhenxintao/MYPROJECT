package com.bmts.heating.commons.entiy.gathersearch.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author naming
 * @description
 * @date 2021/1/10 17:21
 **/
@Data
public class RankSection {
    @ApiModelProperty("点名称 英文标识")
    private String pointName;
    @ApiModelProperty("开始区间")
    private Double start=0.0;
    @ApiModelProperty("结束区间")
    private Double end=10000000.0;
}
