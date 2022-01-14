package com.bmts.heating.commons.entiy.baseInfo.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("模板点信息")
public class TemplatePoint {

    private static final long serialVersionUID = 1L;

    /**
     * 事故低报警
     */
    private BigDecimal accidentLower;

    /**
     * 事故高报警
     */
    private BigDecimal accidentHigh;

    /**
     * 运行低报警
     */
    private BigDecimal runningLower;

    /**
     * 运行高报警
     */
    private BigDecimal runningHigh;

    /**
     * 显示顺序
     */
    private Integer showSort;

    /**
     * 是否报警
     */
    private Boolean isAlarm;

    /**
     * 是否取反
     */
    private Boolean isNegation;

    /**
     * 描述性信息 例如 0.表示什么 1.表示什么
     */
    private String DescriptionJson;

    /**
     * 报警值 单选  0 和 1
     */
    private Integer alarmValue;

    /**
     * 报警外键
     */
    private Integer alarmConfigId;

//	/**
//	 * 点地址
//	 */
//	private String pointAddress;

    /**
     * 设备Id 从字典表里取
     */
    private Integer deviceConfigId;

    /**
     * 字节序
     */
    private String orderValue;

    /**
     * 上行清洗策略
     */
    private String washArray;

    /**
     * 关联参量id
     */
    private Integer pointStandardId;

    /**
     * 扩展字段
     */
    private String expandDesc;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    private String updateUser;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    private String description;

    /**
     * 删除标识
     */
    private Boolean deleteFlag;

    /**
     * 删除时间
     */
    private LocalDateTime deleteTime;

    /**
     * 删除人
     */
    private String deleteUser;

    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 默认值1属于系统 2.控制柜 3.站 4.源 5.网
     */
    private Integer level;

    /**
     * 高限
     */
    private Integer upperBound;

    /**
     * 低限
     */
    private Integer lowerBound;

    /**
     * 修正值
     */
    private Integer correction;

    /**
     * 数据类型
     */
    private Integer dataType;

    /**
     * 下行清洗策略
     */
    private String washDArray;

    /**
     * 分类标志
     */
    private String sortFlag;

    /**
     * 模板Id
     */
    private Integer pointTemplateConfigId;
}

