package com.bmts.heating.commons.basement.model.db.response;

import lombok.Data;

@Data
public class EnergyNodeSource {

	/**
	 * 站、源id
	 */
	private Integer masterId;

	/**
	 * 目标计算量id
	 */
	private Integer id;
	/**
	 * 目标计算量所属系统id
	 */
	private Integer relevanceId;
	/**
	 * 目标计算量pointId
	 */
	private Integer pointTargetId;
	/**
	 * 目标计算量名称
	 */
	private String pointTargetName;
	/**
	 * 目标计算量名称
	 */
	private String columnName;
	/**
	 * jep表达式
	 */
	private String expression;
	/**
	 * 是否有效
	 */
	private Boolean isConverge;
	/**
	 * 计算方式 1:默认计算 2:分区占比
	 */
	private Integer computeType;
	/**
	 * jep表达式标识
	 */
	private String sign;
}
