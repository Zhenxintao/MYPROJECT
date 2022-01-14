package com.bmts.heating.commons.basement.model.db.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class HeatSourceResponse implements Serializable {
    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Integer id;
    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 简拼
     */
    @ApiModelProperty("简拼")
    private String logogram;

    /**
     * 编号
     */
    @ApiModelProperty("编号")
    private String code;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String address;

    /**
     * 热源标识:1.虚拟 2.真实
     */
    @ApiModelProperty("热源标识 1.虚拟 2.真实")
    private Integer flag;

    /**
     * 热源标识： 1.热水锅炉 2.燃气锅炉 3.电厂 4.热泵
     */
    @ApiModelProperty("热源标识 1.热水锅炉 2.燃气锅炉 3.电厂 4.热泵")
    private Integer type;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private BigDecimal latitude;

//    /**
//     * 所属热网
//     */
//    @ApiModelProperty("所属热网")
//    private String heatNetName;
//    @ApiModelProperty("所属热网id")
//    private Integer heatNetId;

    @ApiModelProperty("最大输出功率(kw)")
    private BigDecimal maxPowerOut;
    @ApiModelProperty("热量峰值℃")
    private BigDecimal hotPeak;
    @ApiModelProperty("流量峰值T")
    private BigDecimal flowPeak;
    @ApiModelProperty("供温峰值℃")
    private BigDecimal tempSupplyPeak;
    @ApiModelProperty("回温峰值℃")
    private BigDecimal tempReturnPeak;
    /**
     * 所属热网
     */
    @ApiModelProperty("所属公司")
    private String heatOrganizationName;
    @ApiModelProperty("所属公司")
    private Integer heatOrganizationId;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    private String description;
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
