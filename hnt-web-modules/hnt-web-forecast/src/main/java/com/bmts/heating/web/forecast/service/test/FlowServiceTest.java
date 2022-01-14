package com.bmts.heating.web.forecast.service.test;

import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceBasic;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceCore;
import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceHistory;
import com.bmts.heating.commons.entiy.forecast.algorithm.*;
import com.bmts.heating.commons.entiy.forecast.response.ForecastTempResponse;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.compute.forecast.heatsource.HeatSourceArithmetic;
import com.bmts.heating.compute.forecast.pojo.ForecastHeatLoad;
import com.bmts.heating.web.forecast.service.test.dto.FlowDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


/**
 * @Description 流量法 计算方式
 */
@Service
public class FlowServiceTest {

    @Autowired
    private HeatSourceArithmetic heatSourceArithmetic;


    /**
     * 预测一次供回水温度
     */
    public Response forecastTgTh(FlowDto testDto) {

        LinkedHashMap<String, List<String>> mapMsg = new LinkedHashMap<>();

        // 散热器供回水温度的调整
        Map<String, BigDecimal> radiatorMap = radiatorAdjust(testDto, mapMsg);

        // 预测热负荷率
        ForecastHeatLoad loadRate = loadRate(testDto, mapMsg);

        // 预测二次供回温度
        Map<String, BigDecimal> forecastT2Map = forecastT2gT2h(radiatorMap, loadRate.getLoadRate(), testDto, mapMsg);

        // 一次网严寒季供回水温度的调整
//        Map<String, BigDecimal> adjustMap = adjustTgTh(forecastRequest.getCommonValue(), radiatorMap, forecastRequest.getForecastSourceBasic(), forecastRequest.getSourceCore());

        // 在流量法下，一次网相对流量
        BigDecimal relativeFlow = relativeFlow(loadRate.getHeatLoadIndex(), testDto, mapMsg);

        // 系数d计算
        BigDecimal coefficient = coefficient(radiatorMap, relativeFlow, testDto, mapMsg);

        // 预测一次供回水温度
        Map<String, BigDecimal> tMap = tgTh(loadRate.getLoadRate(), relativeFlow, forecastT2Map, coefficient, testDto, mapMsg);

        ForecastSourceHistory forecastSourceHistory = new ForecastSourceHistory();
        forecastSourceHistory.setForecastLoadRate(loadRate.getLoadRate().setScale(2, BigDecimal.ROUND_HALF_UP));
        forecastSourceHistory.setForecastT1g(tMap.get("Tg").setScale(2, BigDecimal.ROUND_HALF_UP));
        forecastSourceHistory.setForecastT1h(tMap.get("Th").setScale(2, BigDecimal.ROUND_HALF_UP));
        forecastSourceHistory.setRadiatorT2g(forecastT2Map.get("T2g").setScale(2, BigDecimal.ROUND_HALF_UP));
        forecastSourceHistory.setRadiatorT2h(forecastT2Map.get("T2h").setScale(2, BigDecimal.ROUND_HALF_UP));
        // 室内计算温度
        forecastSourceHistory.setIndoorTemp(testDto.getComputeTemp());
        // 预测室外温度
        forecastSourceHistory.setForecastOutTemp(testDto.getForecastHourAvgTemperature());


        // 预测热负荷指标
        BigDecimal heatLoad = heatLoad(testDto, mapMsg);

        // 预测用热量
        BigDecimal heatQuantity = useHeat(heatLoad, testDto, mapMsg);
        forecastSourceHistory.setForecastHot(heatQuantity.setScale(2, BigDecimal.ROUND_HALF_UP));
        forecastSourceHistory.setForecastThermalIndex(heatLoad.setScale(2, BigDecimal.ROUND_HALF_UP));


        Response response = new Response();
        response.setData(forecastSourceHistory);
        response.setMsg(mapMsg.toString());

        return response;

    }


