package com.bmts.heating.web.energy.pojo;

import com.bmts.heating.commons.basement.model.db.entity.EnergyUnitStandardConfig;
import com.bmts.heating.commons.utils.es.ParseUtil;
import com.bmts.heating.compute.hitory.energy.ComprehensiveEnergy;
import com.bmts.heating.compute.hitory.energy.UnitStandardCompute;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@ApiModel("能耗信息响应类")
@Data
@Component
public class EnergyInfoResponse {

	@ApiModelProperty("组织名称")
	private String name;

	@ApiModelProperty("折算面积")
	private Double area;

	@ApiModelProperty("耗热量")
	private Double heat;

	@ApiModelProperty("热单耗")
	private Double unitHeat;

	@ApiModelProperty("折标热单耗")
	private Double backStepUnitHeat;

	@ApiModelProperty("折标煤")
	private Double backStepCoal;

	@ApiModelProperty("耗水量")
	private Double water;

	@ApiModelProperty("水单耗")
	private Double unitWater;

	@ApiModelProperty("耗电量")
	private Double electricity;

	@ApiModelProperty("电单耗")
	private Double unitElectricity;

	@ApiModelProperty("质量")
	private Object quality;

	@ApiModelProperty("时间戳")
	private Object timeStrap;

	public void setSource(Map<String, Object> map, Object avgTemperature, EnergyUnitStandardConfig unitStandardConfig,Object quality) {
		this.area = ParseUtil.parseDouble(map.get("area"));
		this.heat = ParseUtil.parseDouble(map.get("HeatSourceTotalHeat_MtrG"));
		this.unitHeat = ParseUtil.parseDouble(map.get("HeatSourceTotalHeat_MtrGunitStandard"));
		this.water = ParseUtil.parseDouble(map.get("HeatSourceFTSupply"));
		this.unitWater = ParseUtil.parseDouble(map.get("HeatSourceFTSupplyunitStandard"));
		this.electricity = ParseUtil.parseDouble(map.get("HeatSourceEp"));
		this.unitElectricity = ParseUtil.parseDouble(map.get("HeatSourceEpunitStandard"));
		this.quality = quality;
		this.timeStrap = map.get("timeStrap");
		this.name = map.get("index").toString();
		this.computeUnitHeat(avgTemperature,unitStandardConfig);
	}

	public void setStation(Map<String, Object> map, Object avgTemperature, EnergyUnitStandardConfig unitStandardConfig, Object quality) {
		this.area = ParseUtil.parseDouble(map.get("area"));
		this.heat = ParseUtil.parseDouble(map.get("HM_HT"));
		this.unitHeat = ParseUtil.parseDouble(map.get("HM_HTunitStandard"));
		this.water = ParseUtil.parseDouble(map.get("WM_FT"));
		this.unitWater = ParseUtil.parseDouble(map.get("WM_FTunitStandard"));
		this.electricity = ParseUtil.parseDouble(map.get("Ep"));
		this.unitElectricity = ParseUtil.parseDouble(map.get("EpunitStandard"));
		this.quality = quality;
		this.timeStrap = map.get("timeStrap");
		this.name = map.get("index").toString();
		this.computeUnitHeat(avgTemperature,unitStandardConfig);
	}

	public void setSource(Map<String, Object> map, Object avgTemperature, EnergyUnitStandardConfig unitStandardConfig) {
		this.area = ParseUtil.parseDouble(map.get("area"));
		Map heatSourceTotalHeatMtrG = (Map) map.get("HeatSourceTotalHeat_MtrG");
		if (heatSourceTotalHeatMtrG != null) {
			this.heat = ParseUtil.parseDouble(heatSourceTotalHeatMtrG.get("consumption"));
			this.unitHeat = ParseUtil.parseDouble(heatSourceTotalHeatMtrG.get("unitConsumption"));
		}

		Map supply = (Map) map.get("HeatSourceFTSupply");
		if (supply != null) {
			this.water = ParseUtil.parseDouble(supply.get("consumption"));
			this.unitWater = ParseUtil.parseDouble(supply.get("unitConsumption"));
		}
		Map ep = (Map) map.get("HeatSourceEp");
		if (ep != null) {
			this.electricity = ParseUtil.parseDouble(ep.get("consumption"));
			this.unitElectricity = ParseUtil.parseDouble(ep.get("unitConsumption"));
		}
		this.computeUnitHeat(avgTemperature,unitStandardConfig);

		this.timeStrap = map.get("timeStrap");
	}

	public void setStation(Map<String, Object> map, Object avgTemperature, EnergyUnitStandardConfig unitStandardConfig) {
		this.area = ParseUtil.parseDouble(map.get("area"));
		Map hmHt = (Map) map.get("HM_HT");
		if(hmHt != null){
			this.heat = ParseUtil.parseDouble(hmHt.get("consumption"));
			this.unitHeat = ParseUtil.parseDouble(hmHt.get("unitConsumption"));
		}
		Map wmFt = (Map) map.get("WM_FT");
		if (wmFt != null) {
			this.water = ParseUtil.parseDouble(wmFt.get("consumption"));
			this.unitWater = ParseUtil.parseDouble(wmFt.get("unitConsumption"));
		}
		Map ep = (Map) map.get("Ep");
		if (ep != null) {
			this.electricity = ParseUtil.parseDouble(ep.get("consumption"));
			this.unitElectricity = ParseUtil.parseDouble(ep.get("unitConsumption"));
		}
		this.computeUnitHeat(avgTemperature,unitStandardConfig);

		this.timeStrap = map.get("timeStrap");
	}

	/**
	 * @param avgTemperature 平均温度
	 */
	private void computeUnitHeat(Object avgTemperature,EnergyUnitStandardConfig energyUnitStandardConfig) {
		try {
			BigDecimal var1 = BigDecimal.valueOf(ParseUtil.parseDouble(unitHeat));
			BigDecimal var2 = BigDecimal.valueOf(ParseUtil.parseDouble(heat));
			BigDecimal outdoorRealTemp = BigDecimal.valueOf(ParseUtil.parseDouble(avgTemperature));
			//折算单耗
			this.backStepUnitHeat = ParseUtil.parseDouble(UnitStandardCompute.unitStandard(energyUnitStandardConfig.getOutdoorTemp(), energyUnitStandardConfig.getIndoorTemp(), outdoorRealTemp, var1));
			//折标热量
			BigDecimal heatTotal = UnitStandardCompute.unitStandard(energyUnitStandardConfig.getOutdoorTemp(), energyUnitStandardConfig.getIndoorTemp(), outdoorRealTemp,var2);
			//折标煤
			this.backStepCoal = ParseUtil.parseDouble(ComprehensiveEnergy.comprehensiveEnergy(heatTotal, BigDecimal.ZERO, BigDecimal.ZERO));
		}catch (Exception e){
			log.warn("参数错误,无法计算折标量");
		}
	}
}
