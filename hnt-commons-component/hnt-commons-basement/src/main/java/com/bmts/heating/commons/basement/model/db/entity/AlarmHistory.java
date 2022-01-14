package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2020-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("alarmHistory")
public class AlarmHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "Id", type = IdType.AUTO)
    private Integer Id;

    /**
     * 报警级别
     */
    @TableField("grade")
    private Integer grade;

    /**
     * 报警点名称
     */
    @TableField("pointName")
    private String pointName;

    /**
     * 报警点标签名称
     */
    @TableField("columnName")
    private String columnName;

    /**
     * 报警时间
     */
    @TableField("alarmTime")
    private LocalDateTime alarmTime;

    /**
     * 确认时间
     */
    @TableField("confirmTime")
    private LocalDateTime confirmTime;

    /**
     * 恢复时间
     */
    @TableField("destroyTime")
    private LocalDateTime destroyTime;

    /**
     * 报警描述
     */
    @TableField("alarmDesc")
    private String alarmDesc;

    /**
     * 确认人
     */
    @TableField("confirmUser")
    private String confirmUser;

    /**
     * 系统id
     */
    @TableField("heatSystemId")
    private Integer heatSystemId;

    /**
     * 具体地址
     */
    @TableField("address")
    private String address;

    /**
     * 值班人
     */
    @TableField("dutyUser")
    private String dutyUser;

    /**
     * 换热站Id 或热源id
     */
    @TableField("relevanceId")
    private Integer relevanceId;

    /**
     * 换热站名称或热源名称
     */
    @TableField("relevanceName")
    private String relevanceName;

    /**
     * 类型  1.热力站 2.热源
     */
    @TableField("type")
    private Integer type;

    /**
     * 报警值
     */
    @TableField("alarmValue")
    private BigDecimal alarmValue;

    /**
     * 恢复值
     */
    @TableField("recoverValue")
    private BigDecimal recoverValue;

    /**
     * 状态：1 报警 ，2 确认 ，3 恢复
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("updateTime")
    private LocalDateTime updateTime;

    /**
     * 报警级别 json
     */
    @ApiModelProperty("报警级别 json")
    @TableField("rankJson")
    private String rankJson;

}
