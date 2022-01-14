package com.bmts.heating.web.forecast.service.impl;

import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceBasic;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceCore;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceHeatSeason;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceHistory;
import com.bmts.heating.commons.basement.model.db.request.ForecastRequest;
import com.bmts.heating.commons.entiy.common.ForecastType;
import com.bmts.heating.commons.entiy.forecast.algorithm.*;
import com.bmts.heating.commons.entiy.forecast.response.ForecastTempResponse;
import com.bmts.heating.compute.forecast.heatsource.HeatSourceArithmetic;
import com.bmts.heating.web.forecast.strategy.ForecastComputeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 标准工况 计算方式
 */
@Service
public class StandardConditionServiceImpl implements ForecastComputeService {

    @Autowired
    private HeatSourceArithmetic heatSourceArithmetic;


    /**
     * 预测一次供回水温度
     */
    @Override
    public ForecastSourceHistory forecastTgTh(ForecastRequest forecastRequest) {

        // 预测热负荷率
        BigDecimal loadRate = loadRate(forecastRequest.getForecastSourceBasic(), forecastRequest.getForecastHourAvgTemperature());
        // 预测二次供回温度
        Map<String, BigDecimal> t2Map = t2gT2h(forecastRequest.getForecastSourceBasic(), forecastRequest.getSourceCore(), forecastRequest.getHeatSeason(), loadRate);
        // 预测系数
        BigDecimal coefficient = coefficient(forecastRequest.getSourceCore(), forecastRequest.getHeatSeason());
        //  预测一次供回水温度
        Map<String, BigDecimal> tMap = tgTh(forecastRequest.getSourceCore(), forecastRequest.getHeatSeason(), loadRate, t2Map, coefficient);

        ForecastSourceHistory forecastSourceHistory = new ForecastSourceHistory();
        forecastSourceHistory.setForecastLoadRate(loadRate);
        forecastSourceHistory.setForecastT1g(tMap.get("Tg").setScale(2, BigDecimal.ROUND_HALF_UP));
        forecastSourceHistory.setForecastT1h(tMap.get("Th").setScale(2, BigDecimal.ROUND_HALF_UP));
        forecastSourceHistory.setRadiatorT2g(t2Map.get("T2g").setScale(2, BigDecimal.ROUND_HALF_UP));
        forecastSourceHistory.setRadiatorT2h(t2Map.get("T2h").setScale(2, BigDecimal.ROUND_HALF_UP));
        // 室内计算温度
        forecastSourceHistory.setIndoorTemp(forecastRequest.getSourceCore().getInsideTemp());
        // 预测室外温度
        forecastSourceHistory.setForecastOutTemp(forecastRequest.getForecastHourAvgTemperature());

        return forecastSourceHistory;
    }

    /**
     * 预测用热量
     */
    @Override
    public ForecastSourceHistory forecastUseHeat(ForecastRequest forecastRequest) {
        // 预测用热量
        BigDecimal heatQuantity = BigDecimal.ZERO;

        // 预测热负荷指标
        BigDecimal heatLoad = heatLoadHour(forecastRequest.getForecastSourceBasic(), forecastRequest.getSourceCore(), forecastRequest.getForecastHourAvgTemperature());

        if (forecastRequest.getForecastType() == ForecastType.HOUR.value()) {
            // 预测热负荷指标
            //heatLoad = heatLoadHour(forecastRequest.getForecastSourceBasic(), forecastRequest.getSourceCore(), forecastRequest.getForecastHourAvgTemperature());
            heatQuantity = hourUseHeat(heatLoad, forecastRequest.getSourceCore(), forecastRequest.getAreaMultiple());
        } else {
            //  天 和 阶段
            // 预测热负荷指标
            //heatLoad = heatLoad(forecastRequest.getForecastSourceBasic(), forecastRequest.getSourceCore());
            heatQuantity = useHeat(heatLoad, forecastRequest.getSourceCore(), forecastRequest.getInterval(), forecastRequest.getAreaMultiple());
        }

        ForecastSourceHistory forecastSourceHistory = new ForecastSourceHistory();
        forecastSourceHistory.setForecastHot(heatQuantity.setScale(2, BigDecimal.ROUND_HALF_UP));
        forecastSourceHistory.setForecastThermalIndex(heatLoad.setScale(2, BigDecimal.ROUND_HALF_UP));
        // 预测负荷  和预测热量   值一样
        forecastSourceHistory.setForecastLoad(heatQuantity.setScale(2, BigDecimal.ROUND_HALF_UP));
        return forecastSourceHistory;
    }


    /**
     * @return java.math.BigDecimal
     * @Method loadRate
     * @Param [forecastSourceBasic, forecastHourAvgTemperature]
     * @Description 预测热负荷率
     */
    private BigDecimal loadRate(ForecastSourceBasic forecastSourceBasic, BigDecimal forecastHourAvgTemperature) {

        ForecastHeatLoadRate dto = new ForecastHeatLoadRate();
        dto.setDesignCountTemp(forecastSourceBasic.getInsideDesignTemp());
        dto.setDesignOutCountTemp(forecastSourceBasic.getOutsideConfigTemp());
        dto.setDesignForecastTemp(forecastHourAvgTemperature);
        float loadRate = heatSourceArithmetic.forecastHeatLoadRate(dto);
        if (Double.isNaN(loadRate)) {
            throw new RuntimeException("forecastHeatLoadRate计算异常---NaN");
        }
        return new BigDecimal(Float.toString(loadRate));
    }


