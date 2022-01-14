package com.bmts.heating.commons.basement.model.db.response;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("采集量列表返回实体")
public class PointCollectConfigResponse {
    private static final long serialVersionUID = 1L;

    private Integer id;


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
     * 显示顺序
     */
    @ApiModelProperty("显示顺序")
    @TableField("showSort")
    private Integer showSort;

    /**
     * 是否报警
     */
    @ApiModelProperty("是否报警")
    @TableField("isAlarm")
    private Boolean isAlarm;

    /**
     * 是否取反
     */
    @ApiModelProperty("是否取反")
    @TableField("isNegation")
    private Boolean isNegation;

    /**
     * 描述性信息 例如 0.表示什么 1.表示什么
     */
    @ApiModelProperty("描述性信息 例如 0.表示什么 1.表示什么")
    @TableField("descriptionJson")
    private String descriptionJson;


    /**
     * 报警值 单选  0 和 1
     */
    @ApiModelProperty("报警值 单选  0 和 1")
    @TableField("alarmValue")
    private Short alarmValue;

    /**
     * 报警外键
     */
    @ApiModelProperty("报警外键")
    @TableField("alarmConfigId")
    private Integer alarmConfigId;

//    /**
//     * 点地址
//     */
//    @TableField("pointAddress")
//    private String pointAddress;

    /**
     * 数据类型 1-Bool 2-Int 3-Uint 4-Long 5-ULong 6-Float 7-Double
     */
    private Integer dataLengthType;

    /**
     * 设备Id 从字典表里取
     */
    @TableField("deviceConfigId")
    private Integer deviceConfigId;

    /**
     * 字节序
     */
    @TableField("orderValue")
    private String orderValue;

    /**
     * 上行清洗策略
     */
    @ApiModelProperty("上行清洗策略")
    @TableField("washArray")
    private String washArray;

    /**
     * 关联参量id
     */
    @TableField("pointStandardId")
    private Integer pointStandardId;

    /**
     * 系统分区/机组 id
     */
    @TableField("heatSystemId")
    private Integer heatSystemId;

    /**
     * 扩展字段
     */
    @TableField("expandDesc")
    private String expandDesc;

//    /**
//     * 单位
//     */
//    @ApiModelProperty("单位")
//    private Integer pointUnitId;

    /**
     * 参量类型
     */
    @ApiModelProperty("参量类型")
    private Integer pointParameterTypeId;

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

    @ApiModelProperty("描述")
    private String description;

    /**
     * 删除标识
     */
    @ApiModelProperty("删除标识")
    @TableField("deleteFlag")
    private Boolean deleteFlag;

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

}
