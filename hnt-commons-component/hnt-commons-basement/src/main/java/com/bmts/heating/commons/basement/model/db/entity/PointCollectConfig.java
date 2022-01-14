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
 * @author naming
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pointCollectConfig")
@ApiModel("采集量实体")
public class PointCollectConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 开始字节
     */
    @ApiModelProperty("开始字节")
    @TableField("startByte")
    private Integer startByte;

    /**
     * 数据长度 1.单字节整数 2.双字节整数 3.有符号整数 4.带符号整形 5.长整型 6.浮点数
     */
    @ApiModelProperty("数据长度 1.单字节整数 2.双字节整数 3.有符号整数 4.带符号整形 5.长整型 6.浮点数")
    @TableField(exist = false)
    private Integer dataLengthType;

    /**
     * 数据类型 1.参数是实际原值*1进行传输 2.*2传输 3.*100传输 4.*1000 传输 5.DI开关量 6.采集时间 7.巡岗时间
     */
    @ApiModelProperty("数据类型 1.参数是实际原值*1进行传输 2.*2传输 3.*100传输 4.*1000 传输 5.DI开关量 6.采集时间 7.巡岗时间")
    @TableField("dataType")
    private Integer dataType;

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
     * 量程低报警
     */
    @ApiModelProperty("量程低报警")
    @TableField("rangeLower")
    private BigDecimal rangeLower;
    @ApiModelProperty("量程高报警")
    @TableField("rangeHigh")
    private BigDecimal rangeHigh;
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
     * 是否确认报警
     */
    @ApiModelProperty("是否确认报警")
    @TableField("isConfirmAlarm")
    private Boolean isConfirmAlarm;

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

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    @TableField("address")
    private String address;

    /**
     * 传输组数
     */
    @ApiModelProperty("传输组数")
    @TableField("transferGroup")
    private Integer transferGroup;
    
//    /**
//     * 点地址
//     */
//    @ApiModelProperty("点地址")
//    @TableField("pointAddress")
//    private String pointAddress;

    /**
     * 数据类型 1-Bool 2-Int 3-Uint 4-Long 5-ULong 6-Float 7-Double
     */
    @ApiModelProperty("数据类型 1-Bool 2-Int 3-Uint 4-Long 5-ULong 6-Float 7-Double")
    private Integer type;

    /**
     * 设备Id 从字典表里取
     */
    @ApiModelProperty("设备Id 从字典表里取")
    @TableField("deviceConfigId")
    private Integer deviceConfigId;

    /**
     * 字节序
     */
    @ApiModelProperty("字节序")
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
    @ApiModelProperty("关联参量id")
    @TableField("pointStandardId")
    private Integer pointStandardId;

    /**
     * 系统分区/机组 id
     */
    @ApiModelProperty("系统分区/机组 id")
    @TableField("heatSystemId")
    private Integer heatSystemId;

    /**
     * 扩展字段
     */
    @ApiModelProperty("扩展字段")
    @TableField("expandDesc")
    private String expandDesc;


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


    @ApiModelProperty("用户id")
    @TableField("userId")
    private Integer userId;

}
