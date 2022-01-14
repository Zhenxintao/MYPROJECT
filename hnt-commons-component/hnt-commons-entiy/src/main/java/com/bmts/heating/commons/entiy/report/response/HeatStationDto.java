package com.bmts.heating.commons.entiy.report.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName: ReportResponse
 * @Description: 报表数据返回类
 * @Author: pxf
 * @Date: 2021/9/10 13:50
 * @Version: 1.0
 */

@Data
@ApiModel("报表数据返回类")
public class HeatStationDto {

    @ApiModelProperty("公司名称")
    private String orgName;
    @ApiModelProperty("站点名称")
    private String stationName;
    @ApiModelProperty("系统名称")
    private String systemName;
    @ApiModelProperty("供热面积")
    private BigDecimal heatArea;
    @ApiModelProperty("一次供温")
    private BigDecimal t1g;
    @ApiModelProperty("一次回温")
    private BigDecimal t1h;
    @ApiModelProperty("一次供压")
    private BigDecimal p1g;
    @ApiModelProperty("一次回压")
    private BigDecimal p1h;
    @ApiModelProperty("供水流量")
    private BigDecimal flowG;
    @ApiModelProperty("回水流量")
    private BigDecimal flowH;

    @ApiModelProperty("二次供温")
    private BigDecimal t2g;
    @ApiModelProperty("二次回温")
    private BigDecimal t2h;
    @ApiModelProperty("二次供压")
    private BigDecimal p2g;
    @ApiModelProperty("二次回压")
    private BigDecimal p2h;

    @ApiModelProperty("补水量")
    private BigDecimal supplyWarter;
    @ApiModelProperty("热量")
    private BigDecimal quantityHeat;
    @ApiModelProperty("报表时段累计热量")
    private BigDecimal quantityHeatTotal;
    @ApiModelProperty("热指标")
    private BigDecimal heatIndex;


}
