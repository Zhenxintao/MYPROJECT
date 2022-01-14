package com.bmts.heating.service.task.history.task.energy;

import com.bmts.heating.commons.basement.model.db.entity.SourceFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("系统基础信息")
public class HeatSystemBase {

	@ApiModelProperty("关联Id")
	private int relevanceId;
	@ApiModelProperty("面积")
	private BigDecimal area;


	public void setHeatSystemBase(StationFirstNetBaseView stationView){
		this.area = "0".equals(stationView.getNumber()) ?
			stationView.getHeatStationArea() : stationView.getHeatSystemArea();
			this.relevanceId = stationView.getHeatSystemId();
	}

	public void setHeatSystemBase(SourceFirstNetBaseView sourceView){
		this.relevanceId = sourceView.getHeatSystemId();
		this.area = sourceView.getHeatSourceArea();
	}
}
