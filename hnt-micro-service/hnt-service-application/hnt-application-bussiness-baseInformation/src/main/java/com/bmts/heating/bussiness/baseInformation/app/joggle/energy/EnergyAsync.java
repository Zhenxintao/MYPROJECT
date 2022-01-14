package com.bmts.heating.bussiness.baseInformation.app.joggle.energy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.db.entity.EnergyConsumption;
import com.bmts.heating.commons.basement.model.db.entity.HeatTransferStation;
import com.bmts.heating.commons.basement.model.db.entity.SourceFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.db.service.HeatTransferStationService;
import com.bmts.heating.commons.db.service.SourceFirstNetBaseViewService;
import com.bmts.heating.commons.db.service.StationFirstNetBaseViewService;
import com.bmts.heating.commons.entiy.baseInfo.request.EnergyConsumptionDto;
import com.bmts.heating.commons.entiy.baseInfo.request.energyConsumption.Datas;
import com.bmts.heating.commons.entiy.baseInfo.request.energyConsumption.InsertRequest;
import com.bmts.heating.commons.entiy.baseInfo.request.energyConsumption.PointValue;
import com.bmts.heating.commons.entiy.baseInfo.request.energyConsumption.Tag;
import com.bmts.heating.commons.utils.tdengine.TdAggregateTableIndex;
import com.bmts.heating.middleware.td.EnergyConsumptionClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class EnergyAsync {

	@Autowired
	private HeatTransferStationService heatTransferStationService;

	@Autowired
	private SourceFirstNetBaseViewService sourceFirstNetBaseViewService;

	@Autowired
	private EnergyConsumptionClient energyConsumptionClient;


	//向td插入数据
	@Async("asyncEnergyExecutor")
	public void insert(List<EnergyConsumption> energyConsumptions, EnergyConsumptionDto dto) {
		Datas datasHour = null;
		Datas datasDay = null;
		Tag tag = new Tag();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
		LocalDateTime startTime = LocalDateTime.parse(dto.getStartTime(), formatter);
		LocalDateTime endTime = LocalDateTime.parse(dto.getEndTime(), formatter);
		Duration between = Duration.between(startTime, endTime);
		String tableNameHour = null;
		String tableNameDay = null;
		String stableNameHour = null;
		String stableNameDay = null;
		Integer groupId = 0;
		Boolean flagHour = false;
		Boolean flagDay = false;
		long l = System.currentTimeMillis();
		log.info("{}",l);
		if (dto.getType() == 3) {
			for (EnergyConsumption energyConsumption : energyConsumptions) {
				List<Datas> listDataHour = new ArrayList<>();
				List<Datas> listDataDay = new ArrayList<>();
				List<PointValue> listPointValueHour = new ArrayList<>();
				List<PointValue> listPointValueDay = new ArrayList<>();
				PointValue pointValueHour = new PointValue();
				PointValue pointValueDay = new PointValue();
				//导入的是热力站  查询出该热力站对应的全部控制柜  控制柜下对应多个系统
				QueryWrapper queryWrapper = new QueryWrapper();
				queryWrapper.eq("name", energyConsumption.getName());
				List<HeatTransferStation> list1 = heatTransferStationService.list(queryWrapper);
				if (CollectionUtils.isEmpty(list1)) {
					log.error("插入td数据失败，该站不存在{}",energyConsumption);
				}
				if (dto.getEnergyType() == 2) {
					//水
					pointValueHour.setName("wm_ft");
					pointValueHour.setValue(energyConsumption.getValue() /between.toHours());
					listPointValueHour.add(pointValueHour);
					pointValueDay.setName("wm_ft");
					pointValueDay.setValue(energyConsumption.getValue() / between.toDays());
					listPointValueDay.add(pointValueDay);
				} else if (dto.getEnergyType() == 1) {
					pointValueHour.setName("ep");
					pointValueHour.setValue(energyConsumption.getValue() / between.toHours());
					listPointValueHour.add(pointValueHour);
					pointValueDay.setName("ep");
					pointValueDay.setValue(energyConsumption.getValue() /  between.toDays());
					listPointValueDay.add(pointValueDay);
				} else {
					//热
					pointValueHour.setName("hm_ht");
					pointValueHour.setValue(energyConsumption.getValue() /  between.toHours());
					listPointValueHour.add(pointValueHour);
					pointValueDay.setName("hm_ht");
					pointValueDay.setValue(energyConsumption.getValue() /between.toDays());
					listPointValueDay.add(pointValueDay);
				}
				for (long i = 0; i < between.toHours(); i++) {
					datasHour = new Datas();
					datasHour.setTs(startTime.plusHours(i).toLocalDate().atTime(startTime.plusHours(i).getHour(), 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli());
					datasHour.setPointValues(listPointValueHour);
					listDataHour.add(datasHour);
				}
				for (long i = 0; i < between.toDays(); i++) {
					datasDay= new Datas();
					datasDay.setTs(startTime.plusDays(i).toLocalDate().atStartOfDay().toLocalDate().atTime(startTime.plusDays(i).toLocalDate().atStartOfDay().getHour(), 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli());
					datasDay.setPointValues(listPointValueDay);
					listDataDay.add(datasDay);
				}
				dto.setLevel(1);
				//该站含有多个 groupId
				groupId = list1.get(0).getId();
				//热力站
				tableNameHour = TdAggregateTableIndex.STATION_ENERGY_HOUR.getIndex() + "_" + groupId + "_" + dto.getLevel();
				stableNameHour = TdAggregateTableIndex.STATION_ENERGY_HOUR.getIndex();
				tableNameDay = TdAggregateTableIndex.STATION_ENERGY_DAY.getIndex() + "_" + groupId + "_" + dto.getLevel();
				stableNameDay = TdAggregateTableIndex.STATION_ENERGY_DAY.getIndex();
				tag.setGroupId(groupId);
				tag.setLevel(dto.getLevel());
				InsertRequest insertRequestHour = new InsertRequest();
				insertRequestHour.setDatasList(listDataHour);
				insertRequestHour.setStableName(stableNameHour);
				insertRequestHour.setTableName(tableNameHour);
				insertRequestHour.setTag(tag);
				insertRequestHour.setStartTime(startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
				insertRequestHour.setEndTime(endTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
				InsertRequest insertRequestDay = new InsertRequest();
				insertRequestDay.setDatasList(listDataDay);
				insertRequestDay.setStableName(stableNameDay);
				insertRequestDay.setTag(tag);
				insertRequestDay.setTableName(tableNameDay);
				insertRequestDay.setStartTime(startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
				insertRequestDay.setEndTime(endTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
				flagHour = energyConsumptionClient.insertEnergyConsumption(insertRequestHour);
				flagDay = energyConsumptionClient.insertEnergyConsumption(insertRequestDay);
				if (!flagHour || !flagDay) {
					log.error("数据插入td库失败");
				}

			}
		} else {
			for (EnergyConsumption energyConsumption : energyConsumptions) {
				List<Datas> listDataHour = new ArrayList<>();
				List<Datas> listDataDay = new ArrayList<>();
				List<PointValue> listPointValueHour = new ArrayList<>();
				List<PointValue> listPointValueDay = new ArrayList<>();
				PointValue pointValueHour = new PointValue();
				PointValue pointValueDay = new PointValue();
				//导入的是热源
				QueryWrapper queryWrapper = new QueryWrapper();
				queryWrapper.eq("heatSourceName", energyConsumption.getName());
				//queryWrapper.groupBy("heatCabinetId");
				List<SourceFirstNetBaseView> list = sourceFirstNetBaseViewService.list(queryWrapper);
				if (CollectionUtils.isEmpty(list)) {
					log.error("插入td数据失败，该源不存在{}",energyConsumption);
				}
				if (dto.getEnergyType() == 2) {
					//水
					pointValueHour.setName("heatsourcetotalheat_mtrg");
					pointValueHour.setValue(energyConsumption.getValue() / between.toHours());
					listPointValueHour.add(pointValueHour);
					pointValueDay.setName("heatsourcetotalheat_mtrg");
					pointValueDay.setValue(energyConsumption.getValue() /  between.toDays());
					listPointValueDay.add(pointValueDay);
				} else if (dto.getEnergyType() == 1) {
					//电
					pointValueHour.setName("heatsourceep");
					pointValueHour.setValue(energyConsumption.getValue() / between.toHours());
					listPointValueHour.add(pointValueHour);
					pointValueDay.setName("heatsourceep");
					pointValueDay.setValue(energyConsumption.getValue() / between.toDays());
					listPointValueDay.add(pointValueDay);
				} else {
					//热
					pointValueHour.setName("heatsourceftsupply");
					pointValueHour.setValue(energyConsumption.getValue() / between.toHours());
					listPointValueHour.add(pointValueHour);
					pointValueDay.setName("heatsourceftsupply");
					pointValueDay.setValue(energyConsumption.getValue() /between.toDays());
					listPointValueDay.add(pointValueDay);
				}
				for (long i = 0; i < between.toHours(); i++) {
					datasHour = new Datas();
					datasHour.setTs(startTime.plusHours(i).toLocalDate().atTime(startTime.plusHours(i).getHour(), 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli());
					datasHour.setPointValues(listPointValueHour);
					listDataHour.add(datasHour);
				}
				for (long i = 0; i < between.toDays(); i++) {
					datasDay= new Datas();
					datasDay.setTs(startTime.plusDays(i).toLocalDate().atStartOfDay().toLocalDate().atTime(startTime.plusDays(i).toLocalDate().atStartOfDay().getHour(), 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli());
					datasDay.setPointValues(listPointValueDay);
					listDataDay.add(datasDay);
				}

				dto.setLevel(1);
				groupId = list.get(0).getHeatSystemId();
				tableNameHour = TdAggregateTableIndex.SOURCE_ENERGY_HOUR.getIndex() + "_" + groupId + "_" + dto.getLevel();
				stableNameHour = TdAggregateTableIndex.SOURCE_ENERGY_HOUR.getIndex();
				tableNameDay = TdAggregateTableIndex.SOURCE_ENERGY_DAY.getIndex() + "_" + groupId + "_" + dto.getLevel();
				stableNameDay = TdAggregateTableIndex.SOURCE_ENERGY_DAY.getIndex();
				tag.setGroupId(groupId);
				tag.setLevel(dto.getLevel());
				InsertRequest insertRequestHour = new InsertRequest();
				insertRequestHour.setDatasList(listDataHour);
				insertRequestHour.setStableName(stableNameHour);
				insertRequestHour.setTableName(tableNameHour);
				insertRequestHour.setTag(tag);
				insertRequestHour.setStartTime(startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
				insertRequestHour.setEndTime(endTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
				InsertRequest  insertRequestDay = new InsertRequest();
				insertRequestDay.setDatasList(listDataDay);
				insertRequestDay.setStableName(stableNameDay);
				insertRequestDay.setTag(tag);
				insertRequestDay.setTableName(tableNameDay);
				insertRequestDay.setStartTime(startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
				insertRequestDay.setEndTime(endTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
				flagHour = energyConsumptionClient.insertEnergyConsumption(insertRequestHour);
				flagDay = energyConsumptionClient.insertEnergyConsumption(insertRequestDay);
				if (!flagHour || !flagDay) {
					log.error("数据插入td库失败");
				}
			}
		}
		long l1 = System.currentTimeMillis();
		log.info("{}",l1);
	}
}