    /**
     * @return java.util.Map<java.lang.String, java.math.BigDecimal>
     * @Method t2gT2h
     * @Param [forecastSourceBasic, sourceCore, heatSeason,loadRate]
     * @Description 预测二次供回温度
     */
    private Map<String, BigDecimal> t2gT2h(ForecastSourceBasic forecastSourceBasic, ForecastSourceCore sourceCore, ForecastSourceHeatSeason heatSeason, BigDecimal loadRate) {

        ForecastRadiatorSTemp dto = new ForecastRadiatorSTemp();
        dto.setDesignCountTemp(forecastSourceBasic.getInsideDesignTemp());
        dto.setDesignTempG(sourceCore.getRadiatorTg());
        dto.setDesignTempH(sourceCore.getRadiatorTh());
        dto.setRealCountTemp(sourceCore.getInsideTemp());
        dto.setLoadRateStandardConditions(loadRate);
        dto.setRelativeDischargeS(heatSeason.getSecondRelativeFlow());
        ForecastTempResponse forecastTempResponse = heatSourceArithmetic.forecastRadiatorSTemp(dto);
        float t2g = forecastTempResponse.getTg();
        float t2h = forecastTempResponse.getTh();
        if (Double.isNaN(t2g)) {
            throw new RuntimeException("forecastRadiatorSTemp计算异常---NaN");
        }
        if (Double.isNaN(t2h)) {
            throw new RuntimeException("forecastRadiatorSTemp计算异常---NaN");
        }
        Map<String, BigDecimal> map = new HashMap<>();
        map.put("T2g", new BigDecimal(Float.toString(t2g)));
        map.put("T2h", new BigDecimal(Float.toString(t2h)));
        return map;
    }


    /**
     * @return java.math.BigDecimal
     * @Method coefficient
     * @Param [sourceCore, heatSeason]
     * @Description 预测系数
     */
    private BigDecimal coefficient(ForecastSourceCore sourceCore, ForecastSourceHeatSeason heatSeason) {

        ForecastCoefficient dto = new ForecastCoefficient();
        dto.setDesignSTempG(sourceCore.getRadiatorTg());
        dto.setDesignSTempH(sourceCore.getRadiatorTh());
        dto.setDesignFTempG(sourceCore.getFirstNetTg());
        dto.setDesignFTempH(sourceCore.getFirstNetTh());
        dto.setRelativeDischargeF(heatSeason.getFirstNetRelativeFlow());
        dto.setRelativeDischargeS(heatSeason.getSecondRelativeFlow());
        float coefficient = heatSourceArithmetic.forecastCoefficient(dto);
        if (Double.isNaN(coefficient)) {
            throw new RuntimeException("forecastCoefficient计算异常---NaN");
        }
        return new BigDecimal(Float.toString(coefficient));
    }

    /**
     * @return java.util.Map<java.lang.String, java.math.BigDecimal>
     * @Method tgTh
     * @Param [sourceCore, heatSeason, loadRate, t2Map, coefficient]
     * @Description 预测一次供回水温度
     */
    private Map<String, BigDecimal> tgTh(ForecastSourceCore sourceCore, ForecastSourceHeatSeason heatSeason, BigDecimal loadRate, Map<String, BigDecimal> t2Map, BigDecimal coefficient) {

        ForecastFirstTemp dto = new ForecastFirstTemp();
        dto.setLoadRateStandardConditions(loadRate);
        dto.setDesignTempG(sourceCore.getFirstNetTg());
        dto.setDesignTempH(sourceCore.getFirstNetTh());
        dto.setRelativeDischargeF(heatSeason.getFirstNetRelativeFlow());
        dto.setForecastSTempG(t2Map.get("T2g"));
        dto.setForecastSTempH(t2Map.get("T2h"));
        dto.setCoefficient(coefficient);
        ForecastTempResponse forecastTempResponse = heatSourceArithmetic.forecastFirstTemp(dto);
        float tg = forecastTempResponse.getTg();
        float th = forecastTempResponse.getTh();
        if (Double.isNaN(tg)) {
            throw new RuntimeException("forecastFirstTemp计算异常---NaN");
        }
        if (Double.isNaN(th)) {
            throw new RuntimeException("forecastFirstTemp计算异常---NaN");
        }

        Map<String, BigDecimal> map = new HashMap<>();
        map.put("Tg", new BigDecimal(Float.toString(tg)));
        map.put("Th", new BigDecimal(Float.toString(th)));
        return map;
    }


