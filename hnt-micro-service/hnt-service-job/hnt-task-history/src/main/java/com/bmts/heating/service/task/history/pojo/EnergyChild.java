package com.bmts.heating.service.task.history.pojo;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
public class EnergyChild {

	/**
	 * 单位时间能耗量
	 */
	@Field(type = FieldType.Double)
	private Double consumption;

	/**
	 * 单位时间单耗量
	 */
	@Field(type = FieldType.Double)
	private Double unitConsumption;

	/**
	 * 初码
	 */
	@Field(type = FieldType.Double)
	private Double beforeValue;

	/**
	 * 末码
	 */
	@Field(type = FieldType.Double)
	private Double realValue;

	public boolean verify(){
		return realValue == null || beforeValue == null;
	}

}
