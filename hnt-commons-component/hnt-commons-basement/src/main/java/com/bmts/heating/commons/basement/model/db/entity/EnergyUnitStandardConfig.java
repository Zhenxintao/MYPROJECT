package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
@ApiModel("采集能耗点倍率设置")
@TableName("energy_unit_standard_config")
public class EnergyUnitStandardConfig implements Serializable {

	private static final long serialVersionUID = 1L;


	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	@ApiModelProperty("折标室外温度")
	@TableField("outdoorTemp")
	private BigDecimal outdoorTemp;

	@ApiModelProperty("室内温度")
	@TableField("indoorTemp")
	private BigDecimal indoorTemp;

	@ApiModelProperty("操作时间")
	@TableField("dateTime")
	private LocalDateTime dateTime;

	@ApiModelProperty("状态")
	private Boolean state;


}