    /**
     * 散热器供回水温度的调整
     */
    private Map<String, BigDecimal> radiatorAdjust(FlowDto testDto, LinkedHashMap<String, List<String>> mapMsg) {

        List<String> msgList = new ArrayList<>();
        msgList.add("计算散热器供回水温度的调整的参数：室外设计温度=" + testDto.getBasic_outsideConfigTemp() + " ,室内设计温度=" + testDto.getBasic_insideDesignTemp()
                + " ,\n散热器严寒季供水温度=" + testDto.getCore_radiatorTg() + " ,\n散热器严寒季回水温度=" + testDto.getCore_radiatorTh()
                + " ,\n室内实际计算温度tn=" + testDto.getComputeTemp());


        ForecastChangeTemp dto = new ForecastChangeTemp();
        dto.setDesignCountTemp(testDto.getBasic_insideDesignTemp());
        dto.setDesignOutCountTemp(testDto.getBasic_outsideConfigTemp());
        dto.setDesignTempG(testDto.getCore_radiatorTg());
        dto.setDesignTempH(testDto.getCore_radiatorTh());
        // 温度预测方式的属性值（字段名：tempSetting中属性tempValue）。
        dto.setRealCountTemp(testDto.getComputeTemp());

        ForecastTempResponse forecastTempResponse = heatSourceArithmetic.forecastChangeTemp(dto);
        float t2g = forecastTempResponse.getTg();
        float t2h = forecastTempResponse.getTh();
        if (Double.isNaN(t2g)) {
            throw new RuntimeException("forecastChangeTemp计算异常---NaN");
        }
        if (Double.isNaN(t2h)) {
            throw new RuntimeException("forecastChangeTemp计算异常---NaN");
        }
        Map<String, BigDecimal> radiatorMap = new HashMap<>();
        radiatorMap.put("T2g", new BigDecimal(Float.toString(t2g)));
        radiatorMap.put("T2h", new BigDecimal(Float.toString(t2h)));
        msgList.add("计算散热器供水温度结果：Tg=" + new BigDecimal(Float.toString(t2g)).setScale(2, BigDecimal.ROUND_HALF_UP));
        msgList.add("计算散热器回水温度结果：Th=" + new BigDecimal(Float.toString(t2h)).setScale(2, BigDecimal.ROUND_HALF_UP));
        mapMsg.put("流量法预测散热器供回水温度的调整", msgList);
        return radiatorMap;
    }


