package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author naming
 * @since 2020-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("heatSystem")
@ApiModel("系统")
public class HeatSystem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 机组编号
     */
    @ApiModelProperty("机组编号")
    private Integer number;

    /**
     * 热表类型
     */
    @ApiModelProperty("热表类型")
    @TableField("hotListType")
    private Integer hotListType;

    /**
     * 热表口径
     */
    @ApiModelProperty("热表口径")
    @TableField("hotListCaliber")
    private Integer hotListCaliber;

    /**
     * 热表编号
     */
    @ApiModelProperty("热表编号")
    @TableField("hotListNumber")
    private Integer hotListNumber;

    /**
     * 官网口径
     */
    @ApiModelProperty("官网口径")
    @TableField("webSitCaliber")
    private Integer webSitCaliber;

    /**
     * 供热面积
     */
    @ApiModelProperty("供热面积")
    @TableField("heatArea")
    private BigDecimal heatArea;

    /**
     * 父id，用于子分支
     */
    @ApiModelProperty("父id，用于子分支")
    private Integer pid;

    /**
     * 唯一编码
     */
    @ApiModelProperty("唯一编码")
    private String code;

    /**
     * 所属机柜id
     */
    @ApiModelProperty("所属机柜id")
    @TableField("heatCabinetId")
    private Integer heatCabinetId;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    @TableField("createUser")
    private String createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField("createTime")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    @TableField("updateUser")
    private String updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @TableField("updateTime")
    private LocalDateTime updateTime;

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
    @ApiModelProperty("描述")
    private String description;

    /**
     * 删除标识
     */
    @ApiModelProperty("删除标识")
    @TableField("deleteFlag")
    private Boolean deleteFlag;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty("安装位置")
    @TableField(value = "installPosition")
    private String installPosition;
    @ApiModelProperty("是否有分支")
    @TableField(value = "hasChildren")
    private boolean hasChildren;

    @ApiModelProperty("用户id")
    @TableField("userId")
    private Integer userId;

    @ApiModelProperty("散热类型：1.挂片采暖 2.地板辐射 3.热风幕 4.风机盘管 5.其他")
    @TableField("heatingType")
    private Integer heatingType;

    @ApiModelProperty("换热类型：1.间联 2.混水 3.直供 4.汽水换热 5.大温差机组")
    @TableField("heatTransferType")
    private Integer heatTransferType;

    @ApiModelProperty("节能类型：1.非节能建筑 2.一步节能建筑30% 3.二步节能建筑50% 4.三步节能建筑65% 5.四步节能建筑75% 6.其他")
    @TableField("energySavingType")
    private Integer energySavingType;

    @ApiModelProperty("建筑类型：1.民宅 2.学校 3.商业建筑 4.工业建筑 5.保障型建筑(幼儿园、养老院、政府小区等)")
    @TableField("buildingType")
    private Integer buildingType;

    @ApiModelProperty("同步编号")
    @TableField("syncNumber")
    private BigInteger syncNumber;

    @ApiModelProperty("同步父级编号")
    @TableField("syncParentNum")
    private Integer syncParentNum;
}
