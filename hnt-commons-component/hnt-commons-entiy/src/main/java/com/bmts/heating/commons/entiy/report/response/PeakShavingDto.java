package com.bmts.heating.commons.entiy.report.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName: PeakShavingDto
 * @Description: 调峰数据返回类
 * @Author: pxf
 * @Date: 2021/9/10 13:50
 * @Version: 1.0
 */

@Data
@ApiModel("调峰数据返回类")
public class PeakShavingDto {

    @ApiModelProperty("日期")
    private String dayTime = "日期";
    @ApiModelProperty("室外平均气温")
    private BigDecimal outAvgTemp = BigDecimal.ZERO;
    @ApiModelProperty("计划供水温度")
    private BigDecimal planTemp = BigDecimal.ZERO;
    @ApiModelProperty("实际供水温度")
    private BigDecimal realTemp = BigDecimal.ZERO;
    @ApiModelProperty("调峰幅度")
    private BigDecimal peakRange = BigDecimal.ZERO;
    @ApiModelProperty("是否调峰")
    private String peakShaving = "是否调峰";

}