    /**
     * 预测热负荷率
     */
    private ForecastHeatLoad loadRate(FlowDto testDto, LinkedHashMap<String, List<String>> mapMsg) {

        List<String> msgList = new ArrayList<>();
        msgList.add("预测热负荷率的参数：室外设计温度=" + testDto.getBasic_outsideConfigTemp() + " ,室内设计温度=" + testDto.getBasic_insideDesignTemp()
                + " ,\n单位面积设计热指标=" + testDto.getCore_areaHeatingIndex() + " , \n 采暖季预测室外温度tw= " + testDto.getForecastHourAvgTemperature()
                + " ,\n室内实际计算温度tn=" + testDto.getComputeTemp());


        ForecastTempHeatLoadIndex dtoTempLoadIndex = new ForecastTempHeatLoadIndex();
        dtoTempLoadIndex.setDesignCountTemp(testDto.getBasic_insideDesignTemp());
        dtoTempLoadIndex.setDesignOutCountTemp(testDto.getBasic_outsideConfigTemp());
        dtoTempLoadIndex.setAreaHeatIndex(testDto.getCore_areaHeatingIndex());
        // 温度预测方式的属性值（字段名：tempSetting中属性tempValue）。
        dtoTempLoadIndex.setDesignRealCountTemp(testDto.getComputeTemp());
        float tempHeatLoadIndex = heatSourceArithmetic.forecastTempHeatLoadIndex(dtoTempLoadIndex);
        if (Double.isNaN(tempHeatLoadIndex)) {
            throw new RuntimeException("forecastTempHeatLoadIndex计算异常---NaN");
        }

        msgList.add("计算当室内温度由室内设计计算温度t´n变为tn下的严寒季热负荷指标q(W/㎡)的结果= " + new BigDecimal(Float.toString(tempHeatLoadIndex)).setScale(2, BigDecimal.ROUND_HALF_UP));


        ForecastChangeHeatLoadRate dtoLoadRate = new ForecastChangeHeatLoadRate();
        dtoLoadRate.setDesignForecastTemp(testDto.getForecastHourAvgTemperature());
        dtoLoadRate.setDesignOutCountTemp(testDto.getBasic_outsideConfigTemp());
        // 温度预测方式的属性值（字段名：tempSetting中属性tempValue）。
        dtoLoadRate.setDesignRealCountTemp(testDto.getComputeTemp());
        float heatLoadRate = heatSourceArithmetic.forecastChangeHeatLoadRate(dtoLoadRate);
        if (Double.isNaN(heatLoadRate)) {
            throw new RuntimeException("forecastChangeHeatLoadRate计算异常---NaN");
        }

        msgList.add("计算在室外某时刻室外温度下的热负荷率η´ 的结果= " + new BigDecimal(Float.toString(heatLoadRate)).setScale(2, BigDecimal.ROUND_HALF_UP));


        ForecastHeatLoadIndex dtoLoadIndex = new ForecastHeatLoadIndex();
        dtoLoadIndex.setHeatLoadRate(new BigDecimal(Float.toString(heatLoadRate)));
        dtoLoadIndex.setHeatLoadIndex(new BigDecimal(Float.toString(tempHeatLoadIndex)));
        float heatLoadIndex = heatSourceArithmetic.forecastHeatLoadIndex(dtoLoadIndex);
        if (Double.isNaN(heatLoadIndex)) {
            throw new RuntimeException("forecastHeatLoadIndex计算异常---NaN");
        }

        msgList.add("计算在室外某时刻室外温度下的热负荷指标q” 的结果= " + new BigDecimal(Float.toString(heatLoadIndex)).setScale(2, BigDecimal.ROUND_HALF_UP));


        ForecastCoefficientHeatLoadIndex dtoCoefficientLoadIndex = new ForecastCoefficientHeatLoadIndex();
        dtoCoefficientLoadIndex.setHeatLoadIndex(new BigDecimal(Float.toString(heatLoadIndex)));
        // 调整系数为默认值1
        dtoCoefficientLoadIndex.setCoefficient(new BigDecimal(1));
        float coefficientHeatLoadIndex = heatSourceArithmetic.forecastCoefficientHeatLoadIndex(dtoCoefficientLoadIndex);
        if (Double.isNaN(coefficientHeatLoadIndex)) {
            throw new RuntimeException("forecastCoefficientHeatLoadIndex计算异常---NaN");
        }
        msgList.add("计算热负荷调整系数β下的实际计算热负荷指标q´ 的结果= " + new BigDecimal(Float.toString(coefficientHeatLoadIndex)).setScale(2, BigDecimal.ROUND_HALF_UP));

        ForecastRealHeatLoadRate dtoRealLoadRate = new ForecastRealHeatLoadRate();
        dtoRealLoadRate.setCoefficientHeatLoadIndex(new BigDecimal(Float.toString(coefficientHeatLoadIndex)));
        dtoRealLoadRate.setHeatLoadIndex(new BigDecimal(Float.toString(tempHeatLoadIndex)));
        float realLoadRate = heatSourceArithmetic.forecastRealHeatLoadRate(dtoRealLoadRate);
        if (Double.isNaN(realLoadRate)) {
            throw new RuntimeException("forecastRealHeatLoadRate计算异常---NaN");
        }
        msgList.add("实际计算热负荷率η 的结果= " + new BigDecimal(Float.toString(realLoadRate)).setScale(2, BigDecimal.ROUND_HALF_UP));
        mapMsg.put("流量法预测热负荷率η", msgList);

        ForecastHeatLoad heatLoad = new ForecastHeatLoad();
        heatLoad.setHeatLoadIndex(new BigDecimal(Float.toString(heatLoadIndex)));
        heatLoad.setLoadRate(new BigDecimal(Float.toString(realLoadRate)));
        return heatLoad;
    }


