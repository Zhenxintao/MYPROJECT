package com.bmts.heating.web.energy.pojo;

import com.bmts.heating.commons.utils.es.ParseUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;

@Data
@ApiModel("同环比图表响应类")
public class EnergyChartResponse {

	@ApiModelProperty("时间")
	private Object date;

	@ApiModelProperty("总耗")
	private Double value;

	@ApiModelProperty("单耗")
	private Double unitValue;


	public void setHeatSource(HashMap<String,Object> map){
		this.date = map.get("index").toString().replace("_",":");
		this.value = ParseUtil.parseDouble(map.get("HeatSourceTotalHeat_MtrG"));
		this.unitValue = ParseUtil.parseDouble(map.get("HeatSourceTotalHeat_MtrGunitStandard"));
	}

	public void setWaterSource(HashMap<String,Object> map){
		this.date = map.get("index").toString().replace("_",":");
		this.value = ParseUtil.parseDouble(map.get("HeatSourceFTSupply"));
		this.unitValue = ParseUtil.parseDouble(map.get("HeatSourceFTSupplyunitStandard"));
	}

	public void setEleSource(HashMap<String,Object> map){
		this.date = map.get("index").toString().replace("_",":");
		this.value = ParseUtil.parseDouble(map.get("HeatSourceEp"));
		this.unitValue = ParseUtil.parseDouble(map.get("HeatSourceEpunitStandard"));
	}

	public void setHeatSource(HashMap<String,Object> map,String name){
		this.date = name;
		this.value = ParseUtil.parseDouble(map.get("HeatSourceTotalHeat_MtrG"));
		this.unitValue = ParseUtil.parseDouble(map.get("HeatSourceTotalHeat_MtrGunitStandard"));
	}

	public void setWaterSource(HashMap<String,Object> map,String name){
		this.date = name;
		this.value = ParseUtil.parseDouble(map.get("HeatSourceFTSupply"));
		this.unitValue = ParseUtil.parseDouble(map.get("HeatSourceFTSupplyunitStandard"));
	}

	public void setEleSource(HashMap<String,Object> map,String name){
		this.date = name;
		this.value = ParseUtil.parseDouble(map.get("HeatSourceEp"));
		this.unitValue = ParseUtil.parseDouble(map.get("HeatSourceEpunitStandard"));
	}

	public void setHeatStation(HashMap<String,Object> map){
		this.date = map.get("index").toString().replace("_",":");
		this.value = ParseUtil.parseDouble(map.get("HM_HT"));
		this.unitValue = ParseUtil.parseDouble(map.get("HM_HTunitStandard"));
	}

	public void setWaterStation(HashMap<String,Object> map){
		this.date = map.get("index").toString().replace("_",":");
		this.value = ParseUtil.parseDouble(map.get("WM_FT"));
		this.unitValue = ParseUtil.parseDouble(map.get("WM_FTunitStandard"));
	}

	public void setEleStation(HashMap<String,Object> map){
		this.date = map.get("index").toString().replace("_",":");
		this.value = ParseUtil.parseDouble(map.get("Ep"));
		this.unitValue = ParseUtil.parseDouble(map.get("EpunitStandard"));
	}

	public void setHeatStation(HashMap<String,Object> map,String name){
		this.date = name;
		this.value = ParseUtil.parseDouble(map.get("HM_HT"));
		this.unitValue = ParseUtil.parseDouble(map.get("HM_HTunitStandard"));
	}

	public void setWaterStation(HashMap<String,Object> map,String name){
		this.date = name;
		this.value = ParseUtil.parseDouble(map.get("WM_FT"));
		this.unitValue = ParseUtil.parseDouble(map.get("WM_FTunitStandard"));
	}

	public void setEleStation(HashMap<String,Object> map,String name){
		this.date = name;
		this.value = ParseUtil.parseDouble(map.get("Ep"));
		this.unitValue = ParseUtil.parseDouble(map.get("EpunitStandard"));
	}

}
