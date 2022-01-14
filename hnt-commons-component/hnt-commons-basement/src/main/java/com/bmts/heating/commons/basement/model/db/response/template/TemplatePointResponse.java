package com.bmts.heating.commons.basement.model.db.response.template;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("模板点返回结果集")
public class TemplatePointResponse {

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
     * 显示顺序
     */
    @TableField("showSort")
    private Integer showSort;

    /**
     * 是否报警
     */
    @TableField("isAlarm")
    private Boolean isAlarm;

    /**
     * 是否取反
     */
    @TableField("isNegation")
    private Boolean isNegation;

    /**
     * 描述性信息 例如 0.表示什么 1.表示什么
     */
    @TableField("DescriptionJson")
    private String DescriptionJson;

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

//	/**
//	 * 点地址
//	 */
//	@TableField("pointAddress")
//	private String pointAddress;

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
    @TableField("washArray")
    private String washArray;

    /**
     * 关联参量id
     */
    @TableField("pointStandardId")
    private Integer pointStandardId;

    /**
     * 扩展字段
     */
    @TableField("expandDesc")
    private String expandDesc;

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

    private String description;

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

    /**
     * 高限
     */
    @TableField("upperBound")
    private Integer upperBound;

    /**
     * 低限
     */
    @TableField("lowerBound")
    private Integer lowerBound;

    /**
     * 修正值
     */
    private Integer correction;

    /**
     * 数据类型
     */
    @TableField("dataType")
    private Integer dataType;

    /**
     * 下行清洗策略
     */
    @TableField("washDArray")
    private String washDArray;

    /**
     * 分类标志
     */
    @TableField("sortFlag")
    private String sortFlag;

    /**
     * 模板Id
     */
    @TableField("pointTemplateConfigId")
    private Integer pointTemplateConfigId;

    @ApiModelProperty("参量名称")
    private String pointStandardName;

    @ApiModelProperty("标签名称")
    private String columnName;

    @ApiModelProperty("网测类型：0. 公用 1.一次侧 2.二测测")
    private Integer netFlag;

    private int pointConfig;

}
