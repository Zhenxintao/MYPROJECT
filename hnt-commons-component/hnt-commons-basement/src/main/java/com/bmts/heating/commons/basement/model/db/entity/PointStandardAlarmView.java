package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * VIEW
 * </p>
 *
 * @author naming
 * @since 2021-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pointStandardAlarmView")
public class PointStandardAlarmView implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事故低报警
     */
    @TableField("accidentLower")
    private BigDecimal accidentLower;

    /**
     * 事故高报警
     */
    @TableField("accidentHigh")
    private BigDecimal accidentHigh;

    /**
     * 运行低报警
     */
    @TableField("runningLower")
    private BigDecimal runningLower;

    /**
     * 运行高报警
     */
    @TableField("runningHigh")
    private BigDecimal runningHigh;

    /**
     * 是否报警
     */
    @TableField("isAlarm")
    private Boolean isAlarm;

    /**
     * 报警值 单选  0 和 1
     */
    @TableField("alarmValue")
    private Integer alarmValue;

    /**
     * 报警外键
     */
    @TableField("alarmConfigId")
    private Integer alarmConfigId;


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


//    /**
//     * 创建人
//     */
//    @TableField("createUser")
//    private String createUser;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private LocalDateTime createTime;

//    /**
//     * 修改人
//     */
//    @TableField("updateUser")
//    private String updateUser;

//    /**
//     * 修改时间
//     */
//    @TableField("updateTime")
//    private LocalDateTime updateTime;

//    /**
//     * 删除标识
//     */
//    @TableField("deleteFlag")
//    private Boolean deleteFlag;

//    /**
//     * 删除时间
//     */
//    @TableField("deleteTime")
//    private LocalDateTime deleteTime;

//    /**
//     * 删除人
//     */
//    @TableField("deleteUser")
//    private String deleteUser;

    private Integer id;

//    /**
//     * 用户id
//     */
//    @TableField("userId")
//    private Integer userId;

    /**
     * 默认值1属于系统 2.控制柜 3.站 4.源 5.网
     */
    private Integer level;


    @ApiModelProperty("同步编号")
    @TableField("syncNumber")
    private String syncNumber;

    @ApiModelProperty("同步父级编号")
    @TableField("syncParentNum")
    private BigInteger syncParentNum;

    @TableField("pointConfig")
    private Integer pointConfig;

    @ApiModelProperty("报警类型名称")
    @TableField("alarmConfigName")
    private String alarmConfigName;
}