    /**
     * 预测热负荷指标
     */
    private BigDecimal heatLoadHour(ForecastSourceBasic forecastSourceBasic, ForecastSourceCore sourceCore, BigDecimal forecastHourAvgTemperature) {

        ForecastTempHeatLoadIndex dto = new ForecastTempHeatLoadIndex();
        dto.setDesignCountTemp(forecastSourceBasic.getInsideDesignTemp());
        dto.setDesignOutCountTemp(forecastSourceBasic.getOutsideConfigTemp());
        dto.setDesignRealCountTemp(sourceCore.getInsideTemp());
        dto.setAreaHeatIndex(sourceCore.getAreaHeatingIndex());
        float tempHeatLoadIndex = heatSourceArithmetic.forecastTempHeatLoadIndex(dto);
        if (Double.isNaN(tempHeatLoadIndex)) {
            throw new RuntimeException("forecastTempHeatLoadIndex计算异常---NaN");
        }

        ForecastChangeHeatLoadRate dtoRate = new ForecastChangeHeatLoadRate();
        dtoRate.setDesignRealCountTemp(sourceCore.getInsideTemp());
        dtoRate.setDesignOutCountTemp(forecastSourceBasic.getOutsideConfigTemp());
        dtoRate.setDesignForecastTemp(forecastHourAvgTemperature);
        float heatLoadRate = heatSourceArithmetic.forecastChangeHeatLoadRate(dtoRate);
        if (Double.isNaN(heatLoadRate)) {
            throw new RuntimeException("forecastChangeHeatLoadRate计算异常---NaN");
        }

        ForecastHeatLoadIndex dtoIndex = new ForecastHeatLoadIndex();
        dtoIndex.setHeatLoadIndex(new BigDecimal(Float.toString(tempHeatLoadIndex)));
        dtoIndex.setHeatLoadRate(new BigDecimal(Float.toString(heatLoadRate)));
        float heatLoadIndex = heatSourceArithmetic.forecastHeatLoadIndex(dtoIndex);
        if (Double.isNaN(heatLoadIndex)) {
            throw new RuntimeException("forecastHeatLoadIndex计算异常---NaN");
        }

        ForecastCoefficientHeatLoadIndex dtoCoefficient = new ForecastCoefficientHeatLoadIndex();
        // 调整系数默认值为1   调整系数怎么更改
        dtoCoefficient.setCoefficient(new BigDecimal(1));
        dtoCoefficient.setHeatLoadIndex(new BigDecimal(Float.toString(heatLoadIndex)));

        float coefficientHeat = heatSourceArithmetic.forecastCoefficientHeatLoadIndex(dtoCoefficient);
        if (Double.isNaN(coefficientHeat)) {
            throw new RuntimeException("forecastCoefficientHeatLoadIndex计算异常---NaN");
        }
        return new BigDecimal(Float.toString(coefficientHeat));
    }


    /**
     * 预测热负荷指标   天 和 阶段的
     */
    private BigDecimal heatLoad(ForecastSourceBasic forecastSourceBasic, ForecastSourceCore sourceCore) {

        ForecastTempHeatLoadIndex dto = new ForecastTempHeatLoadIndex();
        dto.setDesignCountTemp(forecastSourceBasic.getInsideDesignTemp());
        dto.setDesignOutCountTemp(forecastSourceBasic.getOutsideConfigTemp());
        dto.setDesignRealCountTemp(sourceCore.getInsideTemp());
        dto.setAreaHeatIndex(sourceCore.getAreaHeatingIndex());
        float tempHeatLoadIndex = heatSourceArithmetic.forecastTempHeatLoadIndex(dto);
        if (Double.isNaN(tempHeatLoadIndex)) {
            throw new RuntimeException("forecastTempHeatLoadIndex计算异常---NaN");
        }
        return new BigDecimal(Float.toString(tempHeatLoadIndex));
    }


    /**
     * 预测用热量   天 和    阶段的
     */
    private BigDecimal useHeat(BigDecimal heatLoad, ForecastSourceCore sourceCore, Integer interval, BigDecimal areaMultiple) {

        ForecastWithHeat dto = new ForecastWithHeat();
        dto.setRealCountHeatLoadIndex(heatLoad);
        dto.setArea(sourceCore.getHeatArea().multiply(areaMultiple));
        dto.setHrNumber(interval);
        float heatQuantity = heatSourceArithmetic.forecastWithHeat(dto);
        if (Double.isNaN(heatQuantity)) {
            throw new RuntimeException("forecastWithHeat计算异常---NaN");
        }
        return new BigDecimal(Float.toString(heatQuantity));
    }

    /**
     * 预测  小时 用热量
     */
    private BigDecimal hourUseHeat(BigDecimal heatLoad, ForecastSourceCore sourceCore, BigDecimal areaMultiple) {
        ForecastWithHeatHour dto = new ForecastWithHeatHour();
        dto.setRealCountHeatLoadIndex(heatLoad);
        dto.setArea(sourceCore.getHeatArea().multiply(areaMultiple));
        float heatQuantity = heatSourceArithmetic.forecastWithHeatHour(dto);
        if (Double.isNaN(heatQuantity)) {
            throw new RuntimeException("forecastWithHeatHour计算异常---NaN");
        }
        return new BigDecimal(Float.toString(heatQuantity));
    }

}
