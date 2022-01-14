package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
@TableName("pointConfigSourceView")
public class PointConfigSourceView implements Serializable {

    private static final long serialVersionUID = 1L;

//    /**
//     * 事故低报警
//     */
//    @TableField("accidentLower")
//    private BigDecimal accidentLower;
//
//    /**
//     * 事故高报警
//     */
//    @TableField("accidentHigh")
//    private BigDecimal accidentHigh;
//
//    /**
//     * 运行低报警
//     */
//    @TableField("runningLower")
//    private BigDecimal runningLower;
//
//    /**
//     * 运行高报警
//     */
//    @TableField("runningHigh")
//    private BigDecimal runningHigh;
//
//
//    /**
//     * 是否报警
//     */
//    @TableField("isAlarm")
//    private Boolean isAlarm;

//    /**
//     * 是否取反
//     */
//    @TableField("isNegation")
//    private Boolean isNegation;

//    /**
//     * 描述性信息 例如 0.表示什么 1.表示什么
//     */
//    @TableField("descriptionJson")
//    private String descriptionJson;

//    /**
//     * 报警值 单选  0 和 1
//     */
//    @TableField("alarmValue")
//    private Integer alarmValue;
//
//    /**
//     * 报警外键
//     */
//    @TableField("alarmConfigId")
//    private Integer alarmConfigId;

//    /**
//     * 点地址
//     */
//    @TableField("pointAddress")
//    private String pointAddress;


//    /**
//     * 字节序
//     */
//    @TableField("orderValue")
//    private String orderValue;

//    /**
//     * 上行清洗策略
//     */
//    @TableField("washArray")
//    private String washArray;

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
//     * 扩展字段
//     */
//    @TableField("expandDesc")
//    private String expandDesc;
//
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
//
//    /**
//     * 修改时间
//     */
//    @TableField("updateTime")
//    private LocalDateTime updateTime;

//    private String description;

//    /**
//     * 删除标识
//     */
//    @TableField("deleteFlag")
//    private Boolean deleteFlag;
//
//    /**
//     * 删除时间
//     */
//    @TableField("deleteTime")
//    private LocalDateTime deleteTime;
//
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

//    /**
//     * 高限
//     */
//    @TableField("upperBound")
//    private Integer upperBound;
//
//    /**
//     * 低限
//     */
//    @TableField("lowerBound")
//    private Integer lowerBound;
//
//    /**
//     * 修正值
//     */
//    private Integer correction;

//    /**
//     * 数据类型
//     */
//    @TableField("dataType")
//    private Integer dataType;

//    /**
//     * 下行清洗策略
//     */
//    @TableField("washDArray")
//    private String washDArray;

//    /**
//     * 分类标志
//     */
//    @TableField("sortFlag")
//    private String sortFlag;

    /**
     * 标签名称
     */
    @TableField("columnName")
    private String columnName;

    @TableField("sourceId")
    private Integer sourceId;

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
     * 热源同步编号
     */
    @TableField("sourceSyncNum")
    private Integer sourceSyncNum;

    /**
     * 系统编号
     */
    @TableField("systemNumber")
    private Integer systemNumber;

}
