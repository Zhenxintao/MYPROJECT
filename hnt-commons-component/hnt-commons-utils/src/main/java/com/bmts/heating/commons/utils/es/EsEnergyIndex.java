package com.bmts.heating.commons.utils.es;

public enum EsEnergyIndex {

	//能耗汇聚数据
	ENERGY_CONVERGE_HOUR("energy_converge_hour"),
	ENERGY_CONVERGE_DAY("energy_converge_day");



	EsEnergyIndex(String index) {
		this.index = index;
	}

	private final String index;

	public String getIndex() {
		return index;
	}
}
