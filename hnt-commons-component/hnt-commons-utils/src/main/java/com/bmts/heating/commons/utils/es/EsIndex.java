package com.bmts.heating.commons.utils.es;

//elasticsearch索引枚举类
public enum EsIndex {
	//实时采集数据
	FIRST_REAL_DATA("point_history_first"),
	SECOND_REAL_DATA("point_history_second"),
	INDOOR_TEMP_REAL_DATA("point_history_house"),
	//小时平均
	FIRST_HOUR_AVG("point_history_first_hour_avg"),
	SECOND_HOUR_AVG("point_history_second_hour_avg"),
	INDOOR_TEMP_HOUR_AVG("point_history_house_hour_avg"),
	//整点数据
	FIRST_HOUR("point_history_first_hour"),
	SECOND_HOUR("point_history_second_hour"),
	INDOOR_TEMP_HOUR("point_history_house_hour"),
	//天平均数据
	FIRST_DAY("point_history_first_day"),
	SECOND_DAY("point_history_second_day"),
	INDOOR_TEMP_DAY("point_history_house_day");



	EsIndex(String index) {
		this.index = index;
	}

	private final String index;

	public String getIndex() {
		return index;
	}

}
