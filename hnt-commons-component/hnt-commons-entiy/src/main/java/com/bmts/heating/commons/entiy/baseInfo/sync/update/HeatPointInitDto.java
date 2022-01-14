package com.bmts.heating.commons.entiy.baseInfo.sync.update;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author naming
 * @description
 * @date 2021/6/2 13:42
 **/
@ApiModel("参量同步实体")
@Data
public class HeatPointInitDto {

    /**
     * 同步编号
     */
    @ApiModelProperty(value = "同步编号", required = true)
    private String num;

    /**
     * 同步父级编号
     */
    @ApiModelProperty(value = "同步父级编号", required = true)
    private BigInteger parentNum;

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
     * 是否报警
     */
    @ApiModelProperty("是否报警")
    private Boolean isAlarm;

    /**
     * 报警值 单选  0 和 1
     */
    @ApiModelProperty("报警值 单选  0 和 1")
    private Integer alarmValue;


    /**
     * 高限
     */
    @ApiModelProperty("高限")
    private Integer upperBound;

    /**
     * 低限
     */
    @ApiModelProperty("低限")
    private Integer lowerBound;


    /**
     * 参量名称
     */
    @ApiModelProperty(value = "参量名称", required = true)
    private String pointColumnName;


    @ApiModelProperty("所属类型：1：热力站   2：热源")
    private Integer level;

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
     * 报警级别 和描述
     */
    @ApiModelProperty("报警级别 和描述")
    private String rankJson;

}
