package com.bmts.heating.web.energy.pojo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;

@Api(tags = "换热站当日能耗")
@Data
public class StationEnergyInfoResponse {

	@ApiModelProperty("今日耗水量")
	private Object todayWater=new BigDecimal(0);

	@ApiModelProperty("今日耗电量")
	private Object todayEle=new BigDecimal(0);;

	@ApiModelProperty("今日耗热耗")
	private Object todayHeat=new BigDecimal(0);;

	@ApiModelProperty("昨日耗水量")
	private Object lastDayWater=new BigDecimal(0);;

	@ApiModelProperty("昨日耗电量")
	private Object lastDayEle=new BigDecimal(0);;

	@ApiModelProperty("昨日耗热量")
	private Object lastDayHeat=new BigDecimal(0);;


	public void todaySetter(HashMap<String,Object> map){
		this.todayEle = map.get("Ep");
		this.todayWater = map.get("WM_FT");
		this.todayHeat = map.get("HM_HT");
	}

	public void lastdaySetter(HashMap<String,Object> map){
		this.lastDayEle = map.get("Ep");
		this.lastDayWater = map.get("WM_FT");
		this.lastDayHeat = map.get("HM_HT");
	}
}