    /**
     * 预测二次供回温度
     */
    private Map<String, BigDecimal> forecastT2gT2h(Map<String, BigDecimal> radiatorMap, BigDecimal loadRate, FlowDto testDto, LinkedHashMap<String, List<String>> mapMsg) {

        List<String> msgList = new ArrayList<>();
        msgList.add("预测二次供回温度的参数： 室内实际计算温度tn=" + testDto.getComputeTemp()
                + " ,\n上面计算的热负荷率η = " + loadRate + " , \n 二次管网相对流量= " + testDto.getSeason_secondRelativeFlow()
                + " ,\n室内实际计算温度tn=" + testDto.getComputeTemp()
                + " , \n 上面计算的散热器调整供水温度= " + radiatorMap.get("T2g") + " , \n 上面计算的散热器调整回水温度= " + radiatorMap.get("T2h"));


        ForecastRadiatorSTemp dto = new ForecastRadiatorSTemp();
        dto.setRelativeDischargeS(testDto.getSeason_secondRelativeFlow());
        dto.setLoadRateStandardConditions(loadRate);
        // 注意调用算法时参数来源与标准工况有所不同，参数项designCountTemp及realCountTemp均为温度预测方式的属性值（字段名：tempSetting中属性tempValue）
        dto.setDesignCountTemp(testDto.getComputeTemp());
        dto.setRealCountTemp(testDto.getComputeTemp());

        dto.setDesignTempG(radiatorMap.get("T2g"));
        dto.setDesignTempH(radiatorMap.get("T2h"));
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

        msgList.add("计算预测二次供水温度结果：T2g=" + new BigDecimal(Float.toString(t2g)).setScale(2, BigDecimal.ROUND_HALF_UP));
        msgList.add("计算预测二次回水温度结果：T2h=" + new BigDecimal(Float.toString(t2h)).setScale(2, BigDecimal.ROUND_HALF_UP));
        mapMsg.put("流量法预测二次供回温度", msgList);

        return forecastMap;
    }


    /**
     * 一次网严寒季供回水温度的调整
     */
    private Map<String, BigDecimal> adjustTgTh(BigDecimal realCountTemp, Map<String, BigDecimal> radiatorMap, ForecastSourceBasic forecastSourceBasic, ForecastSourceCore sourceCore) {

        ForecastChangeFirstTemp dto = new ForecastChangeFirstTemp();
        dto.setDesignOutCountTemp(forecastSourceBasic.getOutsideConfigTemp());
        dto.setDesignCountTemp(forecastSourceBasic.getInsideDesignTemp());
        dto.setDesignFirstTempG(sourceCore.getFirstNetTg());
        dto.setDesignFirstTempH(sourceCore.getFirstNetTh());
        dto.setDesignSecondTempG(sourceCore.getRadiatorTg());
        dto.setDesignSecondTempH(sourceCore.getRadiatorTh());
        dto.setDesignSecondTempGChange(radiatorMap.get("T2g"));
        dto.setDesignSecondTempHChange(radiatorMap.get("T2h"));
        // 温度预测方式的属性值（字段名：tempSetting中属性tempValue）。
        dto.setRealCountTemp(realCountTemp);

        ForecastTempResponse forecastTempResponse = heatSourceArithmetic.forecastChangeFirstTemp(dto);
        float tg = forecastTempResponse.getTg();
        float th = forecastTempResponse.getTh();
        if (Double.isNaN(tg)) {
            throw new RuntimeException("forecastChangeFirstTemp计算异常---NaN");
        }
        if (Double.isNaN(th)) {
            throw new RuntimeException("forecastChangeFirstTemp计算异常---NaN");
        }
        Map<String, BigDecimal> adjustMap = new HashMap<>();
        adjustMap.put("Tg", new BigDecimal(Float.toString(tg)));
        adjustMap.put("Th", new BigDecimal(Float.toString(th)));
        return adjustMap;
    }

