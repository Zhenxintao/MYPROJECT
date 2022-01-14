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
 * @since 2020-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
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
    private Integer hotListType;

    /**
     * 热表口径
     */
    @ApiModelProperty("热表口径")
    private Integer hotListCaliber;

    /**
     * 热表编号
     */
    @ApiModelProperty("热表编号")
    private Integer hotListNumber;

    /**
     * 官网口径
     */
    @ApiModelProperty("官网口径")
    private Integer webSitCaliber;

    /**
     * 供热面积
     */
    @ApiModelProperty("供热面积")
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
    private Integer heatCabinetId;

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
    @ApiModelProperty("描述")
    private String description;

    /**
     * 删除标识
     */
    @ApiModelProperty("删除标识")
    private Boolean deleteFlag;

    private Integer id;
    @ApiModelProperty("安装位置")
    private String installPosition;
    @ApiModelProperty("是否有分支")
    private boolean hasChildren;

    @ApiModelProperty("用户id")
    private Integer userId;
}
