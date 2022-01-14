package com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 基础历史数据-数据返回点位信息响应实体
 * */
@Data
public class PointsInfo {
    @ApiModelProperty("点位标识名称")
    private String pointName;
    @ApiModelProperty("点位标识数据值")
    private BigDecimal pointValue;
}
