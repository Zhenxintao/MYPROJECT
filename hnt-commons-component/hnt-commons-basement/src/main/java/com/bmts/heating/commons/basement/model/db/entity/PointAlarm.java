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
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author zxt
 * @since 2021-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pointAlarm")
@ApiModel("具体点位报警配置")
public class PointAlarm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事故低报警
     */
    @ApiModelProperty("事故低报警")
    @TableField("accidentLower")
    private BigDecimal accidentLower;

    /**
     * 事故高报警
     */
    @ApiModelProperty("事故高报警")
    @TableField("accidentHigh")
    private BigDecimal accidentHigh;

    /**
     * 运行低报警
     */
    @ApiModelProperty("运行低报警")
    @TableField("runningLower")
    private BigDecimal runningLower;

    /**
     * 运行高报警
     */
    @ApiModelProperty("运行高报警")
    @TableField("runningHigh")
    private BigDecimal runningHigh;

    /**
     * isAlarm
     */
    @ApiModelProperty("是否报警")
    @TableField("isAlarm")
    private Boolean  isAlarm;

    /**
     * 报警值 单选  0 和 1
     */
    @ApiModelProperty("报警值 单选  0 和 1")
    @TableField("alarmValue")
    private Integer alarmValue;

    /**
     * 报警外键
     */
    @ApiModelProperty("报警外键")
    @TableField("alarmConfigId")
    private Integer alarmConfigId;

//    /**
//     * 关联参量id
//     */
//    @ApiModelProperty("关联参量id")
//    private Integer pointStandardId;

    /**
     * 关联 pointConfig 表 id
     */
    @ApiModelProperty("关联 pointConfig 表 id")
    @TableField("pointConfigId")
    private Integer pointConfigId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField("createTime")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @TableField("updateTime")
    private LocalDateTime updateTime;


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 点的同步编号
     */
    @TableField(exist = false)
    private String  pointSyncNum;


    /**
     * 报警级别
     */
    @ApiModelProperty("报警级别")
    @TableField("grade")
    private Integer grade;

    /**
     * 报警描述
     */
    @ApiModelProperty("报警描述")
    @TableField("alarmDesc")
    private String alarmDesc;


    /**
     * 报警级别 json
     */
    @ApiModelProperty("报警级别 json")
    @TableField("rankJson")
    private String rankJson;

}
