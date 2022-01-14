package com.bmts.heating.web.energy.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bmts.heating.commons.basement.model.db.entity.EnergyUnitStandardConfig;
import com.bmts.heating.commons.basement.model.db.entity.NetSource;
import com.bmts.heating.commons.basement.model.db.entity.SourceFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.entiy.gathersearch.request.*;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.energy.pojo.EnergyInfoDto;
import com.bmts.heating.web.energy.pojo.EnergyInfoResponse;
import com.bmts.heating.web.energy.service.CommonService;
import com.bmts.heating.web.energy.service.EnergyReportService;
import com.bmts.heating.web.energy.service.config.EnergyUnitStandardConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EnergyReportServiceImpl implements EnergyReportService {

	@Autowired
	private TSCCRestTemplate template;
	@Autowired
	private CommonService commonService;
	@Autowired
	private EnergyUnitStandardConfigService energyUnitStandardConfigService;

	private final String url = "/energy/";
	private final String gatherSearch = "gather_search";

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

	private Response pageSource(List<SourceFirstNetBaseView> firstNetBases, EnergyInfoDto dto) {
		return this.sourcePage(firstNetBases, dto);
	}

	private Response pageStation(List<StationFirstNetBaseView> firstNetBases, EnergyInfoDto dto) {
		switch (dto.getType()) {
			case 3:
				/*热力站*/
				return this.stationPage(firstNetBases, dto);
			/*热网*/
			case 2:
				List<EnergyInfoResponse> energyInfoResponses = new ArrayList<>();
				Map<Integer, List<NetSource>> netMap = commonService.netJoinSource(-1).stream().collect(Collectors.groupingBy(NetSource::getNetId));
				Map<Integer, List<StationFirstNetBaseView>> map = firstNetBases.stream().collect(Collectors.groupingBy(StationFirstNetBaseView::getHeatSourceId));
				Set<Integer> netIds = netMap.keySet();
				for (Integer netId : netIds) {
					List<NetSource> netSources = netMap.get(netId);
					List<StationFirstNetBaseView> firstNetBaseViews = new ArrayList<>();
					for (NetSource netSource : netSources) {
						List<StationFirstNetBaseView> firstNetBaseViews1 = map.get(netSource.getSourceId());
						if (CollectionUtils.isNotEmpty(firstNetBaseViews1)){
							firstNetBaseViews.addAll(map.get(netSource.getSourceId()));
						}
					}
					Map<String, Object>[] resMap = this.netPage(firstNetBaseViews, dto);

					energyInfoResponses.addAll(this.convergeNetSinge(resMap, netMap.get(netId).get(0), dto));
				}
				HashMap<String, Object> res = new HashMap<>(16);
				res.put("data", CommonService.sorted(energyInfoResponses,dto));
				res.put("total", energyInfoResponses.size());
				return Response.success(res);
			default:
				return Response.paramError();
		}
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
		EnergyUnitStandardConfig unitStandardConfig = energyUnitStandardConfigService.info();
		return Response.success(this.packageDataSource(energyData, firstNetBases, dto, avgTemperature, unitStandardConfig));
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
		EnergyUnitStandardConfig unitStandardConfig = energyUnitStandardConfigService.info();
		return Response.success(this.packageData(energyData, firstNetBases, dto, avgTemperature, unitStandardConfig));
	}

	//获取能耗历史数据
	private HashMap<String, Object>[] getEnergyData(EnergyInfoDto dto, Integer[] stationSystemIds, int convergeType) {
		QueryEsDto queryEsDto = new QueryEsDto();
		queryEsDto.setStart(dto.getStartTime());
		queryEsDto.setEnd(dto.getEndTime());
		if (dto.getDateType() == 1) {
			queryEsDto.setDocument(HistoryDocument.HOUR);
		} else {
			queryEsDto.setDocument(HistoryDocument.DAY);
		}
		queryEsDto.setHeatSystemId(stationSystemIds);
		queryEsDto.setSourceType(HistorySourceType.ENERGY_CONVERGE);
		queryEsDto.setIndex(convergeType);
		queryEsDto.setSize(dto.getPageCount());
		queryEsDto.setCurrentPage(dto.getCurrentPage());
		queryEsDto.setIncludeTotal(true);
		return template.post(url + "table", queryEsDto, gatherSearch, HashMap[].class);
	}

	//打包返回结果集
	private HashMap<String, Object> packageData(Map<String, Object>[] energyData, List<StationFirstNetBaseView> firstNetBases,
												EnergyInfoDto dto, Object avgTemperature, EnergyUnitStandardConfig unitStandardConfig) {
		final Map<Integer, List<StationFirstNetBaseView>> firstNetBaseMap = firstNetBases.stream().collect(Collectors.groupingBy(StationFirstNetBaseView::getHeatSystemId));
		Map<String, Object>[] energyDataNew = new HashMap[energyData.length-1];
		if (energyDataNew.length >= 0) {
			System.arraycopy(energyData, 0, energyDataNew, 0, energyDataNew.length);
		}
		List<EnergyInfoResponse> list = new ArrayList<>();
		for (Map<String, Object> energyDatum : energyDataNew) {
			Integer relevanceId = Integer.parseInt(energyDatum.get("relevanceId").toString());
			EnergyInfoResponse homeResponse = new EnergyInfoResponse();
			StationFirstNetBaseView firstNetBase = firstNetBaseMap.get(relevanceId).get(0);
			if (dto.getType() == 3) {
				homeResponse.setStation(energyDatum, avgTemperature, unitStandardConfig);
				homeResponse.setName(firstNetBase.getHeatTransferStationName());
			} else {/*热源*/
				homeResponse.setSource(energyDatum, avgTemperature, unitStandardConfig);
				homeResponse.setName(firstNetBase.getHeatSourceName());
			}
			list.add(homeResponse);
		}
		int size = energyData != null ? (int) energyData[energyData.length-1].get("total") : 1;
		HashMap<String, Object> res = new HashMap<>();
		res.put("data", list);
		res.put("total", size);
		return res;
	}

	//打包返回结果集
	private HashMap<String, Object> packageDataSource(Map<String, Object>[] energyData, List<SourceFirstNetBaseView> firstNetBases,
												EnergyInfoDto dto, Object avgTemperature, EnergyUnitStandardConfig unitStandardConfig) {
		final Map<Integer, List<SourceFirstNetBaseView>> firstNetBaseMap = firstNetBases.stream().collect(Collectors.groupingBy(SourceFirstNetBaseView::getHeatSystemId));
		Map<String, Object>[] energyDataNew = new HashMap[energyData.length-1];
		if (energyDataNew.length >= 0) {
			System.arraycopy(energyData, 0, energyDataNew, 0, energyDataNew.length);
		}
		List<EnergyInfoResponse> list = new ArrayList<>();
		for (Map<String, Object> energyDatum : energyDataNew) {
			Integer relevanceId = Integer.parseInt(energyDatum.get("relevanceId").toString());
			EnergyInfoResponse homeResponse = new EnergyInfoResponse();
			SourceFirstNetBaseView firstNetBase = firstNetBaseMap.get(relevanceId).get(0);
			homeResponse.setSource(energyDatum, avgTemperature, unitStandardConfig);
			homeResponse.setName(firstNetBase.getHeatSourceName());
			list.add(homeResponse);
		}
		int size = energyData != null ? (int) energyData[energyData.length-1].get("total") : 1;
		HashMap<String, Object> res = new HashMap<>();
		res.put("data", list);
		res.put("total", size);
		return res;
	}

	/**
	 * 单独热网汇聚
	 */
	private List<EnergyInfoResponse> convergeNetSinge(Map<String, Object>[] energyData, NetSource netSource, EnergyInfoDto dto) {
		List<EnergyInfoResponse> res = new ArrayList<>();
		for (Map<String, Object> energyDatum : energyData) {
			EnergyInfoResponse response;
			if (energyDatum != null) {
				response = new EnergyInfoResponse();
				TimeRange timeRange = new TimeRange();
				timeRange.setStart(dto.getStartTime());
				timeRange.setEnd(dto.getEndTime());
				Object avgTemperature = commonService.avgTemperature(timeRange).get("avgTemperature");
				EnergyUnitStandardConfig unitStandardConfig = energyUnitStandardConfigService.info();
				response.setStation(energyDatum, avgTemperature, unitStandardConfig);
				response.setName(netSource.getNetName());
				res.add(response);
			}
		}
		return res;
	}

	/**
	 * 分热网历史数据加载
	 * @param firstNetBases 基础信息
	 * @param dto 入参
	 * @return map
	 */
	private Map<String, Object>[] netPage(List<StationFirstNetBaseView> firstNetBases, EnergyInfoDto dto) {
		Integer[] ids = new Integer[firstNetBases.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = firstNetBases.get(i).getHeatSystemId();
		}
		return this.getEnergyData(dto, ids, 2);
	}
}
