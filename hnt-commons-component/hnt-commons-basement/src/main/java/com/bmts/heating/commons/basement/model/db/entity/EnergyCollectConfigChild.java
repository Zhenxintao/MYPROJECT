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
@ApiModel("采集能耗点计算配置类子类")
@TableName("energy_collect_config_child")
public class EnergyCollectConfigChild implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	@ApiModelProperty("系统Id")
	@TableField("relevanceId")
	private Integer relevanceId;

	@ApiModelProperty("目标计算量targetId")
	@TableField("targetId")
	private Integer targetId;

	@ApiModelProperty("jep代数标识")
	@TableField("sign")
	private String sign;

	@ApiModelProperty("对应pointId")
	@TableField("pointId")
	private Integer pointId;

	@ApiModelProperty("是否参是常量 ")
	@TableField("status")
	private Boolean status;

	@ApiModelProperty("常量值,可为null")
	@TableField("val")
	private Double val;

	@ApiModelProperty("关联参量标识")
	@TableField("columnName")
	private String columnName;

}
