package com.bmts.heating.commons.entiy.baseInfo.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
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
 * @since 2020-11-16
 */
@Data
@Accessors(chain = true)
@ApiModel("采集量模板")
public class TemplateCollect implements Serializable {

    private static final long serialVersionUID = 1L;


    private Integer id;

    /**
     * 开始字节
     */
    @ApiModelProperty("开始字节")
    private Integer startByte;

    /**
     * 数据长度 1.单字节整数 2.双字节整数 3.有符号整数 4.带符号整形 5.长整型 6.浮点数
     */
//    @ApiModelProperty("数据长度 1.单字节整数 2.双字节整数 3.有符号整数 4.带符号整形 5.长整型 6.浮点数")
//    private Integer dataLengthType;

    /**
     * 数据类型 1.参数是实际原值*1进行传输 2.*2传输 3.*100传输 4.*1000 传输 5.DI开关量 6.采集时间 7.巡岗时间
     */
    @ApiModelProperty("数据类型 1.参数是实际原值*1进行传输 2.*2传输 3.*100传输 4.*1000 传输 5.DI开关量 6.采集时间 7.巡岗时间")
    private Integer dataType;

    /**
     * 事故低报警
     */
    @ApiModelProperty("事故低报警")
    private BigDecimal accidentLower;

    /**
     * 事故高报警
     */
    @ApiModelProperty("事故高报警")
    private BigDecimal accidentHigh;

    /**
     * 运行低报警
     */
    @ApiModelProperty("运行低报警")
    private BigDecimal runningLower;

    /**
     * 运行高报警
     */
    @ApiModelProperty("运行高报警")
    private BigDecimal runningHigh;

    /**
     * 量程高报警
     */
    @ApiModelProperty("量程高报警")
    private BigDecimal rangeHigh;

    /**
     * 量程低报警
     */
    @ApiModelProperty("量程低报警")
    private BigDecimal rangeLower;

    /**
     * 显示顺序
     */
    @ApiModelProperty("显示顺序")
    private Integer showSort;

    /**
     * 是否报警
     */
    @ApiModelProperty("是否报警")
    private Boolean isAlarm;

    /**
     * 是否确认报警
     */
    @ApiModelProperty("是否确认报警")
    private Boolean isConfirmAlarm;

    /**
     * 传输组数
     */
    @ApiModelProperty("传输组数")
    private Integer transferGroup;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String address;

//    /**
//     * 点地址
//     */
//    @ApiModelProperty("点地址")
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
    private Integer deviceConfigId;

    /**
     * 字节序
     */
    @ApiModelProperty("字节序")
    private String orderValue;

    /**
     * 清洗规则
     */
    @ApiModelProperty("清洗规则")
    private String washArray;


    /**
     * 扩展字段
     */
    @ApiModelProperty("扩展字段")
    private String expandDesc;

    /**
     * 关联参量id
     */
    @ApiModelProperty("关联参量id")
    private Integer pointStandardId;

    /**
     * 关联 模板id
     */
    @ApiModelProperty("关联 模板id")
    private Integer pointTemplateConfigId;


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

    private String description;

    /**
     * 删除标识
     */
    @ApiModelProperty("删除标识")
    private Boolean deleteFlag;

    /**
     * 删除时间
     */
    @ApiModelProperty("删除时间")
    private LocalDateTime deleteTime;

    /**
     * 删除人
     */
    @ApiModelProperty("删除人")
    private String deleteUser;


    @ApiModelProperty("用户id")
    private Integer userId;
}
