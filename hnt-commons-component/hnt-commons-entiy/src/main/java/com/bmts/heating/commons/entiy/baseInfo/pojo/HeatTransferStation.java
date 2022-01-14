package com.bmts.heating.commons.entiy.baseInfo.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("换热站")
public class HeatTransferStation implements Serializable {

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
    @ApiModelProperty("true 正常，false 冻结")
    private Boolean status;

    /**
     * 建筑类型
     */
    @ApiModelProperty("建筑类型")
    private Integer buildType;

    /**
     * 是否是生活水
     */
    @ApiModelProperty("是否是生活水")
    private Boolean isLifeWater;

    /**
     * 保温结构类型: 1.节能，2.非节能
     */
    @ApiModelProperty("保温结构类型: 1.节能，2.非节能")
    private Integer insulationConstruction;

    /**
     * 供热方式:1.汽水，2.水水
     */
    @ApiModelProperty("供热方式:1.汽水，2.水水")
    private Integer heatType;

    /**
     * 管理方式：1.长管，2.自管
     */
    @ApiModelProperty("管理方式：1.长管，2.自管")
    private Integer manageType;

    /**
     * 管路布置：1.串联供热，2.分户供热
     */
    @ApiModelProperty("管路布置：1.串联供热，2.分户供热")
    private Integer pipingLayout;

    /**
     * 收费方式: 1.到户，2.集体，3.面积，4.计量
     */
    @ApiModelProperty("收费方式: 1.到户，2.集体，3.面积，4.计量")
    private Integer payType;

    /**
     * 站点类型:1.人工站点 2.检测站点 3.检测站点 4.阀门
     */
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
    @ApiModelProperty("离热源距离")
    private Integer distanceWithHeatSource;

    /**
     * 协议类型 ： 未给出明确类型
     */
    @ApiModelProperty("协议类型 ： 未给出明确类型")
    private Integer protocolType;

    /**
     * 所属热源
     */
    @ApiModelProperty("所属热源")
    private Integer heatSourceId;

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

    @ApiModelProperty("描述")
    private String description;

    /**
     * 删除人
     */
    @ApiModelProperty("删除人")
    private String deleteUser;

    /**
     * 删除时间
     */
    @ApiModelProperty("删除时间")
    private LocalDateTime deleteTime;
    /**
     * 删除标识
     */
    @ApiModelProperty("删除标识")
    private Boolean deleteFlag;

    private Integer id;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("海拔高度")
    private BigDecimal altitude;

}
