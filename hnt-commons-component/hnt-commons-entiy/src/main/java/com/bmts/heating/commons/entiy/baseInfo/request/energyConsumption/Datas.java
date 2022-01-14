package com.bmts.heating.commons.entiy.baseInfo.request.energyConsumption;

import java.util.List;

@lombok.Data
public class Datas {
	private Long ts;
	private List<PointValue> pointValues;
}
