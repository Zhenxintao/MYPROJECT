package com.bmts.heating.commons.entiy.baseInfo.sync.update;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author naming
 * @description
 * @date 2021/6/2 13:11
 **/
@ApiModel("热源同步实体")
@Data
public class HeatSourceInitDto {

    /**
     * 同步编号
     */
    @ApiModelProperty(value = "同步编号", required = true)
    private Integer num;

    /**
     * 编号
     */
    @ApiModelProperty("编号")
    private String code;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", required = true)
    private String name;


    /**
     * 地址
     */
    @ApiModelProperty(value = "地址", required = true)
    private String address;

    /**
     * 热源标识:1.虚拟 2.真实
     */
    @ApiModelProperty("热源标识 1.虚拟 2.真实")
    private Integer flag;

    /**
     * 热源类型： 1.电厂 2.长输热源 3.调峰热源
     */
    @ApiModelProperty("热源类型： 1.电厂 2.长输热源 3.调峰热源")
    private Integer type;

    /**
     * 经度
     */
    @ApiModelProperty(value = "经度", required = true)
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度", required = true)
    private BigDecimal latitude;

    /**
     * 组织结构id
     */
    @ApiModelProperty(value = "组织结构id", required = true)
    private Integer heatOrganizationId;

    /**
     * 所属热网
     */
    @ApiModelProperty(value = "所属热网", required = true)
    private Integer heatNetId;


    /**
     * 管径
     */
    @ApiModelProperty("管径（m）")
    private Integer pipeDiameter;

    /**
     * 供热面积
     */
    @ApiModelProperty("供热面积")
    private BigDecimal heatArea;
}
