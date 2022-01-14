package com.bmts.heating.web.energy.service.impl;

import com.bmts.heating.commons.basement.model.db.entity.EnergyUnitStandardConfig;
import com.bmts.heating.commons.basement.model.db.entity.SourceFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.entiy.gathersearch.request.HistoryDocument;
import com.bmts.heating.commons.entiy.gathersearch.request.HistorySourceType;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsBucketDto;
import com.bmts.heating.commons.entiy.gathersearch.request.TimeRange;
import com.bmts.heating.commons.utils.es.LocalTimeUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.energy.pojo.EnergyChartResponse;
import com.bmts.heating.web.energy.pojo.EnergyCompareDto;
import com.bmts.heating.web.energy.pojo.EnergyInfoResponse;
import com.bmts.heating.web.energy.service.CommonService;
import com.bmts.heating.web.energy.service.EnergyCompareService;
import com.bmts.heating.web.energy.service.config.EnergyUnitStandardConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author hjw
 */
@Service
@Slf4j
public class EnergyCompareServiceImpl implements EnergyCompareService {

	@Autowired
	private TSCCRestTemplate template;
	@Autowired
	private CommonService commonService;

	private final String url = "/energy/";
	private final String gatherSearch = "gather_search";


	//region 同比、环比表格

	@Override
	public Response page(EnergyCompareDto dto) {
		switch (dto.getType()) {
			case 1:
				/*热源*/
				return this.pageSource(commonService.baseSource(dto.getRelevanceId()),dto);
			case 2:
				/*热网*/
				return this.pageStation(commonService.baseHeatNet(dto.getRelevanceId()), dto);
			case 3:
				/*热力站*/
				return this.pageStation(commonService.baseStation(dto.getRelevanceId()), dto);
			default:
				throw new RuntimeException(" {type} not available");
		}
	}


	@Autowired
	private EnergyUnitStandardConfigService energyUnitStandardConfigService;
	/**
	 * 具体表格业务逻辑
	 * @param firstNetBases 基础信息
	 * @param dto           前端请求参数体
	 * @return response
	 */
	private Response pageSource(List<SourceFirstNetBaseView> firstNetBases, EnergyCompareDto dto) {
		TimeRange timeRange = new TimeRange();
		timeRange.setStart(dto.getStartTime());
		timeRange.setEnd(dto.getEndTime());
		Object avgTemperature = commonService.avgTemperature(timeRange).get("avgTemperature");
		EnergyUnitStandardConfig unitStandardConfig = energyUnitStandardConfigService.info();
		Integer[] ids = new Integer[firstNetBases.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = firstNetBases.get(i).getHeatSystemId();
		}
		HashMap<String, Object>[] energyData = this.getEnergyData(dto, ids);
		List<EnergyInfoResponse> list = new ArrayList<>();
		EnergyInfoResponse response;
		if (energyData == null) {return Response.success();}
		for (HashMap<String, Object> energyDatum : energyData) {
			response = new EnergyInfoResponse();
			if (dto.getType() == 1) {
				response.setSource(energyDatum,avgTemperature,unitStandardConfig,"");
			} else {
				response.setStation(energyDatum,avgTemperature,unitStandardConfig,"");
			}
			list.add(response);
		}
		return Response.success(list);
	}
	/**
	 * 具体表格业务逻辑
	 * @param firstNetBases 基础信息
	 * @param dto           前端请求参数体
	 * @return response
	 */
	private Response pageStation(List<StationFirstNetBaseView> firstNetBases, EnergyCompareDto dto) {
		TimeRange timeRange = new TimeRange();
		timeRange.setStart(dto.getStartTime());
		timeRange.setEnd(dto.getEndTime());
		Object avgTemperature = commonService.avgTemperature(timeRange).get("avgTemperature");
		EnergyUnitStandardConfig unitStandardConfig = energyUnitStandardConfigService.info();
		Integer[] ids = new Integer[firstNetBases.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = firstNetBases.get(i).getHeatSystemId();
		}
		HashMap<String, Object>[] energyData = this.getEnergyData(dto, ids);
		List<EnergyInfoResponse> list = new ArrayList<>();
		EnergyInfoResponse response;
		if (energyData == null) {return Response.success();}
		for (HashMap<String, Object> energyDatum : energyData) {
			response = new EnergyInfoResponse();
			if (dto.getType() == 1) {
				response.setSource(energyDatum,avgTemperature,unitStandardConfig,"");
			} else {
				response.setStation(energyDatum,avgTemperature,unitStandardConfig,"");
			}
			list.add(response);
		}
		return Response.success(list);
	}

