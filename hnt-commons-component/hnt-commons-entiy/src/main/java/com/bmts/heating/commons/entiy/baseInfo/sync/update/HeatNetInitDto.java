package com.bmts.heating.commons.entiy.baseInfo.sync.update;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author naming
 * @description
 * @date 2021/6/2 13:22
 **/
@ApiModel("热网同步实体")
@Data
public class HeatNetInitDto {

    /**
     * 同步编号
     */
    @ApiModelProperty(value = "同步编号", required = true)
    private Integer num;


    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    private String code;


    /**
     * 热网类型： 1.环网 2.分网
     */
    @ApiModelProperty("热网类型： 1.环网 2.分网")
    private Integer type;

    @ApiModelProperty("供热面积")
    private BigDecimal heatArea;

    @ApiModelProperty(value = "所属公司", required = true)
    private int heatOrganizationId;

    /**
     * 热网标识:1.虚拟 2.真实
     */
    @ApiModelProperty(value = "热网标识:1.虚拟 2.真实", required = true)
    private Integer flag;
}
