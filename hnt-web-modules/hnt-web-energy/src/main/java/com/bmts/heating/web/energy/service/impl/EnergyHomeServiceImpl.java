package com.bmts.heating.web.energy.service.impl;

import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.response.energy.ReachStandardResponse;
import com.bmts.heating.commons.entiy.baseInfo.request.energy.EnergyHomeChartDto;
import com.bmts.heating.commons.entiy.baseInfo.response.WeatherTempComparison;
import com.bmts.heating.commons.entiy.energy.EnergyType;
import com.bmts.heating.commons.entiy.energy.EvalulateReachStandard;
import com.bmts.heating.commons.entiy.gathersearch.request.*;
import com.bmts.heating.commons.utils.es.LocalTimeUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.energy.pojo.HomeBaseResponse;
import com.bmts.heating.web.energy.pojo.HomeRateDto;
import com.bmts.heating.web.energy.pojo.StationEnergyInfoResponse;
import com.bmts.heating.web.energy.service.CommonService;
import com.bmts.heating.web.energy.service.EnergyHomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hjw
 */
@Service
@Slf4j
public class EnergyHomeServiceImpl implements EnergyHomeService {

	@Autowired
	private TSCCRestTemplate template;
	@Autowired
	private CommonService commonService;

	private final String url = "/energy/";
	private final String gatherSearch = "gather_search";

	//region 基础数据

	@Override
	public Response baseData(Integer type,Integer id){
		switch (type){
			case 1:
				//分公司
				return this.baseDataCompany(id,"base");
			case 2:
				//热网
				return this.baseDataNet(id);
			default: return Response.paramError();
		}
	}

