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
@ApiModel("配置点详情")
public class PointConfigResponse {
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

//    /**
//     * 显示顺序
//     */
//    @TableField("showSort")
//    private Integer showSort;

    /**
     * 是否报警
     */
    @TableField("isAlarm")
    private Boolean isAlarm;

//    /**
//     * 是否取反
//     */
//    @TableField("isNegation")
//    private Boolean isNegation;
//
//
//    /**
//     * 描述性信息 例如 0.表示什么 1.表示什么
//     */
//    @ApiModelProperty("描述性信息 例如 0.表示什么 1.表示什么")
//    @TableField("descriptionJson")
//    private String descriptionJson;

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

    /**
     * 创建人
     */
    @TableField("createUser")
    private String createUser;

    /**
     * 创建时间
     */
    @TableField("createTime")
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

//    private String description;

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
//
//    /**
//     * 数据类型
//     */
//    @TableField("dataType")
//    private Integer dataType;
//
//    /**
//     * 下行清洗策略
//     */
//    @TableField("washDArray")
//    private String washDArray;
//
//    /**
//     * 分类标志
//     */
//    @TableField("sortFlag")
//    private String sortFlag;

    /**
     * 关联 pointAlarm表的 id
     */
    @ApiModelProperty("关联 pointAlarm表的 id")
    private Integer pointAlarmId;

    @ApiModelProperty("报警类型名称")
    private String alarmConfigName;

    @ApiModelProperty("数据类型： 1-Bool 2-Int 3-Uint 4-Long 5-ULong 6-Float 7-Double")
    private Integer dataType;

    /**
     * 报警级别
     */
    @ApiModelProperty("报警级别")
    private Integer grade;

    /**
     * 报警描述
     */
    @ApiModelProperty("报警描述")
    private String alarmDesc;


    /**
     * 报警级别 json
     */
    @ApiModelProperty("报警级别 json")
    private String rankJson;


    /*---标准点信息---*/
    @ApiModelProperty("关联参量")
    private String pointStandardName;

    @ApiModelProperty("标签名称")
    private String columnName;

    @ApiModelProperty("")
    private String nodeCode;

    @ApiModelProperty("单位Id")
    private String pointUnitId;

    @ApiModelProperty("单位名称")
    private String unitValue;

    @ApiModelProperty("网侧类型")
    private String netFlag;
    /*———关联组织信息———*/
    @ApiModelProperty("系统分区/机组 名称")
    private String heatSystemName;

    @ApiModelProperty("换热站id")
    private Integer heatTransferStationId;

    @ApiModelProperty("换热站 名称")
    private String heatTransferStationName;

    @ApiModelProperty("热源id")
    private Integer heatSourceId;

    @ApiModelProperty("热源 名称")
    private String heatSourceName;

    @ApiModelProperty("热网id")
    private Integer heatNetId;

    @ApiModelProperty("热网名称")
    private String heatNetName;

    @ApiModelProperty("控制柜id")
    private Integer heatCabinetId;

    @ApiModelProperty("控制柜名称")
    private String heatCabinetName;

    @ApiModelProperty("参量类型名称")
    private String pointParameterTypeName;
}