    /**
     * 在流量法下，一次网相对流量
     */
    private BigDecimal relativeFlow(BigDecimal heatLoadIndex, FlowDto testDto, LinkedHashMap<String, List<String>> mapMsg) {

        List<String> msgList = new ArrayList<>();
        msgList.add("在流量法下预测一次网相对流量的参数：上面计算的热负荷指标q” =" + heatLoadIndex + " ,面积 =" + testDto.getCore_heatArea()
                + " ,\n一次网设计供水温度=" + testDto.getCore_firstNetTg() + " , \n一次网设计回水温度= " + testDto.getCore_firstNetTh()
                + " ,\n一次网阶段流量 = " + testDto.getSeason_firstNetStageFlow());


        ForecastMaxFlow dto = new ForecastMaxFlow();
        dto.setArea(testDto.getCore_heatArea());
        dto.setDesignTempG(testDto.getCore_firstNetTg());
        dto.setDesignTempH(testDto.getCore_firstNetTh());
        dto.setHeatLoadIndex(heatLoadIndex);
        float maxFlow = heatSourceArithmetic.forecastMaxFlow(dto);
        if (Double.isNaN(maxFlow)) {
            throw new RuntimeException("forecastMaxFlow计算异常---NaN");
        }
        msgList.add("计算最大流量的调整: Grw= " + new BigDecimal(Float.toString(maxFlow)).setScale(2, BigDecimal.ROUND_HALF_UP));

        ForecastRelativeFlowRate dtoRelativeFlow = new ForecastRelativeFlowRate();
        dtoRelativeFlow.setMaxFlow(new BigDecimal(Float.toString(maxFlow)));
        dtoRelativeFlow.setNetStageFlow(testDto.getSeason_firstNetStageFlow());
        float relativeFlow = heatSourceArithmetic.forecastRelativeFlowRate(dtoRelativeFlow);
        if (Double.isNaN(relativeFlow)) {
            throw new RuntimeException("forecastRelativeFlowRate计算异常---NaN");
        }
        msgList.add("计算一次网的相对流量(当室内温度由室内设计计算温度t´n变为tn下的流量的调整) = " + new BigDecimal(Float.toString(relativeFlow)).setScale(2, BigDecimal.ROUND_HALF_UP));
        mapMsg.put("流量法计算一次网相对流量", msgList);
        return new BigDecimal(Float.toString(relativeFlow));
    }


    /**
     * 系数d计算
     */
    private BigDecimal coefficient(Map<String, BigDecimal> radiatorMap, BigDecimal relativeFlow, FlowDto testDto, LinkedHashMap<String, List<String>> mapMsg) {

        List<String> msgList = new ArrayList<>();
        msgList.add("系数d计算的参数：上面计算的一次管网相对流量=" + relativeFlow + " ,二次管网相对流量=" + testDto.getSeason_secondRelativeFlow()
                + " ,\n 上面计算的散热器供水温度= " + radiatorMap.get("T2g") + " , \n 上面计算的散热器回水温度= " + radiatorMap.get("T2h")
                + " ,\n 一次网严寒季供水温度= " + testDto.getCore_firstNetTg() + " , \n 一次网严寒季回水温度= " + testDto.getCore_firstNetTh());


        ForecastCoefficient dto = new ForecastCoefficient();
        dto.setDesignSTempG(radiatorMap.get("T2g"));
        dto.setDesignSTempH(radiatorMap.get("T2h"));
        dto.setDesignFTempG(testDto.getCore_firstNetTg());
        dto.setDesignFTempH(testDto.getCore_firstNetTh());
        dto.setRelativeDischargeF(relativeFlow);
        dto.setRelativeDischargeS(testDto.getSeason_secondRelativeFlow());
        float coefficient = heatSourceArithmetic.forecastCoefficient(dto);
        if (Double.isNaN(coefficient)) {
            throw new RuntimeException("forecastCoefficient计算异常---NaN");
        }

        msgList.add("系数d计算结果： =" + new BigDecimal(Float.toString(coefficient)).setScale(2, BigDecimal.ROUND_HALF_UP));
        mapMsg.put("流量法系数d计算", msgList);

        return new BigDecimal(Float.toString(coefficient));
    }


