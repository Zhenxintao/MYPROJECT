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

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
@ApiModel("能耗单价设置")
@TableName("energy_price")
public class EnergyPrice implements Serializable {

	private static final long serialVersionUID = 1L;


	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	@ApiModelProperty("水单价")
	@TableField("waterPrice")
	private BigDecimal waterPrice;

	@ApiModelProperty("电单价")
	@TableField("electricityPrice")
	private BigDecimal electricityPrice;

	@ApiModelProperty("热单价")
	@TableField("heatPrice")
	private BigDecimal heatPrice;

	@ApiModelProperty("供暖季")
	@TableField("commonSeasonId")
	private Integer commonSeasonId;

	@ApiModelProperty("收费类型")
	@TableField("chargeTypeId")
	private Integer chargeTypeId;


}
