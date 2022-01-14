package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ComputeConfig implements Serializable {

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 计算量所需采集量id
	 */
	@ApiModelProperty("计算量所需采集量id")
	@TableField("pointStandardId")
	private Integer pointStandardId;

	/**
	 * 计算量id
	 */
	@ApiModelProperty("计算量id")
	@TableField("computeId")
	private Integer computeId;

	public ComputeConfig(Integer pointStandardId,Integer computeId){
		this.pointStandardId = pointStandardId;
		this.computeId = computeId;
	}
}