    /**
     * 预测一次供回水温度
     */
    private Map<String, BigDecimal> tgTh(BigDecimal loadRate, BigDecimal relativeFlow, Map<String, BigDecimal> forecastT2Map, BigDecimal coefficient, FlowDto testDto, LinkedHashMap<String, List<String>> mapMsg) {

        List<String> msgList = new ArrayList<>();
        msgList.add("预测一次供回水温度的参数：上面计算的一次网相对流量=" + relativeFlow + " ,\n上面计算的热负荷率=" + loadRate
                + " ,\n 上面计算的二次供水温度= " + forecastT2Map.get("T2g") + " , \n 上面计算的二次回水温度= " + forecastT2Map.get("T2h")
                + " ,\n 一次网严寒季供水温度= " + testDto.getCore_firstNetTg() + " , \n 一次网严寒季回水温度= " + testDto.getCore_firstNetTh()
                + " ,\n 上面计算的系数 =  " + coefficient);


        ForecastFirstTemp dto = new ForecastFirstTemp();
        dto.setLoadRateStandardConditions(loadRate);
        dto.setDesignTempG(testDto.getCore_firstNetTg());
        dto.setDesignTempH(testDto.getCore_firstNetTh());
        dto.setRelativeDischargeF(relativeFlow);
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

        msgList.add("计算预测一次供水温度结果：Tg=" + new BigDecimal(Float.toString(tg)).setScale(2, BigDecimal.ROUND_HALF_UP));
        msgList.add("计算预测一次回水温度结果：Th=" + new BigDecimal(Float.toString(th)).setScale(2, BigDecimal.ROUND_HALF_UP));
        mapMsg.put("流量法预测一次供回水温度", msgList);

        return map;
    }