	/**
	 * 历史数据
	 * @param dto 前端参数体
	 * @param stationSystemIds 系统id集
	 * @return hashMap
	 */
	private HashMap<String, Object>[] getEnergyData(EnergyCompareDto dto, Integer[] stationSystemIds /*是否根据relevanceId分组聚合*/) {
		QueryEsBucketDto queryEsDto = new QueryEsBucketDto();
		queryEsDto.setTimeRanges(this.compareRange(dto));
		queryEsDto.setDocument(HistoryDocument.HOUR);
		String[] fields;
		if (dto.getType() == 1) {
			fields = CommonService.SOURCE_FIELDS;
		} else {
			fields = CommonService.STATION_FIELDS;
		}
		queryEsDto.setIncludeFields(fields);
		queryEsDto.setHeatSystemId(stationSystemIds);
		queryEsDto.setSourceType(HistorySourceType.ENERGY_CONVERGE);
		queryEsDto.setIndex(2);
		return template.post(url + "bucket", queryEsDto, gatherSearch, HashMap[].class);
	}

	/**
	 * 定义历史查询多个时间区间
	 * @param dto 同比、环比表格入参
	 * @return TimeRange 时间区间
	 */
	private List<TimeRange> compareRange(EnergyCompareDto dto) {
		List<TimeRange> timeRanges = new ArrayList<>();
		TimeRange timeRange = new TimeRange();
		timeRange.setStart(dto.getStartTime());
		timeRange.setEnd(dto.getEndTime());
		timeRange.setIndex(LocalTimeUtils.getLocalDate(dto.getStartTime()));
		timeRanges.add(timeRange);
		switch (dto.getCompare()) {
			case 1 :
				/*同比*/
				for (int i = 1; i <= dto.getCycle(); i++) {
					String localDate = LocalTimeUtils.getLocalDate(LocalTimeUtils.getYear(dto.getStartTime(), -i));
					timeRange = new TimeRange();
					timeRange.setStart(LocalTimeUtils.getYear(dto.getStartTime(), -i));
					timeRange.setEnd(LocalTimeUtils.getYear(dto.getEndTime(), -i));
					timeRange.setIndex(localDate);
					timeRanges.add(timeRange);
				}
				break;
			case 2 :
				/*环比*/
				for (int i = 1; i <= dto.getCycle(); i++) {
					String localDate = LocalTimeUtils.getLocalDate(LocalTimeUtils.getDay(dto.getStartTime(), -i));
					timeRange = new TimeRange();
					timeRange.setStart(LocalTimeUtils.getDay(dto.getStartTime(), -i));
					timeRange.setEnd(LocalTimeUtils.getDay(dto.getEndTime(), -i));
					timeRange.setIndex(localDate);
					timeRanges.add(timeRange);
				}
				break;
			default:
				throw new RuntimeException("type is not available");
		}
		return timeRanges;
	}

	//endregion


	//region 图表

	@Override
	public Response charts(EnergyCompareDto dto) {
		switch (dto.getType()) {
			case 1:
				/*热源*/
				return this.sourceCharts(commonService.baseSource(dto.getRelevanceId()),dto);
			case 2:
				/*热网*/
				return this.stationCharts(commonService.baseHeatNet(dto.getRelevanceId()), dto);
			case 3:
				/*热力站*/
				return this.stationCharts(commonService.baseStation(dto.getRelevanceId()), dto);
			default:
				throw new RuntimeException(" {type} not available");
		}
	}

