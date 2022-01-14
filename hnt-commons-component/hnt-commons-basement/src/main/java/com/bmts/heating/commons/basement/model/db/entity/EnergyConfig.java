package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class EnergyConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 关联参量Id
	 */
	@ApiModelProperty("关联参量Id")
	@TableField("pointStandardId")
	private Integer pointStandardId;

	/**
	 * 是否参与汇聚
	 */
	@ApiModelProperty("是否参与汇聚")
	@TableField("isConverge")
	private Boolean isConverge;

	/**
	 * 汇聚方式
	 */
	@ApiModelProperty("汇聚方式")
	@TableField("convergeId")
	private Integer convergeId;


	@ApiModelProperty("能耗类型1:热 2:电 3:水")
	@TableField("type")
	private Integer type;


}
