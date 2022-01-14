package com.bmts.heating.commons.basement.model.db.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("换热站列表响应类")
public class HeatTransferStationResponse {
    private static final long serialVersionUID = 1L;

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
     * 所属机构
     */
    @ApiModelProperty("所属机构")
    @TableField("heatOrganizationId")
    private Integer heatOrganizationId;

    /**
     * 建站日期
     */
    @ApiModelProperty("建站日期")
    @TableField("buildTime")
    private LocalDateTime buildTime;

    /**
     * 改造日期
     */
    @ApiModelProperty("改造日期")
    @TableField("transformTime")
    private LocalDateTime transformTime;

    /**
     * 供热面积
     */
    @ApiModelProperty("供热面积")
    @TableField("heatArea")
    private BigDecimal heatArea;

    /**
     * 在网面积
     */
    @ApiModelProperty("在网面积")
    @TableField("netArea")
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
    @ApiModelProperty("true 正常，false 冻结")
    private Boolean status;

    /**
     * 建筑类型
     */
    @ApiModelProperty("建筑类型")
    @TableField("buildType")
    private Integer buildType;

    /**
     * 是否是生活水
     */
    @ApiModelProperty("是否是生活水")
    @TableField("isLifeWater")
    private Boolean isLifeWater;

    /**
     * 保温结构类型: 1.节能，2.非节能
     */
    @ApiModelProperty("保温结构类型: 1.节能，2.非节能")
    @TableField("insulationConstruction")
    private Integer insulationConstruction;

    /**
     * 供热方式:1.汽水，2.水水
     */
    @ApiModelProperty("供热方式:1.汽水，2.水水")
    @TableField("heatType")
    private Integer heatType;

    /**
     * 管理方式：1.长管，2.自管
     */
    @ApiModelProperty("管理方式：1.长管，2.自管")
    @TableField("manageType")
    private Integer manageType;

    /**
     * 管路布置：1.串联供热，2.分户供热
     */
    @TableField("pipingLayout")
    @ApiModelProperty("管路布置：1.串联供热，2.分户供热")
    private Integer pipingLayout;

    /**
     * 收费方式: 1.到户，2.集体，3.面积，4.计量
     */
    @TableField("payType")
    @ApiModelProperty("收费方式: 1.到户，2.集体，3.面积，4.计量")
    private Integer payType;

    /**
     * 站点类型:1.人工站点 2.检测站点 3.检测站点 4.阀门
     */
    @TableField("StationType")
    @ApiModelProperty("站点类型:1.人工站点 2.检测站点 3.检测站点 4.阀门")
    private Integer StationType;

    /**
     * 地势
     */
    @ApiModelProperty("地势")
    private String terrain;

    /**
     * 离热源距离
     */
    @TableField("distanceWithHeatSource")
    @ApiModelProperty("离热源距离")
    private Integer distanceWithHeatSource;

    /**
     * 协议类型 ： 未给出明确类型
     */
    @TableField("protocolType")
    @ApiModelProperty("协议类型 ： 未给出明确类型")
    private Integer protocolType;

    /**
     * 所属热源
     */
    @TableField("heatSourceId")
    @ApiModelProperty("所属热源")
    private Integer heatSourceId;

    /**
     * 创建人
     */
    @TableField("createUser")
    @ApiModelProperty("创建人")
    private String createUser;

    /**
     * 创建时间
     */
    @TableField("createTime")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @TableField("updateUser")
    @ApiModelProperty("修改人")
    private String updateUser;

    /**
     * 修改时间
     */
    @TableField("updateTime")
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
    @ApiModelProperty("描述")
    private String description;

    /**
     * 删除人
     */
    @ApiModelProperty("删除人")
    @TableField("deleteUser")
    private String deleteUser;

    /**
     * 删除时间
     */
    @ApiModelProperty("删除时间")
    @TableField("deleteTime")
    private LocalDateTime deleteTime;
    /**
     * 删除标识
     */
    @ApiModelProperty("删除标识")
    @TableField("deleteFlag")
    private Boolean deleteFlag;


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("组织名称")
    private String OrgName;

    @ApiModelProperty("热源名称")
    private String heatSourceName;

    @ApiModelProperty("控制柜数量")
    private Integer heatCabinetCount;

    @ApiModelProperty("机组数量")
    private Integer heatSystemCount;


}
