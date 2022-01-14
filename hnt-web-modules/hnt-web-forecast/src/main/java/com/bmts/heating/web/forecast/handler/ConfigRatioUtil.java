package com.bmts.heating.web.forecast.handler;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bmts.heating.commons.basement.model.db.entity.ForecastWeatherCoef;
import com.bmts.heating.commons.basement.model.db.entity.WeatherDay;
import com.bmts.heating.commons.basement.model.db.entity.WeatherHour;
import com.bmts.heating.commons.basement.model.db.request.Weather;
import com.bmts.heating.commons.basement.model.db.request.WindDirection;
import com.bmts.heating.commons.basement.model.db.request.WindPower;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: ConfigRatioUtil
 * @Description: 配置系数工具类
 * @Author: pxf
 * @Date: 2021/9/24 10:52
 * @Version: 1.0
 */
public class ConfigRatioUtil {


    public static BigDecimal setDayRatio(BigDecimal forecastHot, WeatherDay weatherDay, ForecastWeatherCoef forecastWeatherCoef) {
        if (forecastWeatherCoef == null) {
            return forecastHot;
        }
        String power = null;
        if (StringUtils.isNotBlank(weatherDay.getPower())) {
            power = setWindStr(weatherDay.getPower());
        }
        // 进行业务处理
        return getBigDecimal(forecastHot, forecastWeatherCoef, power, weatherDay.getDirect(), weatherDay.getInfo());
    }

    public static BigDecimal setHourRatio(BigDecimal forecastHot, WeatherHour weatherHour, ForecastWeatherCoef forecastWeatherCoef) {
        if (forecastWeatherCoef == null) {
            return forecastHot;
        }

        String power = null;
        if (StringUtils.isNotBlank(weatherHour.getPower())) {
            power = getHourWindStr(weatherHour.getPower());
            if (StringUtils.isBlank(power)) {
                power = setWindStr(weatherHour.getPower());
            }
        }

        return getBigDecimal(forecastHot, forecastWeatherCoef, power, weatherHour.getDirect(), weatherHour.getInfo());
    }

    private static BigDecimal getBigDecimal(BigDecimal forecastHot, ForecastWeatherCoef forecastWeatherCoef, String power, String direct, String info) {
        // 进行业务处理
        Gson gson = new Gson();
        // 风向
        String windDirection = forecastWeatherCoef.getWindDirection();
        List<WindDirection> windDirectionList = gson.fromJson(windDirection, new TypeToken<List<WindDirection>>() {
        }.getType());
        Map<String, BigDecimal> windDirectionMap = windDirectionList.stream().collect(Collectors.toMap(WindDirection::getWindDirection, e -> new BigDecimal(e.getWindDirectionCoefficient()), (key1, key2) -> key2));
        BigDecimal windDirectionRatio = windDirectionMap.get(direct);
        // 风力
        String windPower = forecastWeatherCoef.getWindPower();
        List<WindPower> windPowerList = gson.fromJson(windPower, new TypeToken<List<WindPower>>() {
        }.getType());
        Map<String, BigDecimal> windPowerMap = windPowerList.stream().collect(Collectors.toMap(WindPower::getWindPower, e -> new BigDecimal(e.getWindPowerCoefficient()), (key1, key2) -> key2));
        BigDecimal windPowerRatio = windPowerMap.get(power);
        // 天气情况
        String weather = forecastWeatherCoef.getWeather();
        List<Weather> weatherList = gson.fromJson(weather, new TypeToken<List<Weather>>() {
        }.getType());
        Map<String, BigDecimal> weatherMap = weatherList.stream().collect(Collectors.toMap(Weather::getWeather, e -> new BigDecimal(e.getWeatherCoefficient()), (key1, key2) -> key2));
        BigDecimal weatherRatio = weatherMap.get(info);

        // 进行计算
        if (windDirectionRatio != null && windDirectionRatio.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal multiply = forecastHot.multiply(windDirectionRatio);
            forecastHot = forecastHot.add(multiply);
        }
        if (windPowerRatio != null && windPowerRatio.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal multiply2 = forecastHot.multiply(windPowerRatio);
            forecastHot = forecastHot.add(multiply2);
        }
        if (weatherRatio != null && weatherRatio.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal multiply3 = forecastHot.multiply(weatherRatio);
            forecastHot = forecastHot.add(multiply3);
        }
        return forecastHot;
    }

    public static BigDecimal setStageRatio(BigDecimal forecastHot, List<WeatherHour> weatherHourList, ForecastWeatherCoef forecastWeatherCoef) {
        return forecastHot;
    }


    private static String getHourWindStr(String windStr) {
        switch (windStr) {
            case "无风":
                return "0级";
            case "软风":
                return "1级";
            case "轻风":
                return "2级";
            case "微风":
                return "3级";
            case "和风":
                return "4级";
            case "劲风":
                return "5级";
            case "强风":
                return "6级";
            case "疾风":
                return "7级";
            case "大风":
                return "8级";
            case "烈风":
                return "9级";
            case "狂风":
                return "10级";
            case "暴风":
                return "11级";
            case "台风":
                return "12级";
            default:
                return null;
        }
    }

    private static String setWindStr(String windStr) {
        String replaceAll = windStr.replaceAll("[^0-9]", ",");
        List<Integer> list = new ArrayList<Integer>();
        for (String str : replaceAll.split(",")) {
            if (StringUtils.isNotBlank(str))
                list.add(Integer.parseInt(str));
        }
        if (!CollectionUtils.isEmpty(list)) {
            double asDouble = list.stream().mapToDouble(e -> e).average().getAsDouble();
            return Math.round(asDouble) + "级";
        }
        return null;
    }

}
