package com.bmts.heating.web.forecast.service.test;

import com.bmts.heating.commons.basement.model.db.entity.ForecastSourceHistory;
import com.bmts.heating.commons.entiy.forecast.algorithm.*;
import com.bmts.heating.commons.entiy.forecast.response.ForecastTempResponse;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.compute.forecast.heatsource.HeatSourceArithmetic;
import com.bmts.heating.web.forecast.service.test.dto.StandardConditionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description 标准工况 计算方式
 */
@Service
public class StandardConditionServiceTest {

    @Autowired
    private HeatSourceArithmetic heatSourceArithmetic;


    /**
     * 预测一次供回水温度
     */
    public Response forecastTgTh(StandardConditionDto testDto) {

        LinkedHashMap<String, List<String>> mapMsg = new LinkedHashMap<>();
        // 预测热负荷率
        BigDecimal loadRate = loadRate(testDto.getBasic_insideDesignTemp(), testDto.getBasic_outsideConfigTemp(), testDto.getForecastHourAvgTemperature(), mapMsg);
        // 预测二次供回温度
        Map<String, BigDecimal> t2Map = t2gT2h(loadRate, testDto, mapMsg);
        // 预测系数
        BigDecimal coefficient = coefficient(testDto, mapMsg);
        //  预测一次供回水温度
        Map<String, BigDecimal> tMap = tgTh(loadRate, t2Map, coefficient, testDto, mapMsg);

        ForecastSourceHistory forecastSourceHistory = new ForecastSourceHistory();
        forecastSourceHistory.setForecastLoadRate(loadRate);
        forecastSourceHistory.setForecastT1g(tMap.get("Tg").setScale(2, BigDecimal.ROUND_HALF_UP));
        forecastSourceHistory.setForecastT1h(tMap.get("Th").setScale(2, BigDecimal.ROUND_HALF_UP));
        forecastSourceHistory.setRadiatorT2g(t2Map.get("T2g").setScale(2, BigDecimal.ROUND_HALF_UP));
        forecastSourceHistory.setRadiatorT2h(t2Map.get("T2h").setScale(2, BigDecimal.ROUND_HALF_UP));
        // 室内计算温度
        forecastSourceHistory.setIndoorTemp(testDto.getCore_insideTemp());
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
     * @return java.math.BigDecimal
     * @Method loadRate
     * @Param [forecastSourceBasic, forecastHourAvgTemperature]
     * @Description 预测热负荷率
     */
    private BigDecimal loadRate(BigDecimal basic_insideDesignTemp, BigDecimal basic_outsideConfigTemp, BigDecimal forecastHourAvgTemperature, LinkedHashMap<String, List<String>> mapMsg) {

        List<String> msgList = new ArrayList<>();
        msgList.add("热负荷率所需要的参数：室外设计温度=" + basic_outsideConfigTemp + " ,室内设计温度=" + basic_insideDesignTemp + " ,天气预报室外温度=" + forecastHourAvgTemperature);
        ForecastHeatLoadRate dto = new ForecastHeatLoadRate();
        dto.setDesignCountTemp(basic_insideDesignTemp);
        dto.setDesignOutCountTemp(basic_outsideConfigTemp);
        dto.setDesignForecastTemp(forecastHourAvgTemperature);
        float loadRate = heatSourceArithmetic.forecastHeatLoadRate(dto);
        if (Double.isNaN(loadRate)) {
            throw new RuntimeException("forecastHeatLoadRate计算异常---NaN");
        }

        msgList.add("热负荷率的计算公式：(室内设计温度 - 天气预报室外温度) / (室内设计温度 - 室外设计温度)");
        msgList.add("热负荷率的计算结果：" + loadRate);
        mapMsg.put("标准工况预测热负荷率", msgList);

        return new BigDecimal(Float.toString(loadRate));
    }


    /**
     * @return java.util.Map<java.lang.String, java.math.BigDecimal>
     * @Method t2gT2h
     * @Param [forecastSourceBasic, sourceCore, heatSeason,loadRate]
     * @Description 预测二次供回温度
     */
    private Map<String, BigDecimal> t2gT2h(BigDecimal loadRate, StandardConditionDto testDto, LinkedHashMap<String, List<String>> mapMsg) {

        List<String> msgList = new ArrayList<>();
        msgList.add("二次供回温度所需参数：室内设计温度=" + testDto.getBasic_insideDesignTemp() + " , \n 散热器严寒季供水温度=" + testDto.getCore_radiatorTg()
                + " , \n 散热器严寒季回水温度=" + testDto.getCore_radiatorTh()
                + " , \n 室内实际计算温度tn=" + testDto.getCore_insideTemp()
                + " , \n 预测的热负荷率=" + loadRate
                + " , \n 二次管网相对流量=" + testDto.getSeason_secondRelativeFlow());

        ForecastRadiatorSTemp dto = new ForecastRadiatorSTemp();
        dto.setDesignCountTemp(testDto.getBasic_insideDesignTemp());
        dto.setDesignTempG(testDto.getCore_radiatorTg());
        dto.setDesignTempH(testDto.getCore_radiatorTh());
        dto.setRealCountTemp(testDto.getCore_insideTemp());
        dto.setLoadRateStandardConditions(loadRate);
        dto.setRelativeDischargeS(testDto.getSeason_secondRelativeFlow());
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
        msgList.add("计算结果：T2g=" + new BigDecimal(Float.toString(t2g)).setScale(2, BigDecimal.ROUND_HALF_UP));
        msgList.add("计算结果：T2h=" + new BigDecimal(Float.toString(t2h)).setScale(2, BigDecimal.ROUND_HALF_UP));
        mapMsg.put("标准工况预测二次供回温度", msgList);

        return map;
    }


    /**
     * @return java.math.BigDecimal
     * @Method coefficient
     * @Param [sourceCore, heatSeason]
     * @Description 预测系数
     */
    private BigDecimal coefficient(StandardConditionDto testDto, LinkedHashMap<String, List<String>> mapMsg) {

        List<String> msgList = new ArrayList<>();
        msgList.add("预测系数所需参数：散热器严寒季供水温度=" + testDto.getCore_radiatorTg() + " ,\n 散热器严寒季回水温度=" + testDto.getCore_radiatorTh()
                + " , \n 一次网严寒季供水温度=" + testDto.getCore_firstNetTg() + " , \n 一次网严寒季回水温度=" + testDto.getCore_firstNetTh()
                + " , \n 一次管网相对流量=" + testDto.getSeason_firstNetRelativeFlow() + " , \n 二次网相对流量=" + testDto.getSeason_secondRelativeFlow());


        ForecastCoefficient dto = new ForecastCoefficient();
        dto.setDesignSTempG(testDto.getCore_radiatorTg());
        dto.setDesignSTempH(testDto.getCore_radiatorTh());
        dto.setDesignFTempG(testDto.getCore_firstNetTg());
        dto.setDesignFTempH(testDto.getCore_firstNetTh());
        dto.setRelativeDischargeF(testDto.getSeason_firstNetRelativeFlow());
        dto.setRelativeDischargeS(testDto.getSeason_secondRelativeFlow());
        float coefficient = heatSourceArithmetic.forecastCoefficient(dto);
        if (Double.isNaN(coefficient)) {
            throw new RuntimeException("forecastCoefficient计算异常---NaN");
        }
        msgList.add("系数计算结果：coefficient=" + coefficient);
        mapMsg.put("标准工况预测系数", msgList);

        return new BigDecimal(Float.toString(coefficient));
    }

    /**
     * @return java.util.Map<java.lang.String, java.math.BigDecimal>
     * @Method tgTh
     * @Param [sourceCore, heatSeason, loadRate, t2Map, coefficient]
     * @Description 预测一次供回水温度
     */
    private Map<String, BigDecimal> tgTh(BigDecimal loadRate, Map<String, BigDecimal> t2Map, BigDecimal coefficient, StandardConditionDto testDto, LinkedHashMap<String, List<String>> mapMsg) {

        List<String> msgList = new ArrayList<>();
        msgList.add("预测一次供回水温度所需参数：热负荷率= " + loadRate + " , \n 一次管网相对流量= " + testDto.getSeason_firstNetRelativeFlow()
                + " , \n 一次网严寒季供水温度= " + testDto.getCore_firstNetTg() + " , \n 一次网严寒季回水温度= " + testDto.getCore_firstNetTh()
                + " , \n 预测二次网散热器供水温度= " + t2Map.get("T2g") + " , \n 预测二次网散热器回水温度= " + t2Map.get("T2h")
                + " , \n 预测的系数= " + coefficient);


        ForecastFirstTemp dto = new ForecastFirstTemp();
        dto.setLoadRateStandardConditions(loadRate);
        dto.setDesignTempG(testDto.getCore_firstNetTg());
        dto.setDesignTempH(testDto.getCore_firstNetTh());
        dto.setRelativeDischargeF(testDto.getSeason_firstNetRelativeFlow());
        dto.setForecastSTempG(t2Map.get("T2g"));
        dto.setForecastSTempH(t2Map.get("T2h"));
        dto.setCoefficient(coefficient);
        ForecastTempResponse forecastTempResponse = heatSourceArithmetic.forecastFirstTemp(dto);
        float tg = forecastTempResponse.getTg();
        float th = forecastTempResponse.getTh();

        ///////////////////////////////////


        if (Double.isNaN(tg) || Double.isInfinite(tg)) {
            throw new RuntimeException("forecastFirstTemp计算异常---NaN");
        }
        if (Double.isNaN(th) || Double.isInfinite(th)) {
            throw new RuntimeException("forecastFirstTemp计算异常---NaN");
        }

        Map<String, BigDecimal> map = new HashMap<>();
        map.put("Tg", new BigDecimal(Float.toString(tg)));
        map.put("Th", new BigDecimal(Float.toString(th)));

        msgList.add("计算结果：Tg=" + new BigDecimal(Float.toString(tg)).setScale(2, BigDecimal.ROUND_HALF_UP));
        msgList.add("计算结果：Th=" + new BigDecimal(Float.toString(th)).setScale(2, BigDecimal.ROUND_HALF_UP));
        mapMsg.put("标准工况预测一次供回水温度", msgList);

        return map;
    }


    /**
     * 预测热负荷指标
     */
    private BigDecimal heatLoad(StandardConditionDto testDto, LinkedHashMap<String, List<String>> mapMsg) {

        List<String> msgList = new ArrayList<>();
        msgList.add("预测热负荷指标所需参数：室内设计计算温度t‘n= " + testDto.getBasic_insideDesignTemp() + " , \n 设计室外计算温度t'w= " + testDto.getBasic_outsideConfigTemp()
                + " , \n室内实际计算温度tn= " + testDto.getCore_insideTemp() + " , \n 单位面积设计热指标= " + testDto.getCore_areaHeatingIndex()
                + " , \n 采暖季预测室外温度tw= " + testDto.getForecastHourAvgTemperature());

        ForecastTempHeatLoadIndex dto = new ForecastTempHeatLoadIndex();
        dto.setDesignCountTemp(testDto.getBasic_insideDesignTemp());
        dto.setDesignOutCountTemp(testDto.getBasic_outsideConfigTemp());
        dto.setDesignRealCountTemp(testDto.getCore_insideTemp());
        dto.setAreaHeatIndex(testDto.getCore_areaHeatingIndex());
        float tempHeatLoadIndex = heatSourceArithmetic.forecastTempHeatLoadIndex(dto);
        if (Double.isNaN(tempHeatLoadIndex)) {
            throw new RuntimeException("forecastTempHeatLoadIndex计算异常---NaN");
        }

        msgList.add("计算的热负荷指标q(W/㎡)结果=" + new BigDecimal(Float.toString(tempHeatLoadIndex)).setScale(2, BigDecimal.ROUND_HALF_UP));

        ForecastChangeHeatLoadRate dtoRate = new ForecastChangeHeatLoadRate();
        dtoRate.setDesignRealCountTemp(testDto.getCore_insideTemp());
        dtoRate.setDesignOutCountTemp(testDto.getBasic_outsideConfigTemp());
        dtoRate.setDesignForecastTemp(testDto.getForecastHourAvgTemperature());
        float heatLoadRate = heatSourceArithmetic.forecastChangeHeatLoadRate(dtoRate);
        if (Double.isNaN(heatLoadRate)) {
            throw new RuntimeException("forecastChangeHeatLoadRate计算异常---NaN");
        }
        msgList.add("计算的热负荷率η´结果 =" + new BigDecimal(Float.toString(heatLoadRate)).setScale(2, BigDecimal.ROUND_HALF_UP));

        ForecastHeatLoadIndex dtoIndex = new ForecastHeatLoadIndex();
        dtoIndex.setHeatLoadIndex(new BigDecimal(Float.toString(tempHeatLoadIndex)));
        dtoIndex.setHeatLoadRate(new BigDecimal(Float.toString(heatLoadRate)));
        float heatLoadIndex = heatSourceArithmetic.forecastHeatLoadIndex(dtoIndex);
        if (Double.isNaN(heatLoadIndex)) {
            throw new RuntimeException("forecastHeatLoadIndex计算异常---NaN");
        }

        msgList.add("计算的热负荷指标q”的参数：上面计算的热指标q=" + new BigDecimal(Float.toString(tempHeatLoadIndex)).setScale(2, BigDecimal.ROUND_HALF_UP)
                + " , \n上面计算的热负荷率η´= " + new BigDecimal(Float.toString(heatLoadRate)).setScale(2, BigDecimal.ROUND_HALF_UP)
                + " , \n计算的热负荷指标q”的结果= " + new BigDecimal(Float.toString(heatLoadIndex)).setScale(2, BigDecimal.ROUND_HALF_UP));


        ForecastCoefficientHeatLoadIndex dtoCoefficient = new ForecastCoefficientHeatLoadIndex();
        // 调整系数默认值为1   调整系数怎么更改
        dtoCoefficient.setCoefficient(new BigDecimal(1));
        dtoCoefficient.setHeatLoadIndex(new BigDecimal(Float.toString(heatLoadIndex)));

        float coefficientHeat = heatSourceArithmetic.forecastCoefficientHeatLoadIndex(dtoCoefficient);
        if (Double.isNaN(coefficientHeat)) {
            throw new RuntimeException("forecastCoefficientHeatLoadIndex计算异常---NaN");
        }

        msgList.add("热负荷调整系数β下的实际计算热负荷指标q´ ：调整系数（默认值为1）=" + 1
                + " , \n上面计算的热指标q” = " + new BigDecimal(Float.toString(heatLoadIndex)).setScale(2, BigDecimal.ROUND_HALF_UP)
                + " , \n热负荷指标q´的结果= " + new BigDecimal(Float.toString(coefficientHeat)).setScale(2, BigDecimal.ROUND_HALF_UP));

        mapMsg.put("标准工况预测实际计算热负荷指标q´", msgList);

        return new BigDecimal(Float.toString(coefficientHeat));
    }


    /**
     * 预测用热量
     */
    private BigDecimal useHeat(BigDecimal heatLoad, StandardConditionDto testDto, LinkedHashMap<String, List<String>> mapMsg) {

        List<String> msgList = new ArrayList<>();
        msgList.add("预测用热量所需参数：设计室外计算温度t'w= " + testDto.getBasic_outsideConfigTemp() + " , \n上面计算的热负荷指标q´= " + heatLoad
                + " , \n室内实际计算温度tn= " + testDto.getCore_insideTemp() + " , \n 供热面积F= " + testDto.getCore_heatArea()
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
        mapMsg.put("标准工况预测用热量", msgList);
        return new BigDecimal(Float.toString(heatQuantity));
    }

}
