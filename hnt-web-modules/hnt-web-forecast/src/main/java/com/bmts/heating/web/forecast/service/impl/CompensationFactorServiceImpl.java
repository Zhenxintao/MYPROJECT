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
 * @Description 补偿系数法 计算方式
 */
@Service
public class CompensationFactorServiceImpl implements ForecastComputeService {

    @Autowired
    private HeatSourceArithmetic heatSourceArithmetic;

    /**
     * 预测一次供回水温度
     */
    @Override
    public ForecastSourceHistory forecastTgTh(ForecastRequest forecastRequest) {

        // 预测热负荷率
        BigDecimal loadRate = loadRate(forecastRequest.getCommonValue(), forecastRequest.getForecastSourceBasic(), forecastRequest.getSourceCore(), forecastRequest.getForecastHourAvgTemperature());

        // 预测二次供回温度
        Map<String, BigDecimal> forecastT2Map = forecastT2gT2h(forecastRequest.getForecastSourceBasic(), forecastRequest.getSourceCore(), forecastRequest.getHeatSeason(), loadRate);

        // 系数d计算
        BigDecimal coefficient = coefficient(forecastRequest.getSourceCore(), forecastRequest.getHeatSeason());

        // 预测一次供回水温度
        Map<String, BigDecimal> tMap = tgTh(forecastRequest.getSourceCore(), forecastRequest.getHeatSeason(), loadRate, forecastT2Map, coefficient);

        ForecastSourceHistory forecastSourceHistory = new ForecastSourceHistory();
        forecastSourceHistory.setForecastLoadRate(loadRate.setScale(2, BigDecimal.ROUND_HALF_UP));
        forecastSourceHistory.setForecastT1g(tMap.get("Tg").setScale(2, BigDecimal.ROUND_HALF_UP));
        forecastSourceHistory.setForecastT1h(tMap.get("Th").setScale(2, BigDecimal.ROUND_HALF_UP));
        forecastSourceHistory.setRadiatorT2g(forecastT2Map.get("T2g").setScale(2, BigDecimal.ROUND_HALF_UP));
        forecastSourceHistory.setRadiatorT2h(forecastT2Map.get("T2h").setScale(2, BigDecimal.ROUND_HALF_UP));
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
        BigDecimal heatLoad = heatLoadHour(forecastRequest.getCommonValue(), forecastRequest.getForecastSourceBasic(), forecastRequest.getSourceCore(), forecastRequest.getForecastHourAvgTemperature());

        if (forecastRequest.getForecastType() == ForecastType.HOUR.value()) {
            // 预测热负荷指标
            // heatLoad = heatLoadHour(forecastRequest.getCommonValue(), forecastRequest.getForecastSourceBasic(), forecastRequest.getSourceCore(), forecastRequest.getForecastHourAvgTemperature());

            heatQuantity = hourUseHeat(heatLoad, forecastRequest.getSourceCore(), forecastRequest.getAreaMultiple());
        } else {
            // 预测热负荷指标
            // heatLoad = heatLoad(forecastRequest.getForecastSourceBasic(), forecastRequest.getSourceCore());

            // 预测用热量
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
     * 预测热负荷率
     */
    private BigDecimal loadRate(BigDecimal compensationValue, ForecastSourceBasic forecastSourceBasic, ForecastSourceCore sourceCore, BigDecimal forecastHourAvgTemperature) {


        ForecastTempHeatLoadIndex dtoTempLoadIndex = new ForecastTempHeatLoadIndex();
        dtoTempLoadIndex.setDesignCountTemp(forecastSourceBasic.getInsideDesignTemp());
        dtoTempLoadIndex.setDesignOutCountTemp(forecastSourceBasic.getOutsideConfigTemp());
        dtoTempLoadIndex.setDesignRealCountTemp(sourceCore.getInsideTemp());
        dtoTempLoadIndex.setAreaHeatIndex(sourceCore.getAreaHeatingIndex());
        float tempHeatLoadIndex = heatSourceArithmetic.forecastTempHeatLoadIndex(dtoTempLoadIndex);
        if (Double.isNaN(tempHeatLoadIndex)) {
            throw new RuntimeException("forecastTempHeatLoadIndex计算异常---NaN");
        }

        ForecastChangeHeatLoadRate dtoLoadRate = new ForecastChangeHeatLoadRate();
        dtoLoadRate.setDesignForecastTemp(forecastHourAvgTemperature);
        dtoLoadRate.setDesignOutCountTemp(forecastSourceBasic.getOutsideConfigTemp());
        dtoLoadRate.setDesignRealCountTemp(sourceCore.getInsideTemp());
        float heatLoadRate = heatSourceArithmetic.forecastChangeHeatLoadRate(dtoLoadRate);
        if (Double.isNaN(heatLoadRate)) {
            throw new RuntimeException("forecastChangeHeatLoadRate计算异常---NaN");
        }

        ForecastHeatLoadIndex dtoLoadIndex = new ForecastHeatLoadIndex();
        dtoLoadIndex.setHeatLoadRate(new BigDecimal(Float.toString(heatLoadRate)));
        dtoLoadIndex.setHeatLoadIndex(new BigDecimal(Float.toString(tempHeatLoadIndex)));
        float heatLoadIndex = heatSourceArithmetic.forecastHeatLoadIndex(dtoLoadIndex);
        if (Double.isNaN(heatLoadIndex)) {
            throw new RuntimeException("forecastHeatLoadIndex计算异常---NaN");
        }

        ForecastCoefficientHeatLoadIndex dtoCoefficientLoadIndex = new ForecastCoefficientHeatLoadIndex();
        dtoCoefficientLoadIndex.setHeatLoadIndex(new BigDecimal(Float.toString(heatLoadIndex)));
        // 调整系数为补偿值系数（数据来源：c中补偿值方式属性值（字段名：compensation中属性compensationValue））
        dtoCoefficientLoadIndex.setCoefficient(compensationValue);
        float coefficientHeatLoadIndex = heatSourceArithmetic.forecastCoefficientHeatLoadIndex(dtoCoefficientLoadIndex);
        if (Double.isNaN(coefficientHeatLoadIndex)) {
            throw new RuntimeException("forecastCoefficientHeatLoadIndex计算异常---NaN");
        }

        ForecastRealHeatLoadRate dtoRealLoadRate = new ForecastRealHeatLoadRate();
        dtoRealLoadRate.setCoefficientHeatLoadIndex(new BigDecimal(Float.toString(coefficientHeatLoadIndex)));
        dtoRealLoadRate.setHeatLoadIndex(new BigDecimal(Float.toString(tempHeatLoadIndex)));
        float realLoadRate = heatSourceArithmetic.forecastRealHeatLoadRate(dtoRealLoadRate);
        if (Double.isNaN(realLoadRate)) {
            throw new RuntimeException("forecastRealHeatLoadRate计算异常---NaN");
        }
        return new BigDecimal(Float.toString(realLoadRate));
    }


    /**
     * 预测二次供回温度
     */
    private Map<String, BigDecimal> forecastT2gT2h(ForecastSourceBasic forecastSourceBasic, ForecastSourceCore sourceCore, ForecastSourceHeatSeason heatSeason, BigDecimal loadRate) {

        ForecastRadiatorSTemp dto = new ForecastRadiatorSTemp();
        dto.setRelativeDischargeS(heatSeason.getSecondRelativeFlow());
        dto.setLoadRateStandardConditions(loadRate);
        dto.setDesignCountTemp(forecastSourceBasic.getInsideDesignTemp());
        dto.setRealCountTemp(sourceCore.getInsideTemp());
        dto.setDesignTempG(sourceCore.getRadiatorTg());
        dto.setDesignTempH(sourceCore.getRadiatorTh());
        ForecastTempResponse forecastTempResponse = heatSourceArithmetic.forecastRadiatorSTemp(dto);


        float t2g = forecastTempResponse.getTg();
        float t2h = forecastTempResponse.getTh();
        if (Double.isNaN(t2g)) {
            throw new RuntimeException("forecastRadiatorSTemp计算异常---NaN");
        }
        if (Double.isNaN(t2h)) {
            throw new RuntimeException("forecastRadiatorSTemp计算异常---NaN");
        }
        Map<String, BigDecimal> forecastMap = new HashMap<>();
        forecastMap.put("T2g", new BigDecimal(Float.toString(t2g)));
        forecastMap.put("T2h", new BigDecimal(Float.toString(t2h)));
        return forecastMap;
    }

    /**
     * 系数d计算
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
     * 预测一次供回水温度
     */
    private Map<String, BigDecimal> tgTh(ForecastSourceCore sourceCore, ForecastSourceHeatSeason heatSeason, BigDecimal loadRate, Map<String, BigDecimal> forecastT2Map, BigDecimal coefficient) {

        ForecastFirstTemp dto = new ForecastFirstTemp();
        dto.setLoadRateStandardConditions(loadRate);
        dto.setDesignTempG(sourceCore.getFirstNetTg());
        dto.setDesignTempH(sourceCore.getFirstNetTh());
        dto.setRelativeDischargeF(heatSeason.getFirstNetRelativeFlow());
        dto.setForecastSTempG(forecastT2Map.get("T2g"));
        dto.setForecastSTempH(forecastT2Map.get("T2h"));
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
     * 预测热负荷指标 小时的
     */
    private BigDecimal heatLoadHour(BigDecimal compensationValue, ForecastSourceBasic forecastSourceBasic, ForecastSourceCore sourceCore, BigDecimal forecastHourAvgTemperature) {

        ForecastTempHeatLoadIndex dto = new ForecastTempHeatLoadIndex();
        dto.setDesignCountTemp(forecastSourceBasic.getInsideDesignTemp());
        dto.setDesignOutCountTemp(forecastSourceBasic.getOutsideConfigTemp());
        dto.setAreaHeatIndex(sourceCore.getAreaHeatingIndex());
        dto.setDesignRealCountTemp(sourceCore.getInsideTemp());
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
        dtoCoefficient.setHeatLoadIndex(new BigDecimal(Float.toString(heatLoadIndex)));
        // 调整系数为补偿值系数（数据来源：c中补偿值方式属性值（字段名：compensation中属性compensationValue）
        dtoCoefficient.setCoefficient(compensationValue);
        float coefficientHeat = heatSourceArithmetic.forecastCoefficientHeatLoadIndex(dtoCoefficient);
        if (Double.isNaN(coefficientHeat)) {
            throw new RuntimeException("forecastCoefficientHeatLoadIndex计算异常---NaN");
        }
        return new BigDecimal(Float.toString(coefficientHeat));
    }


    /**
     * 预测热负荷指标 天 和 阶段的
     */
    private BigDecimal heatLoad(ForecastSourceBasic forecastSourceBasic, ForecastSourceCore sourceCore) {

        ForecastTempHeatLoadIndex dto = new ForecastTempHeatLoadIndex();
        dto.setDesignCountTemp(forecastSourceBasic.getInsideDesignTemp());
        dto.setDesignOutCountTemp(forecastSourceBasic.getOutsideConfigTemp());
        dto.setAreaHeatIndex(sourceCore.getAreaHeatingIndex());
        dto.setDesignRealCountTemp(sourceCore.getInsideTemp());
        float tempHeatLoadIndex = heatSourceArithmetic.forecastTempHeatLoadIndex(dto);
        if (Double.isNaN(tempHeatLoadIndex)) {
            throw new RuntimeException("forecastTempHeatLoadIndex计算异常---NaN");
        }
        return new BigDecimal(Float.toString(tempHeatLoadIndex));
    }

    /**
     * 预测用热量
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
