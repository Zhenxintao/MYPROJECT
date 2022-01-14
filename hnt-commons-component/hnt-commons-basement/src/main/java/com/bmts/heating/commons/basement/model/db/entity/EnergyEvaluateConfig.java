package com.bmts.heating.commons.basement.model.db.entity;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode
@Accessors(chain = true)
@TableName(value = "energy_evaluate_config",autoResultMap = true)
public class EnergyEvaluateConfig implements Serializable {

	private static final long serialVersionUID = 1L;


	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	@ApiModelProperty("基准值")
	@TableField("referenceVal")
	private BigDecimal referenceVal;

	@ApiModelProperty("评价区间")
	@TableField(typeHandler = FastjsonTypeHandler.class)
	private JSON evaluate;

	@ApiModelProperty("室外温度")
	@TableField("temperature")
	private int temperature;

	@ApiModelProperty("合格区间")
	@TableField(typeHandler = FastjsonTypeHandler.class)
	private JSON qualified;

	@ApiModelProperty("能耗数据类型")
	@TableField("energyType")
	private int energyType;
}
