package com.bmts.heating.commons.utils.es;

import lombok.Data;

@Data
public class EnergyChild {

	//单位时间能耗量
	private Double consumption;

	//单位时间单耗
	private Double unitConsumption;

	//初码
	private Double beforeValue;

	//末码
	private Double realValue;

	//单位
	private String unit;


}
