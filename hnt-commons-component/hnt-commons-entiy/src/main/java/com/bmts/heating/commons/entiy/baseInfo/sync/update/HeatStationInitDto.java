package com.bmts.heating.commons.entiy.baseInfo.sync.update;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author naming
 * @description
 * @date 2021/6/2 13:26
 **/
@ApiModel("热力站同步实体")
@Data
public class HeatStationInitDto {

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
    @ApiModelProperty("编号")
    private String code;

    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String address;

    /**
     * 所属机构
     */
    @ApiModelProperty(value = "公司组织机构", required = true)
    private Integer heatOrganizationId;

    /**
     * 建站日期
     */
    @ApiModelProperty("建站日期")
    private LocalDateTime buildTime;

    /**
     * 改造日期
     */
    @ApiModelProperty("改造日期")
    private LocalDateTime transformTime;

    /**
     * 供热面积
     */
    @ApiModelProperty("供热面积")
    private BigDecimal heatArea;

    /**
     * 在网面积
     */
    @ApiModelProperty("在网面积")
    private BigDecimal netArea;

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

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private String principal;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private String phone;

    /**
     * true 正常，false 冻结
     */
    @ApiModelProperty(value = "true 正常，false 冻结", required = true)
    private Boolean status;

//    /**
//     * 是否是生活水
//     */
//    @ApiModelProperty("是否是生活水")
//    private Boolean isLifeWater;

//    /**
//     * 保温结构类型: 1.节能，2.非节能
//     */
//    @ApiModelProperty("保温结构类型: 1.节能，2.非节能")
//    private Integer insulationConstruction;

    /**
     * 供热方式:1.汽水，2.水水
     */
    @ApiModelProperty("供热方式:1.高温水，2.蒸汽")
    private Integer heatType;

    /**
     * 管理方式：1.自管 2.外户
     */
    @ApiModelProperty("管理方式：1.自管 2.外户")
    private Integer manageType;

//    /**
//     * 管路布置：1.串联供热，2.分户供热
//     */
//    @ApiModelProperty("管路布置：1.串联供热，2.分户供热")
//    private Integer pipingLayout;

//    /**
//     * 收费方式: 1.到户，2.集体，3.面积，4.计量
//     */
//    @ApiModelProperty("收费方式: 1.到户，2.集体，3.面积，4.计量")
//    private Integer payType;

    /**
     * 站点类型:1.面积收费站 2.热计量站
     */

    @ApiModelProperty(value = "站点类型:1.面积收费站 2.热计量站 3.生活水", required = true)
    private Integer stationType;

    /**
     * 地势
     */
    @ApiModelProperty("地势")
    private String terrain;

    /**
     * 离热源距离
     */

    @ApiModelProperty("离热源距离")
    private Integer distanceWithHeatSource;
    /**
     * 所属热源
     */
    @ApiModelProperty(value = "所属热源", required = true)
    private Integer heatSourceId;

    @ApiModelProperty("海拔高度")
    private BigDecimal altitude;

    @ApiModelProperty("阀控类型：1.总阀 2.分阀 3.总阀+分阀 4.总阀+分泵 5.总泵 6.总泵+分阀 7.总泵+总阀 8.总泵+总阀+分阀")
    private Integer valveControlType;

//    /**
//     * 所属 所组织机构
//     */
//    @ApiModelProperty(value = "所组织机构")
//    private Integer viceOrgId;

}