	/**
	 * 分公司
	 * @param id 公司id
	 * @return response
	 */
	private Response baseDataCompany(Integer id,String request){
		try {
			List<StationFirstNetBaseView> firstNetBases = commonService.filterFirstNetBaseByOrgId(id);
			Integer[] ids = new Integer[firstNetBases.size()];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = firstNetBases.get(i).getHeatSystemId();
			}
			HashMap<String, Object>[] energyMap;
			if ("base".equals(request)) {
				energyMap = this.historyData1(ids);
				return Response.success(energyMap != null ? inputWeather(energyMap) : null);
			} else {
				energyMap = this.historyData2(ids);
				return Response.success(energyMap);
			}
		}
		catch (Exception e){
			return Response.fail();
		}
	}

	private List<HomeBaseResponse> inputWeather(HashMap<String,Object>[] energyMap){
		WeatherTempComparison weatherTempComparison = commonService.weatherInfo();
		List<HomeBaseResponse> list = new ArrayList<>();
		HomeBaseResponse homeBaseResponseN = new HomeBaseResponse();
		HomeBaseResponse homeBaseResponseL = new HomeBaseResponse();
		HomeBaseResponse homeBaseResponseY = new HomeBaseResponse();
		list.add(homeBaseResponseN);
		list.add(homeBaseResponseL);
		list.add(homeBaseResponseY);
		BigDecimal heat = null;
		BigDecimal ele = null;
		BigDecimal water = null;
		homeBaseResponseN.setTemp(weatherTempComparison.getCurrent());
		homeBaseResponseN.setIndex("昨日");

		homeBaseResponseL.setTemp(weatherTempComparison.getBeforeDay());
		homeBaseResponseL.setTempIndex(this.compare(weatherTempComparison.getCurrent(),weatherTempComparison.getBeforeDay()));
		homeBaseResponseL.setIndex("同比");

		homeBaseResponseY.setTemp(weatherTempComparison.getBeforeYear());
		homeBaseResponseL.setTempIndex(this.compare(weatherTempComparison.getCurrent(),weatherTempComparison.getBeforeYear()));
		homeBaseResponseY.setIndex("环比");

		for (HashMap<String, Object> stringObjectHashMap : energyMap) {
			if ("昨日".equals(stringObjectHashMap.get("index"))) {
				heat = toBigDecimal(stringObjectHashMap.get("HM_HTunitStandard"));
				homeBaseResponseN.setHeat(heat);
				ele = toBigDecimal(stringObjectHashMap.get("EpunitStandard"));
				homeBaseResponseN.setEle(ele);
				water = toBigDecimal(stringObjectHashMap.get("WM_FTunitStandard"));
				homeBaseResponseN.setWater(water);
			}
			else if ("环比".equals(stringObjectHashMap.get("index"))) {
				if(heat != null){
					BigDecimal hmHTunitStandard = divide(toBigDecimal(stringObjectHashMap.get("HM_HTunitStandard")), heat);
					homeBaseResponseL.setHeat(hmHTunitStandard);
					if(hmHTunitStandard != null) {
						homeBaseResponseL.setHeatIndex(hmHTunitStandard.compareTo(new BigDecimal(0)));
					}
				}
				if(ele != null){
					BigDecimal epunitStandard = divide(toBigDecimal(stringObjectHashMap.get("EpunitStandard")), ele);
					homeBaseResponseL.setEle(epunitStandard);
					if (epunitStandard != null) {
						homeBaseResponseL.setEleIndex(epunitStandard.compareTo(new BigDecimal(0)));
					}
				}
				if(water != null){
					BigDecimal wm_fTunitStandard = divide(toBigDecimal(stringObjectHashMap.get("WM_FTunitStandard")), water);
					homeBaseResponseL.setWater(wm_fTunitStandard);
					if (wm_fTunitStandard != null) {
						homeBaseResponseL.setWaterIndex(wm_fTunitStandard.compareTo(new BigDecimal(0)));
					}
				}
			}
			else {
				if (null != weatherTempComparison.getBeforeYear()) {
					homeBaseResponseY.setTempIndex(weatherTempComparison.getCurrent().compareTo(weatherTempComparison.getBeforeYear()));
				}
				if(heat != null){
					BigDecimal hm_hTunitStandard = divide(toBigDecimal(stringObjectHashMap.get("HM_HTunitStandard")), heat);
					homeBaseResponseY.setHeat(hm_hTunitStandard);
					if (hm_hTunitStandard != null) {
						homeBaseResponseY.setHeatIndex(hm_hTunitStandard.compareTo(new BigDecimal(0)));
					}
				}
				if(ele != null){
					BigDecimal epunitStandard = divide(toBigDecimal(stringObjectHashMap.get("EpunitStandard")), ele);
					homeBaseResponseY.setEle(epunitStandard);
					if (epunitStandard != null) {
						homeBaseResponseY.setEleIndex(epunitStandard.compareTo(new BigDecimal(0)));
					}
				}
				if(water != null){
					BigDecimal wm_fTunitStandard = divide(toBigDecimal(stringObjectHashMap.get("WM_FTunitStandard")), water);
					homeBaseResponseY.setWater(wm_fTunitStandard);
					if (wm_fTunitStandard != null) {
						homeBaseResponseY.setWaterIndex(wm_fTunitStandard.compareTo(new BigDecimal(0)));
					}
				}
			}
		}
		return list;
	}

	private BigDecimal divide(BigDecimal var1,BigDecimal var2){
		if (var1 != null && var2 != null) {
			try {

			return var1.divide(var2,1).subtract(new BigDecimal("1"));
			}catch (Exception e){
				log.info("除数为0");
			}
		}
		return null;
	}

	private int compare(BigDecimal var1,BigDecimal var2){
		if (var1 != null && var2 != null) {
			return var1.compareTo(var2);
		}
		return 0;
	}

	private BigDecimal toBigDecimal(Object obj){
		if (obj != null) {
			try {
				return new BigDecimal(obj.toString()).setScale(2,BigDecimal.ROUND_HALF_DOWN);
			}catch (Exception e){
				return null;
			}
		}
		return null;
	}

	/**
	 * 分热网
	 * @param id 网id
	 * @return response
	 */
	private Response baseDataNet(Integer id){
		try {
			List<StationFirstNetBaseView> firstNetBases = commonService.filterFirstNetBase();
			if (id != null) {
//			firstNetBases = firstNetBases.stream().filter(e -> e.getHeatNetId() == id).collect(Collectors.toList());
			}
			Integer[] ids = new Integer[firstNetBases.size()];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = firstNetBases.get(i).getHeatSystemId();
			}
			HashMap<String, Object>[] energyMap = this.historyData1(ids);

			if (firstNetBases.size() == 0) {
				return Response.success();
			}
			return Response.success(inputWeather(energyMap));
		}catch (Exception e){
			return  Response.fail();
		}
	}

	private HashMap<String,Object>[] historyData1(Integer[] stationSystemIds){

		QueryEsBucketDto queryEsDto = new QueryEsBucketDto();
		List<TimeRange> list = new ArrayList<>();
		for (int i = 0; i < 1; i++) {
			TimeRange timeRange = new TimeRange();
//			timeRange.setStart(LocalTimeUtils.getHour(-1));
//			timeRange.setEnd(LocalTimeUtils.getHour(0));
			timeRange.setStart(LocalTimeUtils.getDay(-1));
			timeRange.setEnd(LocalTimeUtils.getDay(0));
			timeRange.setIndex("昨日");
			list.add(timeRange);

			timeRange = new TimeRange();
			timeRange.setStart(LocalTimeUtils.getDay(-2));
			timeRange.setEnd(LocalTimeUtils.getDay(-1));
			timeRange.setIndex("环比");
			list.add(timeRange);

			timeRange = new TimeRange();
			timeRange.setStart(LocalTimeUtils.getYear(LocalTimeUtils.getDay(-1),-1));
			timeRange.setEnd(LocalTimeUtils.getYear(LocalTimeUtils.getDay(0),-1));
			timeRange.setIndex("同比");
			list.add(timeRange);

		}
		queryEsDto.setTimeRanges(list);
		queryEsDto.setDocument(HistoryDocument.HOUR);
		String[] fields = CommonService.STATION_FIELDS;
		queryEsDto.setIncludeFields(fields);
		queryEsDto.setHeatSystemId(stationSystemIds);
		queryEsDto.setSourceType(HistorySourceType.ENERGY_CONVERGE);
		queryEsDto.setIndex(2);
		return template.post(url + "bucket", queryEsDto, gatherSearch, HashMap[].class);
	}

	private HashMap<String,Object>[] historyData2(Integer[] stationSystemIds){
		QueryEsBucketDto queryEsDto = new QueryEsBucketDto();
		List<TimeRange> list = new ArrayList<>();
		for (int i = 0; i < 1; i++) {
			TimeRange timeRange = new TimeRange();
			timeRange.setStart(LocalTimeUtils.getDay(0));
			timeRange.setEnd(LocalTimeUtils.getHour(0));
			timeRange.setIndex("公司");
			list.add(timeRange);
		}
		queryEsDto.setTimeRanges(list);
		queryEsDto.setDocument(HistoryDocument.HOUR);
		String[] fields = CommonService.STATION_FIELDS;
		queryEsDto.setIncludeFields(fields);
		queryEsDto.setHeatSystemId(stationSystemIds);
		queryEsDto.setSourceType(HistorySourceType.ENERGY_CONVERGE);
		queryEsDto.setIndex(2);
		return template.post(url + "bucket", queryEsDto, gatherSearch, HashMap[].class);
	}

	//endregion

	//region 分公司雷达图
	@Override
	public Response radarCharts(Integer id) {
		return this.baseDataCompany(id,"radar");
	}

	//endregion

	//region 综合能耗
	@Override
	public Response synthesizeEnergy() {
		return null;
	}

	//endregion

	//region 达标率
	@Override
	public Response rate(HomeRateDto dto) {
		LocalDate evaluateTime = null;
		if (dto.getCompare() == null) {
			/*
			 * 取当天数据
			 */
			evaluateTime = LocalDateTime.now().toLocalDate().plusDays(-1);
		} else {
			switch (dto.getCompare()) {
				case 1:
					evaluateTime = LocalDateTime.now().toLocalDate().plusDays(-8);
					break;
				case 2:
					evaluateTime = LocalDateTime.now().toLocalDate().plusMonths(-1).plusDays(-1);
					break;
				case 3:
					evaluateTime = LocalDateTime.now().toLocalDate().plusYears(-1).plusDays(-1);
					break;
			}
		}
		EvalulateReachStandard evalulateReachStandard=new EvalulateReachStandard();
		evalulateReachStandard.setTarget(dto.getTarget());
		evalulateReachStandard.setDate(evaluateTime);
		try {
			evalulateReachStandard.setEnergyType(EnergyType.WM_FT);
			ReachStandardResponse[] resultWater= template.doHttp(url.concat("evaluate/home"), evalulateReachStandard, gatherSearch, ReachStandardResponse[].class, HttpMethod.POST);
			evalulateReachStandard.setEnergyType(EnergyType.Ep);
			ReachStandardResponse[] resultEp= template.doHttp(url.concat("evaluate/home"), evalulateReachStandard, gatherSearch, ReachStandardResponse[].class, HttpMethod.POST);
			evalulateReachStandard.setEnergyType(EnergyType.HM_HT);
			ReachStandardResponse[] resultHeat= template.doHttp(url.concat("evaluate/home"), evalulateReachStandard, gatherSearch, ReachStandardResponse[].class, HttpMethod.POST);
			Map<String,ReachStandardResponse[]> result=new HashMap<>();
			result.put("water",packageResponse(resultWater));
			result.put("ele",packageResponse(resultEp));
			result.put("heat",packageResponse(resultHeat));
			return Response.success(result);

		}catch (Exception e)
		{
			log.error("查询达标数据出错 {}",e);
			return Response.success();
		}
	}

	private ReachStandardResponse[] packageResponse(ReachStandardResponse[] responses){
		ReachStandardResponse[] res = new ReachStandardResponse[3];
		for (int i = 0; i < 3; i++) {
			ReachStandardResponse response = new ReachStandardResponse();
			response.setQualified(i+1);
			response.setCount(0);
			res[i] = response;
		}

		for (ReachStandardResponse response : responses) {
			if (response.getQualified().equals(1)){
				res[0] = response;
			}else if(response.getQualified().equals(2)){
				res[1] = response;
			}else if(response.getQualified().equals(3)){
				res[2] = response;
			}
		}
		return res;
	}
	//endregion

	//region  换热站排行
	@Override
	public Response energyRank(EnergyHomeChartDto dto) {
		try {
			List<StationFirstNetBaseView> firstNetBases = commonService.filterFirstNetBase();
			Integer[] ids = new Integer[firstNetBases.size()];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = firstNetBases.get(i).getHeatSystemId();
			}
			HashMap<String, Object>[] heatEnergyMap;
			HashMap<String, Object>[] waterEnergyMap;
			HashMap<String, Object>[] eleEnergyMap;
			dto.setEnergyType(1);
			waterEnergyMap = this.getEnergyLine(dto, ids);
			dto.setEnergyType(2);
			eleEnergyMap = this.getEnergyLine(dto, ids);
			dto.setEnergyType(3);
			heatEnergyMap = this.getEnergyLine(dto, ids);
			HashMap<String, HashMap<String, Object>[]> map = new HashMap<>(3);
			map.put("water", waterEnergyMap);
			map.put("ele", eleEnergyMap);
			map.put("heat", heatEnergyMap);
			if (firstNetBases.size() == 0) {
				return Response.success();
			}
			return Response.success(this.packageDataLine(map, firstNetBases));
		}catch (Exception e){
			return Response.fail();
		}
	}

	private HashMap<String,Object>[] getEnergyLine(EnergyHomeChartDto dto, Integer[] stationSystemIds){
		QueryEsDto queryEsDto = new QueryEsDto();
		switch (dto.getType()){
			//小时单耗
			case 1:
				queryEsDto.setStart(LocalTimeUtils.getDay(0));
				queryEsDto.setEnd(LocalTimeUtils.getHour(0));
				queryEsDto.setDocument(HistoryDocument.HOUR);break;
			//天
			case 2:
				queryEsDto.setStart(LocalTimeUtils.getDay(-1));
				queryEsDto.setEnd(LocalTimeUtils.getDay(0));
				queryEsDto.setDocument(HistoryDocument.DAY);break;
			default:
				throw new RuntimeException("param energyType is error");
		}
		queryEsDto.setCurrentPage(1);
		queryEsDto.setSize(dto.getTop());
		switch (dto.getEnergyType()){
			case 1:
				queryEsDto.setField("water");break;
			case 2:
				queryEsDto.setField("electricity");break;
			case 3:
				queryEsDto.setField("heat");break;
			default:
				throw new RuntimeException("param energyType is error");
		}
		queryEsDto.setSortType(dto.getIsAsc());
		queryEsDto.setHeatSystemId(stationSystemIds);
		queryEsDto.setIncludeTotal(false);
		queryEsDto.setSourceType(HistorySourceType.ENERGY_CONVERGE);
		return template.post(url + "table", queryEsDto, gatherSearch, HashMap[].class);
	}
	//打包返回结果集
	private Map<String, Object> packageDataLine(HashMap<String,HashMap<String,Object>[]> maps, List<StationFirstNetBaseView> firstNetBases){
		final Map<Integer, List<StationFirstNetBaseView>> firstNetBaseMap = firstNetBases.stream().collect(Collectors.groupingBy(StationFirstNetBaseView::getHeatSystemId));
		Map<String,Object> res = new HashMap<>();

		List<Map<String,Object>> waterLis;
		List<Map<String,Object>> eleLis;
		List<Map<String,Object>> heatLis;
		waterLis = packageData(maps.get("water"),firstNetBaseMap,1);
		eleLis = packageData(maps.get("ele"),firstNetBaseMap,2);
		heatLis = packageData(maps.get("heat"),firstNetBaseMap,3);
		res.put("water",waterLis);
		res.put("ele",eleLis);
		res.put("heat",heatLis);
		return res;
	}

	private List<Map<String,Object>> packageData(HashMap<String, Object>[] map,Map<Integer, List<StationFirstNetBaseView>> firstNetBaseMap,int energType){
		List<Map<String,Object>> lis = new ArrayList<>();
		for (HashMap<String, Object> dataNum : map) {
			Integer relevanceId = Integer.parseInt(dataNum.get("relevanceId").toString());
			Map<String, Object> response = new HashMap<>();
			StationFirstNetBaseView firstNetBase = firstNetBaseMap.get(relevanceId).get(0);
			response.put("column", firstNetBase.getHeatTransferStationName());
			try {
				if (energType == 1) {
					response.put("value", ((HashMap) dataNum.get("WM_FT")).get("consumption"));
				} else {
//					response.put("value", ((HashMap) dataNum.get("WM_FT")).get("unitConsumption"));
				}
			} catch (Exception e) {
				log.info("当前系统{}不存在WM_FT", dataNum.get("relevanceId"));
			}
			try {
				if (energType == 2) {
					response.put("value", ((HashMap) dataNum.get("Ep")).get("consumption"));
				} else {
//					response.put("value", ((HashMap) dataNum.get("Ep")).get("unitConsumption"));
				}
			} catch (Exception e) {
				log.info("当前系统{}不存在Ep", dataNum.get("relevanceId"));
			}
			try {
				if (energType == 3) {
					response.put("value", ((HashMap) dataNum.get("HM_HT")).get("consumption"));
				} else {
//					response.put("value", ((HashMap) dataNum.get("HM_HT")).get("unitConsumption"));
				}
			} catch (Exception e) {
				log.info("当前系统{}不存在HM_HT", dataNum.get("relevanceId"));
			}
			lis.add(response);
		}
		return lis;
	}

	//endregion


	//region 换热站能耗

	@Override
	public Response stationEnergy(int id) {
		try {
			List<StationFirstNetBaseView> firstNetBases = commonService.filterFirstNetBase();
			Integer[] ids = new Integer[0];
			if (id == -1){
				ids = new Integer[firstNetBases.size()];
				for (int i = 0;i<firstNetBases.size();i++) {
					ids[i] = firstNetBases.get(i).getHeatSystemId();
				}
			}else{
				for (StationFirstNetBaseView firstNetBase : firstNetBases) {
					if (id == firstNetBase.getHeatTransferStationId()) {
						ids = new Integer[]{firstNetBase.getHeatSystemId()};
						break;
					}
				}
			}
			if (ids.length == 0) {
				return Response.paramError();
			}
			StationEnergyInfoResponse response = new StationEnergyInfoResponse();
			HashMap<String, Object>[] stationEnergy = this.getStationEnergy(ids);
			for (HashMap<String, Object> map : stationEnergy) {
				if ("今日".equals(map.get("index"))) {
					response.todaySetter(map);
				}else{
					response.lastdaySetter(map);
				}
			}
			return Response.success(response);
		}catch (Exception e){
			return  Response.fail();
		}
	}

	private HashMap<String,Object>[] getStationEnergy(Integer[] stationSystemIds){
		QueryEsBucketDto queryEsDto = new QueryEsBucketDto();
		List<TimeRange> list = new ArrayList<>();
		for (int i = 0; i < 1; i++) {
			TimeRange timeRange = new TimeRange();
			timeRange.setStart(LocalTimeUtils.getDay(0));
			timeRange.setEnd(LocalTimeUtils.getHour(0));
			timeRange.setIndex("今日");
			list.add(timeRange);

			timeRange = new TimeRange();
			timeRange.setStart(LocalTimeUtils.getDay(-1));
			timeRange.setEnd(LocalTimeUtils.getDay(0));
			timeRange.setIndex("昨日");
			list.add(timeRange);
		}
		queryEsDto.setTimeRanges(list);
		queryEsDto.setDocument(HistoryDocument.HOUR);
		String[] fields = CommonService.STATION_FIELDS;
		queryEsDto.setIncludeFields(fields);
		queryEsDto.setHeatSystemId(stationSystemIds);
		queryEsDto.setSourceType(HistorySourceType.ENERGY_CONVERGE);
		queryEsDto.setIndex(2);
		return template.post(url + "bucket", queryEsDto, gatherSearch, HashMap[].class);
	}

	//endregion
}
