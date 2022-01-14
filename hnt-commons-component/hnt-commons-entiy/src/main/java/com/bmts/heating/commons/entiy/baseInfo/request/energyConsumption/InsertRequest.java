package com.bmts.heating.commons.entiy.baseInfo.request.energyConsumption;

import lombok.Data;

import java.util.List;

@Data
public class InsertRequest {
	private String tableName;
	private String StableName;
	private List<Datas> datasList;
	private Tag tag;
	private Long startTime;
	private Long endTime;
}
