package com.bmts.heating.commons.entiy.balance.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.context.annotation.Description;

import java.util.List;

/**
 * @author zxt
 * @description
 * @date 2021/02/02 15:46
 **/
@ApiModel("全网平衡机组基础信息")
@Data
@Description("全网平衡机组基础信息")
public class ResultBalanceInfo {
    @ApiModelProperty("全网计算目标均度(℃)")
    private float target;
    @ApiModelProperty("全网实际目标均温(℃)")
    private float targetnet;
    @ApiModelProperty("全网计算供热面积(㎡)")
    private float area;
    @ApiModelProperty("全网实际供热面积(㎡)")
    private float areanet;
    @ApiModelProperty("计算失调度")
    private float imbalance;
    @ApiModelProperty("实际失调度")
    private float imbalancenet;
    @ApiModelProperty("返回机组信息")
    private List<Unit> units;
}
