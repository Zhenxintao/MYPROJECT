//package com.bmts.heating.commons.basement.model.db.response.template;
//
//import com.baomidou.mybatisplus.annotation.TableField;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//@Data
//@ApiModel("采集量模板返回结果集")
//public class TemplateCollectResponse {
//
//    private Integer id;
//
//    /**
//     * 开始字节
//     */
//    @ApiModelProperty("开始字节")
//    @TableField("startByte")
//    private Integer startByte;
//
//    /**
//     * 数据长度 1.单字节整数 2.双字节整数 3.有符号整数 4.带符号整形 5.长整型 6.浮点数
//     */
////	@ApiModelProperty("数据长度 1.单字节整数 2.双字节整数 3.有符号整数 4.带符号整形 5.长整型 6.浮点数")
////	@TableField("dataLengthType")
////	private Integer dataLengthType;
//
//    /**
//     * 数据类型 1.参数是实际原值*1进行传输 2.*2传输 3.*100传输 4.*1000 传输 5.DI开关量 6.采集时间 7.巡岗时间
//     */
//    @ApiModelProperty("数据类型 1.参数是实际原值*1进行传输 2.*2传输 3.*100传输 4.*1000 传输 5.DI开关量 6.采集时间 7.巡岗时间")
//    @TableField("dataType")
//    private Integer dataType;
//
//    /**
//     * 事故低报警
//     */
//    @ApiModelProperty("事故低报警")
//    @TableField("accidentLower")
//    private BigDecimal accidentLower;
//
//    /**
//     * 事故高报警
//     */
//    @ApiModelProperty("事故高报警")
//    @TableField("accidentHigh")
//    private BigDecimal accidentHigh;
//
//    /**
//     * 运行低报警
//     */
//    @ApiModelProperty("运行低报警")
//    @TableField("runningLower")
//    private BigDecimal runningLower;
//
//    /**
//     * 运行高报警
//     */
//    @ApiModelProperty("运行高报警")
//    @TableField("runningHigh")
//    private BigDecimal runningHigh;
//
//    /**
//     * 量程高报警
//     */
//    @ApiModelProperty("量程高报警")
//    @TableField("rangeHigh")
//    private BigDecimal rangeHigh;
//
//    /**
//     * 量程低报警
//     */
//    @ApiModelProperty("量程低报警")
//    @TableField("rangeLower")
//    private BigDecimal rangeLower;
//
//    /**
//     * 显示顺序
//     */
//    @ApiModelProperty("显示顺序")
//    @TableField("showSort")
//    private Integer showSort;
//
//    /**
//     * 是否报警
//     */
//    @ApiModelProperty("是否报警")
//    @TableField("isAlarm")
//    private Boolean isAlarm;
//
//    /**
//     * 是否确认报警
//     */
//    @ApiModelProperty("是否确认报警")
//    @TableField("isConfirmAlarm")
//    private Boolean isConfirmAlarm;
//
//    /**
//     * 传输组数
//     */
//    @ApiModelProperty("传输组数")
//    @TableField("transferGroup")
//    private Integer transferGroup;
//
//    /**
//     * 地址
//     */
//    @ApiModelProperty("地址")
//    @TableField("address")
//    private String address;
//
//    /**
//     * 点地址
//     */
//    @ApiModelProperty("点地址")
//    @TableField("pointAddress")
//    private String pointAddress;
//
//    /**
//     * 数据类型 1-Bool 2-Int 3-Uint 4-Long 5-ULong 6-Float 7-Double
//     */
//    @ApiModelProperty("数据类型 1-Bool 2-Int 3-Uint 4-Long 5-ULong 6-Float 7-Double")
//    @TableField("type")
//    private Integer type;
//
//    /**
//     * 设备Id 从字典表里取
//     */
//    @ApiModelProperty("设备Id 从字典表里取")
//    @TableField("deviceConfigId")
//    private Integer deviceConfigId;
//
//    /**
//     * 字节序
//     */
//    @ApiModelProperty("字节序")
//    @TableField("orderValue")
//    private String orderValue;
//
//    /**
//     * 清洗规则
//     */
//    @ApiModelProperty("清洗规则")
//    @TableField("washArray")
//    private String washArray;
//
//
//    /**
//     * 扩展字段
//     */
//    @ApiModelProperty("扩展字段")
//    @TableField("expandDesc")
//    private String expandDesc;
//
//    /**
//     * 关联参量id
//     */
//    @ApiModelProperty("关联参量id")
//    @TableField("pointStandardId")
//    private Integer pointStandardId;
//
//    /**
//     * 关联 模板id
//     */
//    @ApiModelProperty("关联 模板id")
//    @TableField("pointTemplateConfigId")
//    private Integer pointTemplateConfigId;
//
//    /**
//     * 单位
//     */
//    @ApiModelProperty("单位")
//    @TableField("pointUnitId")
//    private Integer pointUnitId;
//
//    /**
//     * 参量类型
//     */
//    @ApiModelProperty("参量类型")
//    @TableField("pointParameterTypeId")
//    private Integer pointParameterTypeId;
//
//    /**
//     * 创建人
//     */
//    @ApiModelProperty("创建人")
//    @TableField("createUser")
//    private String createUser;
//
//    /**
//     * 创建时间
//     */
//    @ApiModelProperty("创建时间")
//    @TableField("createTime")
//    private LocalDateTime createTime;
//
//    /**
//     * 修改人
//     */
//    @ApiModelProperty("修改人")
//    @TableField("updateUser")
//    private String updateUser;
//
//    /**
//     * 修改时间
//     */
//    @ApiModelProperty("修改时间")
//    @TableField("updateTime")
//    private LocalDateTime updateTime;
//
//    private String description;
//
//    /**
//     * 删除标识
//     */
//    @ApiModelProperty("删除标识")
//    @TableField("deleteFlag")
//    private Boolean deleteFlag;
//
//    /**
//     * 删除时间
//     */
//    @ApiModelProperty("删除时间")
//    @TableField("deleteTime")
//    private LocalDateTime deleteTime;
//
//    /**
//     * 删除人
//     */
//    @ApiModelProperty("删除人")
//    @TableField("deleteUser")
//    private String deleteUser;
//
//    @ApiModelProperty("参量名称")
//    private String pointStandardName;
//
//    @ApiModelProperty("标签名称")
//    private String columnName;
//
//    @ApiModelProperty("网测类型：0. 公用 1.一次侧 2.二测测")
//    private Integer netFlag;
//
//    private Integer pointConfig;
//}