	private Response sourceCharts(List<SourceFirstNetBaseView> firstNetBases, EnergyCompareDto dto) {
		Integer[] ids = new Integer[firstNetBases.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = firstNetBases.get(i).getHeatSystemId();
		}
		HashMap<String, Object>[] energyData = this.getEnergyDataCharts(dto, ids);
		List<EnergyChartResponse> list = new ArrayList<>();
		if (energyData == null) {
			return Response.success();
		}
		for (HashMap<String, Object> energyDatum : energyData) {
			EnergyChartResponse response = new EnergyChartResponse();
			if (dto.getType() == 1) {
				switch (dto.getEnergyType()) {
					case 1:
						/*水*/
						response.setWaterSource(energyDatum);
						break;
					case 2:
						/*电*/
						response.setEleSource(energyDatum);
						break;
					case 3:
						/*热*/
						response.setHeatSource(energyDatum);
						break;
					default:
						throw new RuntimeException("type is not available");
				}
			} else {
				switch (dto.getEnergyType()) {
					case 1 :
						/*水*/
						response.setWaterStation(energyDatum);
						break;
					case 2 :
						/*电*/
						response.setEleStation(energyDatum);
						break;
					case 3 :
						/*热*/
						response.setHeatStation(energyDatum);
						break;
					default:
						throw new RuntimeException("type is not available");
				}
			}
			list.add(response);
		}
		return Response.success(list);
	}

	private Response stationCharts(List<StationFirstNetBaseView> firstNetBases, EnergyCompareDto dto) {
		Integer[] ids = new Integer[firstNetBases.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = firstNetBases.get(i).getHeatSystemId();
		}
		HashMap<String, Object>[] energyData = this.getEnergyDataCharts(dto, ids);
		List<EnergyChartResponse> list = new ArrayList<>();
		if (energyData == null) {
			return Response.success();
		}
		for (HashMap<String, Object> energyDatum : energyData) {
			EnergyChartResponse response = new EnergyChartResponse();
			if (dto.getType() == 1) {
				switch (dto.getEnergyType()) {
					case 1:
						/*水*/
						response.setWaterSource(energyDatum);
						break;
					case 2:
						/*电*/
						response.setEleSource(energyDatum);
						break;
					case 3:
						/*热*/
						response.setHeatSource(energyDatum);
						break;
					default:
						throw new RuntimeException("type is not available");
				}
			} else {
				switch (dto.getEnergyType()) {
					case 1 :
						/*水*/
						response.setWaterStation(energyDatum);
						break;
					case 2 :
						/*电*/
						response.setEleStation(energyDatum);
						break;
					case 3 :
						/*热*/
						response.setHeatStation(energyDatum);
						break;
					default:
						throw new RuntimeException("type is not available");
				}
			}
			list.add(response);
		}
		return Response.success(list);
	}

	private HashMap<String, Object>[] getEnergyDataCharts(EnergyCompareDto dto, Integer[] stationSystemIds /*是否根据relevanceId分组聚合*/) {
		QueryEsBucketDto queryEsDto = new QueryEsBucketDto();
		queryEsDto.setTimeRanges(this.compareRange(dto));
		queryEsDto.setDocument(HistoryDocument.HOUR);
		String[] fields;
		if (dto.getType() == 1) {
			fields = commonService.routeEnergySource(dto.getEnergyType());
		} else {
			fields = commonService.routeEnergyStation(dto.getEnergyType());
		}
		queryEsDto.setIncludeFields(fields);
		queryEsDto.setHeatSystemId(stationSystemIds);
		queryEsDto.setSourceType(HistorySourceType.ENERGY_CONVERGE);
		queryEsDto.setIndex(2);
		return template.post(url + "bucket", queryEsDto, gatherSearch, HashMap[].class);
	}
	//endregion

}
