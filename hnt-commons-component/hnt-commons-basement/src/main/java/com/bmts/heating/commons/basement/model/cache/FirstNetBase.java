package com.bmts.heating.commons.basement.model.cache;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 一网基础信息
 */
@ApiModel("网 、源、站、控制柜、系统 关联信息")
@Data
public class FirstNetBase implements Serializable {

    /**
     * 热网信息字段
     */
    @ApiModelProperty("热网id")
    private int heatNetId;

    @ApiModelProperty("热网名称")
    private String heatNetName;
    /**
     * 热网 的供热面积
     */
    @ApiModelProperty("热网供热面积")
    private BigDecimal heatNetArea;
    /**
     * 热网 所属组织结构 id
     */
    @ApiModelProperty("热网所属组织结构id")
    private int heatNetOrgId;
    /**
     * 热网 所属组织结构 名称
     */
    @ApiModelProperty("热网所属组织结构名称")
    private String heatNetOrgName;

    /**
     * 热源信息字段
     */
    @ApiModelProperty("热源id")
    private int heatSourceId;
    @ApiModelProperty("热源名称")
    private String heatSourceName;
    /**
     * 热源  的供热面积
     */
    @ApiModelProperty("热源供热面积")
    private BigDecimal heatSourceArea;
    /**
     * 热源 所属组织结构 id
     */
    @ApiModelProperty("热源所属组织结构id")
    private int heatSourceOrgId;
    /**
     * 热源 所属组织结构 名称
     */
    @ApiModelProperty("热源所属组织结构名称")
    private String heatSourceOrgName;


    /**
     * 换热站信息字段
     */
    @ApiModelProperty("换热站id")
    private int heatTransferStationId;
    @ApiModelProperty("换热站名称")
    private String heatTransferStationName;
    /**
     * 换热站  的供热面积
     */
    @ApiModelProperty("换热站供热面积")
    private BigDecimal heatStationArea;
    /**
     * 换热站  的在网面积
     */
    @ApiModelProperty("换热站在网面积")
    private BigDecimal heatStationNetArea;
    /**
     * 换热站 所属组织结构 id
     */
    @ApiModelProperty("换热站所属组织结构id")
    private int heatStationOrgId;
    /**
     * 换热站 所属组织结构 名称
     */
    @ApiModelProperty("换热站所属组织结构名称")
    private String heatStationOrgName;


    /**
     * 控制柜信息字段
     */
    @ApiModelProperty("控制柜id")
    private int heatCabinetId;
    @ApiModelProperty("控制柜名称")
    private String heatCabinetName;


    /**
     * 系统信息字段
     */
    @ApiModelProperty("系统id")
    private int heatSystemId;

    @ApiModelProperty("系统名称")
    private String heatSystemName;
    /**
     * 系统  的供热面积
     */
    @ApiModelProperty("系统供热面积")
    private BigDecimal heatSystemArea;

    @ApiModelProperty("采暖方式 1.地板辐射 2.挂片采暖")
    private int heatingType;

    @ApiModelProperty("换热站状态 1.正常 2.冻结")
    private boolean status;

}
