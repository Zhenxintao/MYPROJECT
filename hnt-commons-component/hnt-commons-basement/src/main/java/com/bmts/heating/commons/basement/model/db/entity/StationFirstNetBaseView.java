package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author naming
 * @since 2021-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("stationFirstNetBaseView")
public class StationFirstNetBaseView implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("heatSystemId")
    private Integer heatSystemId;

    /**
     * 名称
     */
    @TableField("heatSystemName")
    private String heatSystemName;

    /**
     * 散热类型：1.挂片采暖 2.地板辐射 3.热风幕 4.风机盘管 5.其他
     */
    @TableField("heatingType")
    private Integer heatingType;

    /**
     * 供热面积
     */
    @TableField("heatSystemArea")
    private BigDecimal heatSystemArea;

    @TableField("heatCabinetId")
    private Integer heatCabinetId;

    /**
     * 名称
     */
    @TableField("heatCabinetName")
    private String heatCabinetName;

    @TableField("heatTransferStationId")
    private Integer heatTransferStationId;

    /**
     * 名称
     */
    @TableField("heatTransferStationName")
    private String heatTransferStationName;

    /**
     * 供热面积
     */
    @TableField("heatStationArea")
    private BigDecimal heatStationArea;

    /**
     * 在网面积
     */
    @TableField("heatStationNetArea")
    private BigDecimal heatStationNetArea;

    /**
     * true 正常，false 冻结
     */
    private Boolean status;

    /**
     * 所属机构
     */
    @TableField("heatStationOrgId")
    private Integer heatStationOrgId;

    /**
     * 名称
     */
    @TableField("heatStationOrgName")
    private String heatStationOrgName;

    @TableField("heatSourceId")
    private Integer heatSourceId;

    /**
     * 名称
     */
    @TableField("heatSourceName")
    private String heatSourceName;

    /**
     * 供热面积
     */
    @TableField("heatSourceArea")
    private BigDecimal heatSourceArea;

    /**
     * 所属组织架构
     */
    @TableField("heatSourceOrgId")
    private Integer heatSourceOrgId;

    /**
     * 名称
     */
    @TableField("heatSourceOrgName")
    private String heatSourceOrgName;

    /**
     * 系统同步编号
     */
    @TableField("systemSyncNum")
    private Integer systemSyncNum;

    /**
     * 控制柜同步编号
     */
    @TableField("cabinetSyncNum")
    private Integer cabinetSyncNum;

    /**
     * 热力站同步编号
     */
    @TableField("stationSyncNum")
    private Integer stationSyncNum;
//    @TableField("heatNetId")
//    private Integer heatNetId;
//
//    /**
//     * 名称
//     */
//    @TableField("heatNetName")
//    private String heatNetName;
//
//    /**
//     * 供热面积
//     */
//    @TableField("heatNetArea")
//    private BigDecimal heatNetArea;
//
//    /**
//     * 所属组织架构
//     */
//    @TableField("heatNetOrgId")
//    private Integer heatNetOrgId;
//
//    /**
//     * 名称
//     */
//    @TableField("heatNetOrgName")
//    private String heatNetOrgName;

    /**
     * 机组编号
     */
    @TableField("number")
    private String number;


    @ApiModelProperty("排序字段")
    @TableField("heatStationSort")
    private Integer heatStationSort;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    @TableField("longitude")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    @TableField("latitude")
    private BigDecimal latitude;

    /**
     * 海拔
     */
    @ApiModelProperty("海拔")
    @TableField("altitude")
    private BigDecimal altitude;

//    /**
//     * 所属站的 副级组织结构名称
//     */
//    @ApiModelProperty("站副级组织结构名称")
//    @TableField("viceOrgName")
//    private String viceOrgName;

    
}