    /**
     * 预测热负荷指标
     */
    private BigDecimal heatLoad(FlowDto testDto, LinkedHashMap<String, List<String>> mapMsg) {

        List<String> msgList = new ArrayList<>();
        msgList.add("预测热负荷指标的参数：室外设计温度=" + testDto.getBasic_outsideConfigTemp() + " ,室内设计温度=" + testDto.getBasic_insideDesignTemp()
                + " ,\n单位面积设计热指标= " + testDto.getCore_areaHeatingIndex()
                + " ,\n室内实际计算温度tn=" + testDto.getComputeTemp()
                + " , \n 采暖季预测室外温度tw= " + testDto.getForecastHourAvgTemperature());


        ForecastTempHeatLoadIndex dto = new ForecastTempHeatLoadIndex();
        dto.setDesignCountTemp(testDto.getBasic_insideDesignTemp());
        dto.setDesignOutCountTemp(testDto.getBasic_outsideConfigTemp());
        dto.setAreaHeatIndex(testDto.getCore_areaHeatingIndex());
        // 温度预测方式的属性值（字段名：tempSetting中属性tempValue）
        dto.setDesignRealCountTemp(testDto.getComputeTemp());
        float tempHeatLoadIndex = heatSourceArithmetic.forecastTempHeatLoadIndex(dto);
        if (Double.isNaN(tempHeatLoadIndex)) {
            throw new RuntimeException("forecastTempHeatLoadIndex计算异常---NaN");
        }

        msgList.add("计算当室内温度由室内设计计算温度t´n变为tn下的严寒季热负荷指标q(W/㎡)结果 =" + new BigDecimal(Float.toString(tempHeatLoadIndex)).setScale(2, BigDecimal.ROUND_HALF_UP));


        ForecastChangeHeatLoadRate dtoRate = new ForecastChangeHeatLoadRate();
        dtoRate.setDesignOutCountTemp(testDto.getBasic_outsideConfigTemp());
        dtoRate.setDesignForecastTemp(testDto.getForecastHourAvgTemperature());
        // 温度预测方式的属性值（字段名：tempSetting中属性tempValue）
        dtoRate.setDesignRealCountTemp(testDto.getComputeTemp());
        float heatLoadRate = heatSourceArithmetic.forecastChangeHeatLoadRate(dtoRate);
        if (Double.isNaN(heatLoadRate)) {
            throw new RuntimeException("forecastChangeHeatLoadRate计算异常---NaN");
        }

        msgList.add("计算在室外某时刻室外温度下的热负荷率η´ 结果 =" + new BigDecimal(Float.toString(heatLoadRate)).setScale(2, BigDecimal.ROUND_HALF_UP));


        ForecastHeatLoadIndex dtoIndex = new ForecastHeatLoadIndex();
        dtoIndex.setHeatLoadIndex(new BigDecimal(Float.toString(tempHeatLoadIndex)));
        dtoIndex.setHeatLoadRate(new BigDecimal(Float.toString(heatLoadRate)));
        float heatLoadIndex = heatSourceArithmetic.forecastHeatLoadIndex(dtoIndex);
        if (Double.isNaN(heatLoadIndex)) {
            throw new RuntimeException("forecastHeatLoadIndex计算异常---NaN");
        }

        msgList.add("计算在室外某时刻室外温度下的热负荷指标q” 结果 =" + new BigDecimal(Float.toString(heatLoadIndex)).setScale(2, BigDecimal.ROUND_HALF_UP));


        ForecastCoefficientHeatLoadIndex dtoCoefficient = new ForecastCoefficientHeatLoadIndex();
        // 调整系数默认值为1   调整系数怎么更改
        dtoCoefficient.setCoefficient(new BigDecimal(1));
        dtoCoefficient.setHeatLoadIndex(new BigDecimal(Float.toString(heatLoadIndex)));

        float coefficientHeat = heatSourceArithmetic.forecastCoefficientHeatLoadIndex(dtoCoefficient);
        if (Double.isNaN(coefficientHeat)) {
            throw new RuntimeException("forecastCoefficientHeatLoadIndex计算异常---NaN");
        }

        msgList.add("计算在室外某时刻室外温度下，在热负荷调整系数β下的实际计算热负荷指标q´ 结果 =" + new BigDecimal(Float.toString(coefficientHeat)).setScale(2, BigDecimal.ROUND_HALF_UP));
        mapMsg.put("流量法预测热负荷指标", msgList);

        return new BigDecimal(Float.toString(coefficientHeat));
    }

    /**
     * 预测用热量
     */
    private BigDecimal useHeat(BigDecimal heatLoad, FlowDto testDto, LinkedHashMap<String, List<String>> mapMsg) {

        List<String> msgList = new ArrayList<>();
        msgList.add("预测用热量所需参数：设计室外计算温度t'w= " + testDto.getBasic_outsideConfigTemp() + " , \n上面计算的热负荷指标q´= " + heatLoad
                + " ,\n室内实际计算温度tn=" + testDto.getComputeTemp() + " , \n 供热面积F= " + testDto.getCore_heatArea()
                + " , \n 采暖季预测室外温度tw= " + testDto.getForecastHourAvgTemperature() + " , \n 时间间隔小时= " + testDto.getInterval());


        ForecastWithHeat dto = new ForecastWithHeat();
        dto.setRealCountHeatLoadIndex(heatLoad);
        dto.setArea(testDto.getCore_heatArea());
        dto.setHrNumber(testDto.getInterval());
        float heatQuantity = heatSourceArithmetic.forecastWithHeat(dto);
        if (Double.isNaN(heatQuantity)) {
            throw new RuntimeException("forecastWithHeat计算异常---NaN");
        }
        msgList.add("计算结果：用热量=" + new BigDecimal(Float.toString(heatQuantity)).setScale(2, BigDecimal.ROUND_HALF_UP));
        mapMsg.put("流量法预测用热量", msgList);
        return new BigDecimal(Float.toString(heatQuantity));

    }

}
