package com.bmts.heating.web.energy.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bmts.heating.commons.basement.model.db.entity.EnergyUnitStandardConfig;
import com.bmts.heating.commons.basement.model.db.entity.NetSource;
import com.bmts.heating.commons.basement.model.db.entity.SourceFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.entiy.baseInfo.cache.FirstNetBase;
import com.bmts.heating.commons.entiy.gathersearch.request.HistoryDocument;
import com.bmts.heating.commons.entiy.gathersearch.request.HistorySourceType;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsBucketDto;
import com.bmts.heating.commons.entiy.gathersearch.request.TimeRange;
import com.bmts.heating.commons.utils.es.LocalTimeUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.energy.pojo.EnergyChartResponse;
import com.bmts.heating.web.energy.pojo.EnergyInfoDto;
import com.bmts.heating.web.energy.pojo.EnergyInfoResponse;
import com.bmts.heating.web.energy.service.CommonService;
import com.bmts.heating.web.energy.service.EnergyInfoService;
import com.bmts.heating.web.energy.service.config.EnergyUnitStandardConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EnergyInfoServiceImpl implements EnergyInfoService {

	@Autowired
	private TSCCRestTemplate template;
	@Autowired
	private CommonService commonService;
	@Autowired
	private EnergyUnitStandardConfigService energyUnitStandardConfigService;

	private final String url = "/energy/";
	private final String gatherSearch = "gather_search";


	//region 表格

	/**
	 * 表格
	 *
	 * @param dto EnergyInfoDto
	 * @return HashMap<String, Object>
	 */
	@Override
	public Response page(EnergyInfoDto dto) {
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

	private Response pageStation(List<StationFirstNetBaseView> firstNetBases, EnergyInfoDto dto) {
//		int size = firstNetBases.size();
		switch (dto.getType()) {
			case 3:
//				firstNetBases = firstNetBases.stream().skip(start).limit(end).collect(Collectors.toList());
				return this.stationPage(firstNetBases, dto);
			/*热网*/
			case 2:
				List<EnergyInfoResponse> energyInfoResponses = new ArrayList<>();
				List<NetSource> netSources = commonService.netJoinSource(-1);
				//网-源
				Map<Integer, List<NetSource>> netMap = netSources.stream().collect(Collectors.groupingBy(NetSource::getNetId));
				//源-站
				Map<Integer, List<StationFirstNetBaseView>> map = firstNetBases.stream().collect(Collectors.groupingBy(StationFirstNetBaseView::getHeatSourceId));
				Set<Integer> netIds = netMap.keySet();
				for (Integer netId : netIds) {
					List<NetSource> netSources1 = netMap.get(netId);
					List<StationFirstNetBaseView> firstNetBaseViews = new ArrayList<>(16);
					for (NetSource source : netSources1) {
						List<StationFirstNetBaseView> views = map.get(source.getSourceId());
						if (CollectionUtils.isNotEmpty(views)){
							firstNetBaseViews.addAll(views);
						}
					}
					Map<String, Object>[] hashMaps = this.netPage(firstNetBaseViews, dto);
					if (hashMaps != null){
						energyInfoResponses.add(this.convergeNetSinge(hashMaps, netMap.get(netId).get(0), dto));
					}
				}
				HashMap<String, Object> res = new HashMap<>(16);
				res.put("data", CommonService.sorted(energyInfoResponses,dto));
				res.put("total",energyInfoResponses.size());
				return Response.success(res);
			default:
				return Response.paramError();
		}
	}

	private Response pageSource(List<SourceFirstNetBaseView> firstNetBases, EnergyInfoDto dto) {
		return this.sourcePage(firstNetBases, dto);
	}

	/**
	 * 单独热网汇聚
	 */
	private EnergyInfoResponse convergeNetSinge(Map<String, Object>[] energyData, NetSource netSource, EnergyInfoDto dto) {
		EnergyInfoResponse response = new EnergyInfoResponse();
		if (energyData != null) {
			Map<String, Object> energyDatum = energyData[0];
			TimeRange timeRange = new TimeRange();
			timeRange.setStart(dto.getStartTime());
			timeRange.setEnd(dto.getEndTime());
			Object avgTemperature = commonService.avgTemperature(timeRange).get("avgTemperature");
			if (avgTemperature==null){
				avgTemperature=0;
			}
			EnergyUnitStandardConfig unitStandardConfig = energyUnitStandardConfigService.info();
			response.setStation(energyDatum, avgTemperature, unitStandardConfig, "优");
			response.setName(netSource.getNetName());
		}
		return response;
	}

	//分热网历史数据加载
	private HashMap<String, Object>[] netPage(List<StationFirstNetBaseView> firstNetBases, EnergyInfoDto dto) {
		Integer[] ids = new Integer[firstNetBases.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = firstNetBases.get(i).getHeatSystemId();
		}
		return this.getEnergyData(dto, ids, 2);
	}

	private Response sourcePage(List<SourceFirstNetBaseView> firstNetBases, EnergyInfoDto dto) {
		Integer[] ids = new Integer[firstNetBases.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = firstNetBases.get(i).getHeatSystemId();
		}
		Map<String, Object>[] energyData = this.getEnergyData(dto, ids, 1);
		if (firstNetBases.size() == 0 || energyData == null) {
			return Response.success("数据为空");
		}
		TimeRange timeRange = new TimeRange();
		timeRange.setStart(dto.getStartTime());
		timeRange.setEnd(dto.getEndTime());
		Object avgTemperature = commonService.avgTemperature(timeRange).get("avgTemperature");
		if (avgTemperature==null){
			avgTemperature=0;
		}
		EnergyUnitStandardConfig unitStandardConfig = energyUnitStandardConfigService.info();
		return Response.success(this.packageSourceData(energyData, firstNetBases, dto, avgTemperature, unitStandardConfig));
	}

	private Response stationPage(List<StationFirstNetBaseView> firstNetBases, EnergyInfoDto dto) {
		Integer[] ids = new Integer[firstNetBases.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = firstNetBases.get(i).getHeatSystemId();
		}
		Map<String, Object>[] energyData = this.getEnergyData(dto, ids, 1);
		if (firstNetBases.size() == 0 || energyData == null) {
			return Response.success("数据为空");
		}
		TimeRange timeRange = new TimeRange();
		timeRange.setStart(dto.getStartTime());
		timeRange.setEnd(dto.getEndTime());
		Object avgTemperature = commonService.avgTemperature(timeRange).get("avgTemperature");
		if (avgTemperature==null){
			avgTemperature=0;
		}
		EnergyUnitStandardConfig unitStandardConfig = energyUnitStandardConfigService.info();
		return Response.success(this.packageStationData(energyData, firstNetBases, dto, avgTemperature, unitStandardConfig));
	}

	//
	private List<TimeRange> tableTimeRange(EnergyInfoDto dto) {
		List<TimeRange> timeRanges = new ArrayList<>();
		TimeRange timeRange = new TimeRange();
		timeRange.setStart(dto.getStartTime());
		timeRange.setEnd(dto.getEndTime());
		timeRange.setIndex("表格");
		timeRanges.add(timeRange);
		return timeRanges;
	}

	//获取能耗历史数据
	private HashMap<String, Object>[] getEnergyData(EnergyInfoDto dto, Integer[] stationSystemIds, int convergeType) {
		QueryEsBucketDto queryEsDto = new QueryEsBucketDto();
		queryEsDto.setTimeRanges(tableTimeRange(dto));
		if (dto.getDateType() == 1) {
			queryEsDto.setDocument(HistoryDocument.HOUR);
		} else {
			queryEsDto.setDocument(HistoryDocument.DAY);
		}
		String[] fields;
		if (dto.getType() == 1) {
			fields = CommonService.SOURCE_FIELDS;
		} else {
			fields = CommonService.STATION_FIELDS;
		}
		queryEsDto.setIncludeFields(fields);
		queryEsDto.setHeatSystemId(stationSystemIds);
		queryEsDto.setSourceType(HistorySourceType.ENERGY_CONVERGE);
		queryEsDto.setIndex(convergeType);
//		queryEsDto.setSize(dto.getPageCount());
//		queryEsDto.setCurrentPage(dto.getCurrentPage());
		return template.post(url + "bucket", queryEsDto, gatherSearch, HashMap[].class);
	}

	//打包返回结果集
	private HashMap<String, Object> packageStationData(Map<String, Object>[] energyData, List<StationFirstNetBaseView> firstNetBases,
												EnergyInfoDto dto, Object avgTemperature, EnergyUnitStandardConfig unitStandardConfig) {
		final Map<Integer, List<StationFirstNetBaseView>> firstNetBaseMap = firstNetBases.stream().collect(Collectors.groupingBy(StationFirstNetBaseView::getHeatSystemId));
		List<EnergyInfoResponse> list = new ArrayList<>();
		for (Map<String, Object> energyDatum : energyData) {
			Integer relevanceId = Integer.parseInt(energyDatum.get("relevanceId").toString());
			EnergyInfoResponse homeResponse = new EnergyInfoResponse();
			StationFirstNetBaseView firstNetBase = firstNetBaseMap.get(relevanceId).get(0);
			homeResponse.setStation(energyDatum, avgTemperature, unitStandardConfig, "差");
			homeResponse.setName(firstNetBase.getHeatTransferStationName());
			list.add(homeResponse);
		}
//		int size = firstNetBases.size();
		int size = energyData.length;
		HashMap<String, Object> res = new HashMap<>();
		res.put("data", CommonService.sorted(list,dto));
		res.put("total", size);
		return res;
	}

	/**打包返回结果集
	 *
	 * @param energyData 历史数据
	 * @param firstNetBases 基础信息
	 * @param dto 查询条件
	 * @param avgTemperature  气温数据
	 * @param unitStandardConfig 折标配置
	 * @return map
	 */
	private HashMap<String, Object> packageSourceData(Map<String, Object>[] energyData,
													  List<SourceFirstNetBaseView> firstNetBases,
												EnergyInfoDto dto, Object avgTemperature,
													  EnergyUnitStandardConfig unitStandardConfig) {
		final Map<Integer, List<SourceFirstNetBaseView>> firstNetBaseMap = firstNetBases.stream().collect(Collectors.groupingBy(SourceFirstNetBaseView::getHeatSystemId));
		List<EnergyInfoResponse> list = new ArrayList<>();
		for (Map<String, Object> energyDatum : energyData) {
			Integer relevanceId = Integer.parseInt(energyDatum.get("relevanceId").toString());
			EnergyInfoResponse homeResponse = new EnergyInfoResponse();
			SourceFirstNetBaseView firstNetBase = firstNetBaseMap.get(relevanceId).get(0);
			homeResponse.setSource(energyDatum, avgTemperature, unitStandardConfig, "差");
			homeResponse.setName(firstNetBase.getHeatSourceName());
			list.add(homeResponse);
		}
		int size = firstNetBases.size();
		HashMap<String, Object> res = new HashMap<>();
		res.put("data", CommonService.sorted(list,dto));
		res.put("total", size);
		return res;
	}
	//endregion

	//region 评价雷达图
	@Override
	public Response evaluateRadar(EnergyInfoDto dto) {
		return null;
	}
	//endregion

	//region 能耗明细chart

	@Override
	public Response energyInfo(EnergyInfoDto dto) {
		switch (dto.getType()) {
			case 1:
				/*热源*/
				return this.energyInfoSource(commonService.baseSource(dto.getRelevanceId()),dto);
			case 2:
				/*热网*/
				return this.energyInfo(commonService.baseHeatNet(dto.getRelevanceId()), dto);
			case 3:
				/*热力站*/
				return this.energyInfo(commonService.baseStation(dto.getRelevanceId()), dto);
			default:
				throw new RuntimeException(" {type} not available");
		}
	}

	public Response energyInfoSource(List<SourceFirstNetBaseView> firstNetBases, EnergyInfoDto dto) {
		HashMap<String, Object>[] energyData;
		Integer[] ids;
		if (dto.getRelevanceId() == null) {
			ids = new Integer[firstNetBases.size()];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = firstNetBases.get(i).getHeatSystemId();
			}
		} else {
			ids = new Integer[]{dto.getRelevanceId()};
		}
		energyData = this.getEnergyDataInfo(dto, ids);
		List<EnergyChartResponse> list = new ArrayList<>();
		EnergyChartResponse response;
		if (energyData == null) {
			return Response.success();
		}
		for (HashMap<String, Object> energyDatum : energyData) {
			response = new EnergyChartResponse();
			if (dto.getType() == 1) {
				switch (dto.getEnergyType()) {
					case 1:/*水*/
						response.setWaterSource(energyDatum);
						break;
					case 2:/*电*/
						response.setEleSource(energyDatum);
						break;
					case 3:/*热*/
						response.setHeatSource(energyDatum);
						break;
					default:
						throw new RuntimeException("type is not available");
				}
			} else {
				switch (dto.getEnergyType()) {
					case 1:
						/*水*/
						response.setWaterStation(energyDatum);
						break;
					case 2:
						/*电*/
						response.setEleStation(energyDatum);
						break;
					case 3:
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

	public Response energyInfo(List<StationFirstNetBaseView> firstNetBases, EnergyInfoDto dto) {
		HashMap<String, Object>[] energyData;
		Integer[] ids;
//		if (dto.getRelevanceId() == null) {
			ids = new Integer[firstNetBases.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = firstNetBases.get(i).getHeatSystemId();
		}
		energyData = this.getEnergyDataInfo(dto, ids);
		List<EnergyChartResponse> list = new ArrayList<>();
		EnergyChartResponse response;
		if (energyData == null) {
			return Response.success();
		}
		for (HashMap<String, Object> energyDatum : energyData) {
			response = new EnergyChartResponse();
			if (dto.getType() == 1) {
				switch (dto.getEnergyType()) {
					case 1:/*水*/
						response.setWaterSource(energyDatum);
						break;
					case 2:/*电*/
						response.setEleSource(energyDatum);
						break;
					case 3:/*热*/
						response.setHeatSource(energyDatum);
						break;
					default:
						throw new RuntimeException("type is not available");
				}
			} else {
				switch (dto.getEnergyType()) {
					case 1:
						/*水*/
						response.setWaterStation(energyDatum);
						break;
					case 2:
						/*电*/
						response.setEleStation(energyDatum);
						break;
					case 3:
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

//	//分热网历史数据加载
//	private HashMap<String, Object>[]  netInfo(List<FirstNetBase> firstNetBases, EnergyInfoDto dto){
//		Integer[] ids = new Integer[firstNetBases.size()];
//		for (int i = 0; i < ids.length; i++) {
//			ids[i] = firstNetBases.get(i).getHeatSystemId();
//		}
//		return this.getEnergyDataInfo(dto, ids);
//	}

	/**
	 * 定义多个历史时间区间
	 *
	 * @param dto 区分小时、天数据
	 * @return TimeRanges 多个时间区间
	 */
	private List<TimeRange> infoTimeRange(EnergyInfoDto dto) {
		List<TimeRange> timeRanges = new ArrayList<>();
		TimeRange timeRange;
		switch (dto.getDateType()) {
			case 1:
				//小时
				for (int i = 7; i >= 0; i--) {
					String localDate = LocalTimeUtils.getLocalDateTime(LocalTimeUtils.getHour(-i - 1), "HH_00");
					timeRange = new TimeRange();
					timeRange.setStart(LocalTimeUtils.getHour(-i - 1));
					timeRange.setEnd(LocalTimeUtils.getHour(-i));
					timeRange.setIndex(localDate);
					timeRanges.add(timeRange);
				}
				break;
			case 2:
				//天
				for (int i = 0; i < 7; i++) {
					String localDate = LocalTimeUtils.getLocalDate(LocalTimeUtils.getDay(-i - 1));
					timeRange = new TimeRange();
					timeRange.setStart(LocalTimeUtils.getDay(-i - 1));
					timeRange.setEnd(LocalTimeUtils.getDay(-i));
					timeRange.setIndex(localDate);
					timeRanges.add(timeRange);
				}
				break;
			default:
				throw new RuntimeException("param dateType is available");
		}
		return timeRanges;
	}

	//获取能耗历史数据
	private HashMap<String, Object>[] getEnergyDataInfo(EnergyInfoDto dto, Integer[] stationSystemIds /*是否根据relevanceId分组聚合*/) {
		QueryEsBucketDto queryEsDto = new QueryEsBucketDto();
		queryEsDto.setTimeRanges(this.infoTimeRange(dto));
		if (dto.getDateType() == 1) {
			queryEsDto.setDocument(HistoryDocument.HOUR);
		} else {
			queryEsDto.setDocument(HistoryDocument.DAY);
		}
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

	//region 能耗对比
	@Override
	public Response energyCompared(EnergyInfoDto dto) {
		switch (dto.getType()) {
			case 1:
				/*热源*/
				return this.energyComparedSource(commonService.baseSource(dto.getRelevanceId()),dto);
			case 2:
				/*热网*/
				return this.energyComparedStation(commonService.baseHeatNet(dto.getRelevanceId()), dto);
			case 3:
				/*热力站*/
				return this.energyComparedStation(commonService.baseStation(dto.getRelevanceId()), dto);
			default:
				throw new RuntimeException(" {type} not available");
		}
	}
	private Response energyComparedSource(List<SourceFirstNetBaseView> firstNetBases, EnergyInfoDto dto) {
		return sourceConverge(firstNetBases, dto);
	}

	private Response energyComparedStation(List<StationFirstNetBaseView> firstNetBases, EnergyInfoDto dto) {
		switch (dto.getType()) {
			case 3:/*热力站*/
				return stationConverge(firstNetBases, dto);
			case 2:/*热网*/
				List<EnergyChartResponse> energyChartResponses = new ArrayList<>();
				List<NetSource> netSources = commonService.netJoinSource(-1);
				//网-源
				Map<Integer, List<NetSource>> netMap = netSources.stream().collect(Collectors.groupingBy(NetSource::getNetId));
				//源-站
				Map<Integer, List<StationFirstNetBaseView>> map = firstNetBases.stream().collect(Collectors.groupingBy(StationFirstNetBaseView::getHeatSourceId));
				Set<Integer> netIds = netMap.keySet();
				for (Integer netId : netIds) {
					List<NetSource> netSources1 = netMap.get(netId);
					List<StationFirstNetBaseView> firstNetBaseViews = new ArrayList<>(16);
					for (NetSource source : netSources1) {
						List<StationFirstNetBaseView> views = map.get(source.getSourceId());
						if (CollectionUtils.isNotEmpty(views)){
							firstNetBaseViews.addAll(views);
						}
					}
					HashMap<String, Object>[] hashMaps = this.netPage(firstNetBaseViews, dto);
					if (hashMaps != null){
						energyChartResponses.add(this.convergeNetSingeCharts(hashMaps, netMap.get(netId).get(0), dto));
					}
				}
//				HashMap<String, Object> res = new HashMap<>(16);
//
//
//				List<EnergyChartResponse> energyChartResponses;
//				for (StationFirstNetBaseView firstNetBase : firstNetBases) {
//
//				}
//				energyChartResponses = this.convergeNetSingeCharts();
				return Response.success(CommonService.sorted(energyChartResponses,dto));
			default:
				return Response.paramError();
		}
	}

	private EnergyChartResponse convergeNetSingeCharts(HashMap<String, Object>[] energyDatum, NetSource netSource, EnergyInfoDto dto) {
		EnergyChartResponse response = new EnergyChartResponse();
//		String name = firstNetBase.getHeatNetName();
		String name = netSource.getNetName();
		switch (dto.getEnergyType()) {
			case 1:
				/*水*/
				response.setWaterStation(energyDatum[0], name);
				break;
			case 2:
				/*电*/
				response.setEleStation(energyDatum[0], name);
				break;
			case 3:
				/*热*/
				response.setHeatStation(energyDatum[0], name);
				break;
			default:
				throw new RuntimeException("type is not available");
		}
		return response;
	}

	//分热网历史数据加载
	private HashMap<String, Object>[] netConverge(List<StationFirstNetBaseView> firstNetBases, EnergyInfoDto dto) {
		Integer[] ids = new Integer[firstNetBases.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = firstNetBases.get(i).getHeatSystemId();
		}
		return this.getEnergyData(dto, ids, 2);
	}

	private Response sourceConverge(List<SourceFirstNetBaseView> firstNetBases, EnergyInfoDto dto) {
		HashMap<String, Object>[] energyData;
		Integer[] ids;
		if (dto.getRelevanceId() == null) {
			ids = new Integer[firstNetBases.size()];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = firstNetBases.get(i).getHeatSystemId();
			}
		} else {
			ids = new Integer[]{dto.getRelevanceId()};
		}
		energyData = this.getEnergyDataConverge(dto, ids);
		List<EnergyChartResponse> list = new ArrayList<>();
		if (energyData == null) {
			return Response.success();
		}
		for (HashMap<String, Object> energyDatum : energyData) {
			EnergyChartResponse response = new EnergyChartResponse();
			String name = this.getSourceName(Integer.parseInt(energyDatum.get("relevanceId").toString()), firstNetBases);
			switch (dto.getEnergyType()) {
				case 1:
					/*水*/
					response.setWaterSource(energyDatum, name);
					break;
				case 2:
					/*电*/
					response.setEleSource(energyDatum, name);
					break;
				case 3:
					/*热*/
					response.setHeatSource(energyDatum, name);
					break;
				default:
					throw new RuntimeException("type is not available");
			}
			list.add(response);
		}
		return Response.success(CommonService.sorted(list,dto));
	}

	private Response stationConverge(List<StationFirstNetBaseView> firstNetBases, EnergyInfoDto dto) {
		HashMap<String, Object>[] energyData;
		Integer[] ids;
		if (dto.getRelevanceId() == null) {
			ids = new Integer[firstNetBases.size()];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = firstNetBases.get(i).getHeatSystemId();
			}
		} else {
			ids = new Integer[]{dto.getRelevanceId()};
		}
		energyData = this.getEnergyDataConverge(dto, ids);
		List<EnergyChartResponse> list = new ArrayList<>();
		if (energyData == null) {
			return Response.success();
		}
		for (HashMap<String, Object> energyDatum : energyData) {
			EnergyChartResponse response = new EnergyChartResponse();
			if (dto.getType() == 2) {
				String name = getNetName(Integer.parseInt(energyDatum.get("relevanceId").toString()), firstNetBases);
				switch (dto.getEnergyType()) {
					case 1:
						/*水*/
						response.setWaterStation(energyDatum, name);
						break;
					case 2:
						/*电*/
						response.setEleStation(energyDatum, name);
						break;
					case 3:
						/*热*/
						response.setHeatStation(energyDatum, name);
						break;
					default:
						throw new RuntimeException("type is not available");
				}
			} else if (dto.getType() == 3) {
				String name = getStationName(Integer.parseInt(energyDatum.get("relevanceId").toString()), firstNetBases);
				switch (dto.getEnergyType()) {
					case 1:
						/*水*/
						response.setWaterStation(energyDatum, name);
						break;
					case 2:
						/*电*/
						response.setEleStation(energyDatum, name);
						break;
					case 3:
						/*热*/
						response.setHeatStation(energyDatum, name);
						break;
					default:
						throw new RuntimeException("type is not available");
				}
			}
			list.add(response);
		}
		return Response.success(CommonService.sorted(list,dto));
	}


	private String getSourceName(Integer reId, List<SourceFirstNetBaseView> firstNetBases) {
		return firstNetBases.stream().filter(e -> e.getHeatSystemId().equals(reId)).map(SourceFirstNetBaseView::getHeatSourceName).findFirst().orElse("未知热源");
	}

	private String getStationName(Integer reId, List<StationFirstNetBaseView> firstNetBases) {
		return firstNetBases.stream().filter(e -> e.getHeatSystemId().equals(reId)).map(StationFirstNetBaseView::getHeatTransferStationName).findFirst().orElse("未知热力站");
	}

	private String getNetName(Integer reId, List<StationFirstNetBaseView> firstNetBases) {
		return firstNetBases.stream().filter(e -> e.getHeatSystemId().equals(reId)).map(StationFirstNetBaseView::getHeatTransferStationName).findFirst().orElse("未知热网");
	}

	private List<TimeRange> convergeTimeRange(EnergyInfoDto dto) {
		List<TimeRange> timeRanges = new ArrayList<>();
		TimeRange timeRange = new TimeRange();
		String localDate = LocalTimeUtils.getLocalDate(LocalTimeUtils.getDay(-1));
		timeRange.setStart(dto.getStartTime());
		timeRange.setEnd(dto.getEndTime());
		timeRange.setIndex(localDate);
		timeRanges.add(timeRange);
		return timeRanges;
	}

	/**
	 * 获取能耗历史数据
	 */
	private HashMap<String, Object>[] getEnergyDataConverge(EnergyInfoDto dto, Integer[] stationSystemIds) {
		QueryEsBucketDto queryEsDto = new QueryEsBucketDto();
		queryEsDto.setTimeRanges(convergeTimeRange(dto));
		if (dto.getDateType() == 1) {
			queryEsDto.setDocument(HistoryDocument.HOUR);
		} else {
			queryEsDto.setDocument(HistoryDocument.DAY);
		}
		String[] fields;
		if (dto.getType() == 1) {
			fields = CommonService.SOURCE_FIELDS;
		} else {
			fields = CommonService.STATION_FIELDS;
		}
		queryEsDto.setIncludeFields(fields);

		queryEsDto.setHeatSystemId(stationSystemIds);
		queryEsDto.setSourceType(HistorySourceType.ENERGY_CONVERGE);
		queryEsDto.setIndex(1);
		return template.post(url + "bucket", queryEsDto, gatherSearch, HashMap[].class);
	}

	//endregion

}
