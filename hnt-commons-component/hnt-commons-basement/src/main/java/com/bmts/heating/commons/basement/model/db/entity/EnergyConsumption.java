package com.bmts.heating.commons.basement.model.db.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmts.heating.commons.basement.config.ExcelColumn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@ApiModel("能耗统计类")
@Data
@TableName("energyconsumption")
public class EnergyConsumption implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("id")
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	@ApiModelProperty("名称")
	@ExcelColumn(value = "换热站名/热源名", col = 1)
	@TableField(value = "name")
	private String name;

	@ApiModelProperty("值")
	@ExcelColumn(value = "本期电耗/水耗/热耗", col = 2)
	@TableField(value = "value")
	private Double value;

	@ApiModelProperty("是否参与计算  1 参与 2 不参与")
	//@ExcelColumn(value = "是否参与计算", col = 3)
	@TableField(value = "isCaculate")
	private Integer isCaculate;

	@ApiModelProperty("类型 1 热源 2 热力站")
	//@ExcelColumn(value = "类型", col = 4)
	@TableField(value = "type")
	private Integer type;

	@ApiModelProperty("创建时间")
	@TableField(value = "createTime")
	private LocalDateTime createTime;


	@ApiModelProperty("开始时间")
	@TableField(value = "startTime")
	private LocalDateTime startTime;

	@ApiModelProperty("结束时间")
	@TableField(value = "endTime")
	private LocalDateTime endTime;

	@ApiModelProperty("能耗类型 1 电 2 水 3 热")
	//@ExcelColumn(value = "能耗类型", col = 5)
	@TableField("energyType")
	private Integer energyType;

	@ApiModelProperty("路径")
	@TableField("url")
	private String url;

}
