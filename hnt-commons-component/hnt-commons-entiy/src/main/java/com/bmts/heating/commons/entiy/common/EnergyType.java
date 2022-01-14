package com.bmts.heating.commons.entiy.common;

public enum EnergyType {

	/*
	水
	 */
	WATER(1),
	/*
	电
	 */
	ELECTRICITY(2),
	/*
	热
	 */
	HEAT(3);

	private final int index;

	EnergyType(int index) {
		this.index = index;
	}

	public int type() {
		return index;
	}
}
