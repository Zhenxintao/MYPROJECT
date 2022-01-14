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

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
@ApiModel("采集能耗点倍率设置")
@TableName("energy_rate_config")
public class EnergyRateConfig implements Serializable {

	private static final long serialVersionUID = 1L;


	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	@ApiModelProperty("热源、热力站Id")
	@TableField("targetId")
	private Integer targetId;

	@ApiModelProperty("关联机组Id")
	@TableField("relevanceId")
	private BigDecimal relevanceId;

	@ApiModelProperty("水倍率")
	@TableField("waterRate")
	private BigDecimal waterRate;

	@ApiModelProperty("电倍率")
	@TableField("electricityRate")
	private BigDecimal electricityRate;

	@ApiModelProperty("热倍率")
	@TableField("heatRate")
	private BigDecimal heatRate;

	@ApiModelProperty("类型-热源:4 热力站:3")
	private Integer type;

	@ApiModelProperty("创建用户")
	@TableField("createUser")
	private Integer createUser;

	@ApiModelProperty("状态")
	private Boolean status;

	@ApiModelProperty("创建时间")
	@TableField("createTime")
	private LocalDateTime createTime;

	@ApiModelProperty("更新时间")
	@TableField("updateTime")
	private LocalDateTime updateTime;

	@ApiModelProperty("操作用户")
	@TableField("updateUser")
	private Integer updateUser;

	@ApiModelProperty("收费类型")
	@TableField("chargeTypeId")
	private Integer chargeTypeId;
}
