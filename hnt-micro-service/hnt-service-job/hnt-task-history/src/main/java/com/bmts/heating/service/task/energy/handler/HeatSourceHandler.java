package com.bmts.heating.service.task.energy.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.db.service.EnergyEvaluateHistoryService;
import com.bmts.heating.commons.entiy.energy.EnergyType;
import com.bmts.heating.commons.entiy.energy.EvaluateRateType;
import com.bmts.heating.commons.entiy.energy.EvaluateSection;
import com.bmts.heating.commons.entiy.energy.EvaluateTarget;
import com.bmts.heating.commons.utils.common.Tuple3;
import com.bmts.heating.compute.hitory.energy.UnitStandardCompute;
import com.bmts.heating.service.task.energy.pojo.EnergySystemData;
import com.bmts.heating.service.task.energy.pojo.NetSystemRelation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author naming
 * @description
 * @date 2021/5/16 20:06
 **/
@Service
@Slf4j
public class HeatSourceHandler {
    @Autowired
    CommonHandler commonHandler;
    @Autowired
    EnergyEvaluateHistoryService energyEvaluateHistoryService;

    public void excute(EnergyUnitStandardConfig energyUnitStandardConfig, BigDecimal avgTemp, List<SourceFirstNetBaseView> stationFirstNetBaseViews, List<Object> objects, List<EnergyEvaluateConfig> list, EvaluateTarget target) {
        int temp = avgTemp.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        Map<Integer, List<NetSystemRelation>> targetGroups = stationFirstNetBaseViews.stream().filter(x -> x.getHeatSourceId() != null).map(x -> {
            NetSystemRelation netStationRelation = new NetSystemRelation();
            netStationRelation.setTargetId(x.getHeatSourceId());
            netStationRelation.setNetName(x.getHeatSourceName());
            netStationRelation.setSystemId(x.getHeatSystemId());
            return netStationRelation;
        }).collect(Collectors.groupingBy(NetSystemRelation::getTargetId));
        /**
         * ??????????????????
         */
        List<EnergySystemData> historyData = objects.stream().map(x -> JSONObject.parseObject(JSONObject.toJSONString(x), EnergySystemData.class)).collect(Collectors.toList());
        /**
         * ???????????? ????????????
         */
        List<EnergyEvaluateHistory> energyEvaluateHistories = new ArrayList<>(targetGroups.keySet().size() * 3);
        for (Map.Entry<Integer, List<NetSystemRelation>> entry : targetGroups.entrySet()) {
            Integer k = entry.getKey();
            List<NetSystemRelation> v = entry.getValue();
            /**
             * key ??????id value ???NetSystemRelation
             */
            BigDecimal water = new BigDecimal(0);
            BigDecimal ep = new BigDecimal(0);
            BigDecimal heat = new BigDecimal(0);
            /**
             * ??????????????????
             */
            String sourceName = "";
            /**
             * ?????? ???????????????
             */
            for (NetSystemRelation netSystemRelation : v) {
                EnergySystemData energySystemData = historyData.stream().filter(x -> x.getRelevanceId() == netSystemRelation.getSystemId()).findFirst().orElse(null);
                if (energySystemData != null) {
                    if (energySystemData.getHM_HT() != null)
                        heat = heat.add(UnitStandardCompute.unitStandard(energyUnitStandardConfig.getOutdoorTemp(), energyUnitStandardConfig.getIndoorTemp(), avgTemp, energySystemData.getHM_HT()));
                    if (energySystemData.getWM_FT() != null)
                        water = water.add(energySystemData.getWM_FT());
                    if (energySystemData.getEp() != null)
                        ep = ep.add(energySystemData.getEp());
                    sourceName = netSystemRelation.getNetName();
                }

            }
            Tuple3<String, EvaluateRateType, BigDecimal> waterEvaluate = Evaluate(water, list, EnergyType.HeatSourceFTSupply, temp);
            Tuple3<String, EvaluateRateType, BigDecimal> epEvaluate = Evaluate(ep, list, EnergyType.HeatSourceEp, temp);
            Tuple3<String, EvaluateRateType, BigDecimal> heatEvaluate = Evaluate(heat, list, EnergyType.HeatSourceTotalHeat_MtrG, temp);
            if (waterEvaluate == null || epEvaluate == null || heatEvaluate == null) {
                log.error("??????????????????????????????");
                return;
            }
            energyEvaluateHistories.add(commonHandler.buildDbDatas(waterEvaluate, k, sourceName, water, EnergyType.HeatSourceFTSupply, target));
            energyEvaluateHistories.add(commonHandler.buildDbDatas(epEvaluate, k, sourceName, ep, EnergyType.HeatSourceEp, target));
            energyEvaluateHistories.add(commonHandler.buildDbDatas(heatEvaluate, k, sourceName, heat, EnergyType.HeatSourceTotalHeat_MtrG, target));
        }
        try {
            energyEvaluateHistoryService.saveBatch(energyEvaluateHistories);
        } catch (Exception e) {
            log.error("???????????????????????? {}", e);
        }
    }

    /**
     * ??????
     *
     * @param value      ??????????????????
     * @param list       ???????????????db
     * @param energyType ???????????????
     * @return
     */
    private Tuple3<String, EvaluateRateType, BigDecimal> Evaluate(BigDecimal value, List<EnergyEvaluateConfig> list, EnergyType energyType, int avgTemp) {
        EnergyEvaluateConfig energyEvaluateConfig = null;
        if (energyType == EnergyType.HeatSourceTotalHeat_MtrG) //
            energyEvaluateConfig = list.stream().filter(x -> x.getEnergyType() == (energyType.ordinal() -2) && x.getTemperature() == avgTemp).findFirst().orElse(null);
        else
            energyEvaluateConfig = list.stream().filter(x -> x.getEnergyType() == energyType.ordinal() - 2).findFirst().orElse(null);
        if (energyEvaluateConfig == null)
            return null;
        float computeValue = (value.floatValue() - energyEvaluateConfig.getReferenceVal().floatValue() * 10) / (energyEvaluateConfig.getReferenceVal().floatValue() * 10);
        JSON json = energyEvaluateConfig.getEvaluate();
        JSONObject evaluateConfig = json.toJavaObject(JSONObject.class);
        /**
         * ?????? ????????????
         */
        String evaluate = null;
        for (Map.Entry<String, Object> entry : evaluateConfig.getInnerMap().entrySet()) {
            EvaluateSection section = JSONObject.parseObject(JSONObject.toJSONString(entry.getValue()), EvaluateSection.class);
            if (computeValue < section.getUp() && computeValue >= section.getDown()) {
                evaluate = entry.getKey();
                break;
            }
        }
        if (evaluate == null) {
            log.warn("?????????????????? ?????????????????????{}", computeValue);
            evaluate = "excellent";
        }
        /**
         * ??????????????????
         */
        JSONObject jsonObject = energyEvaluateConfig.getQualified().toJavaObject(JSONObject.class);
        EvaluateSection section = JSONObject.parseObject(jsonObject.get("excellent").toString(), EvaluateSection.class);
        EvaluateRateType qualified = EvaluateRateType.qualified;
        if (computeValue < section.getDown()) {
            qualified = EvaluateRateType.notQualified;
        } else if (computeValue > section.getUp()) qualified = EvaluateRateType.passQualified;

        return new Tuple3<>(evaluate, qualified, energyEvaluateConfig.getReferenceVal());
    }

}
