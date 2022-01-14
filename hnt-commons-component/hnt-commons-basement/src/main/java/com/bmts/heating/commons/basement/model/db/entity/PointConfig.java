package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author naming
 * @since 2021-01-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pointConfig")
public class PointConfig implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 关联参量id
     */
    @TableField("pointStandardId")
    private Integer pointStandardId;

    /**
     * 关联id
     */
    @TableField("relevanceId")
    private Integer relevanceId;


    /**
     * 创建人
     */
    @TableField("createUser")
    private String createUser;

    /**
     * 创建时间
     */
    @TableField("createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @TableField("updateUser")
    private String updateUser;

    /**
     * 修改时间
     */
    @TableField("updateTime")
    private LocalDateTime updateTime;


    /**
     * 删除标识
     */
    @TableField("deleteFlag")
    private Boolean deleteFlag;

    /**
     * 删除时间
     */
    @TableField("deleteTime")
    private LocalDateTime deleteTime;

    /**
     * 删除人
     */
    @TableField("deleteUser")
    private String deleteUser;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    @TableField("userId")
    private Integer userId;

    /**
     * 默认值1属于系统 2.控制柜 3.站 4.源 5.网
     */
    private Integer level;

    @TableField(exist = false)
    private String pointColumnName;

    @ApiModelProperty("同步编号")
    @TableField("syncNumber")
    private String syncNumber;

    @ApiModelProperty("同步父级编号")
    @TableField("syncParentNum")
    private BigInteger syncParentNum;

}
