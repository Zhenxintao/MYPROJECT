package com.bmts.heating.commons.entiy.balance.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceMemberResponse {
    @ApiModelProperty("全网平衡Id")
    private Integer balanceNetId;
    @ApiModelProperty("系统Id")
    private Integer relevanceId;
    @ApiModelProperty("状态")
    private Boolean status;
    @ApiModelProperty("换热站名称")
    private String heatTransferStationName;
    @ApiModelProperty("系统名称")
    private String heatSystemName;
    @ApiModelProperty("系统面积")
    private BigDecimal heatSystemArea;
    @ApiModelProperty("差距值")
    private String diffValue;
    @ApiModelProperty("阀门开度")
    private String valveOpening;
    @ApiModelProperty("二次供温")
    private String tg;
    @ApiModelProperty("二次回温")
    private String th;
}
