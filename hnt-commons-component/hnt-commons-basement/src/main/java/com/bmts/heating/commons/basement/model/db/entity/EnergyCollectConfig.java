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

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
@ApiModel("采集能耗点计算配置类")
@TableName("energy_collect_config")
public class EnergyCollectConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	@ApiModelProperty("系统Id")
	@TableField("relevanceId")
	private Integer relevanceId;

	@ApiModelProperty("目标计算量pointId")
	@TableField("pointTargetId")
	private Integer pointTargetId;

	@ApiModelProperty("目标计算量名称")
	@TableField("pointTargetName")
	private String pointTargetName;

	@ApiModelProperty("jep表达式")
	@TableField("expression")
	private String expression;

	@ApiModelProperty("是否参与计算")
	@TableField("isConverge")
	private Boolean isConverge;

	@ApiModelProperty("计算方式1:默认 2:分区占比")
	@TableField("computeType")
	private Integer computeType;

	@ApiModelProperty("jep代数标识")
	@TableField("sign")
	private String sign;

	@ApiModelProperty("关联参量标识")
	@TableField("columnName")
	private String columnName;
}
