package com.bmts.heating.service.task.energy.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.db.service.EnergyEvaluateConfigService;
import com.bmts.heating.commons.db.service.EnergyEvaluateHistoryService;
import com.bmts.heating.commons.db.service.EnergyUnitStandardConfigService;
import com.bmts.heating.commons.entiy.energy.EnergyType;
import com.bmts.heating.commons.entiy.energy.EvaluateRateType;
import com.bmts.heating.commons.entiy.energy.EvaluateSection;
import com.bmts.heating.commons.entiy.energy.EvaluateTarget;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.HistoryEnergyDataResponse;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.HistoryEnergyResponse;
import com.bmts.heating.commons.utils.common.Tuple3;
import com.bmts.heating.compute.hitory.energy.UnitStandardCompute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author naming
 * @description
 * @date 2021/4/28 13:17
 **/
@Service
@Slf4j
public class StationHandler {
    @Autowired
    EnergyEvaluateConfigService energyEvaluateConfigService;
    @Autowired
    EnergyUnitStandardConfigService energyUnitStandardConfigService;
    @Autowired
    EnergyEvaluateHistoryService energyEvaluateHistoryService;
    @Autowired
    CommonHandler commonHandler;

    public void excute(EnergyUnitStandardConfig energyUnitStandardConfig, BigDecimal avgTemp, List<HeatTransferStation> stationFirstNetBaseViews, HistoryEnergyDataResponse historyEnergyDataResponse, List<EnergyEvaluateConfig> list) {
        List<EnergyEvaluateHistory> energyEvaluateHistories = new ArrayList<>();
        List<HistoryEnergyResponse> responseData = historyEnergyDataResponse.getResponseData();

        for (HistoryEnergyResponse object : responseData) {
            List<EnergyEvaluateHistory> energyEvaluateHistory = EvaluateItem(object, list, avgTemp, stationFirstNetBaseViews, energyUnitStandardConfig);
            if (energyEvaluateHistory != null) {
                energyEvaluateHistories.addAll(energyEvaluateHistory);
            }
        }

        try {
            energyEvaluateHistoryService.saveBatch(energyEvaluateHistories);
        } catch (Exception e) {
            log.error("评价数据插入失败 {}", e);
        }
    }

    public static void main(String[] args) {

        System.out.print(new BigDecimal(4.3).setScale(0, BigDecimal.ROUND_HALF_UP));
//        System.out.print(LocalTimeUtils.getHour(0));
//        System.out.print(LocalTimeUtils.getHour(-24));
    }


    /**
     * 评价每一个热力站
     *
     * @param energyData               历史数据水电热
     * @param list                     评价配置信息
     * @param avgTemp                  平均温度
     * @param energyUnitStandardConfig 温度配置
     * @return
     */
    private List<EnergyEvaluateHistory> EvaluateItem(HistoryEnergyResponse energyData, List<EnergyEvaluateConfig> list, BigDecimal avgTemp, List<HeatTransferStation> stationFirstNetBaseViews, EnergyUnitStandardConfig energyUnitStandardConfig) {
        BigDecimal heat = energyData.getNiggerHeadHeatUnitConsumption();
        BigDecimal water =energyData.getWaterConsumption();
        BigDecimal ep = energyData.getElectricityConsumption();
        int temp = avgTemp.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        Integer stationId =energyData.getRelevanceId();
        HeatTransferStation heatTransferStation = stationFirstNetBaseViews.stream().filter(x -> x.getId().equals(stationId)).findFirst().orElse(null);
        List<EnergyEvaluateHistory> result = new ArrayList<>(3);
        if (heatTransferStation != null) {
            /**
             * 评价水电热
             */
            Tuple3<String, EvaluateRateType, BigDecimal> waterEvaluate = Evaluate(water, list, EnergyType.WM_FT, temp);
            Tuple3<String, EvaluateRateType, BigDecimal> epEvaluate = Evaluate(ep, list, EnergyType.Ep, temp);
            Tuple3<String, EvaluateRateType, BigDecimal> heatEvaluate = Evaluate(heat, list, EnergyType.HM_HT, temp);
            if (waterEvaluate == null || epEvaluate == null || heatEvaluate == null) {
                log.error("数据库未配置相关信息");
                return null;
            }
            result.add(commonHandler.buildDbDatas(waterEvaluate, stationId, heatTransferStation.getName(), water, EnergyType.WM_FT, EvaluateTarget.station));
            result.add(commonHandler.buildDbDatas(epEvaluate, stationId, heatTransferStation.getName(), ep, EnergyType.Ep, EvaluateTarget.station));
            result.add(commonHandler.buildDbDatas(heatEvaluate, stationId, heatTransferStation.getName(), heat, EnergyType.HM_HT, EvaluateTarget.station));
            return result;
        } else return null;

    }


    /**
     * 评价
     *
     * @param value      水电热数值
     * @param list       评价配置项db
     * @param energyType 水电热类型
     * @return
     */
    private Tuple3<String, EvaluateRateType, BigDecimal> Evaluate(BigDecimal value, List<EnergyEvaluateConfig> list, EnergyType energyType, int avgTemp) {
        EnergyEvaluateConfig energyEvaluateConfig = null;
        if (energyType == EnergyType.HM_HT) //
            energyEvaluateConfig = list.stream().filter(x -> x.getEnergyType() == (energyType.ordinal() + 1) && x.getTemperature() == avgTemp).findFirst().orElse(null);
        else
            energyEvaluateConfig = list.stream().filter(x -> x.getEnergyType() == energyType.ordinal() + 1).findFirst().orElse(null);
        if (energyEvaluateConfig == null)
            return null;
        float computeValue = (value.floatValue() - energyEvaluateConfig.getReferenceVal().floatValue()) / energyEvaluateConfig.getReferenceVal().floatValue();
        JSON json = energyEvaluateConfig.getEvaluate();
        JSONObject evaluateConfig = json.toJavaObject(JSONObject.class);
        /**
         * 评价 优良中差
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
            log.warn("取评价配置项 未匹配到计算值{}", computeValue);
            evaluate = "excellent";
        }
        /**
         * 判断是否合格
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
