package com.bmts.heating.bussiness.baseInformation.app.joggle.common;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.WeatherDay;
import com.bmts.heating.commons.basement.model.db.entity.WeatherHour;
import com.bmts.heating.commons.db.service.WeatherDayService;
import com.bmts.heating.commons.db.service.WeatherHourService;
import com.bmts.heating.commons.entiy.baseInfo.response.WeatherTempComparison;
import com.bmts.heating.commons.entiy.common.WeatherType;
import com.bmts.heating.commons.entiy.gathersearch.request.TimeRange;
import com.bmts.heating.commons.utils.es.LocalTimeUtils;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "天气预报查询")
@RestController
@RequestMapping("common/weather")
@Slf4j
public class WeatherJoggle {
    @Autowired
    private WeatherHourService weatherHourService;
    @Autowired
    private WeatherDayService weatherDayService;

    @ApiOperation("查询当前小时天气")
    @GetMapping("/hour/{num}")
    public WeatherHour queryPointByType(@PathVariable Integer num) {
        LocalDateTime hour = LocalTimeUtils.getHour(num, true);
        return weatherHourService.getOne(Wrappers.<WeatherHour>lambdaQuery().eq(WeatherHour::getWeatherTime, hour));
    }

    @ApiOperation("天气对比")
    @GetMapping("/queryTempComparison")
    public Response queryTempComparison() {
        try {
            List<WeatherHour> list = weatherHourService.list(Wrappers.<WeatherHour>lambdaQuery().eq(WeatherHour::getForecastType, WeatherType.REAL.value()).ge(WeatherHour::getWeatherTime, LocalDateTime.now().toLocalDate()).le(WeatherHour::getWeatherTime, LocalDateTime.now()));
            Double temp = list.stream().map(x -> x.getTemperature()).collect(Collectors.averagingDouble(BigDecimal::intValue));
            LocalDateTime localDateTime = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0).withHour(0).plusDays(-1);
            List<WeatherDay> weatherDays = weatherDayService.list(Wrappers.<WeatherDay>lambdaQuery().eq(WeatherDay::getForecastTime, localDateTime).or().eq(WeatherDay::getForecastTime, localDateTime.plusYears(-1)).orderByDesc(WeatherDay::getForecastTime));
            WeatherTempComparison weatherTempComparison=new WeatherTempComparison();
            weatherTempComparison.setCurrent(new BigDecimal(temp));
            if(weatherDays.size()>0) {
                weatherTempComparison.setBeforeDay(weatherDays.get(0).getAvgTemp());
            }
            if(weatherDays.size()>1) {
                weatherTempComparison.setBeforeYear(weatherDays.get(1).getAvgTemp());
            }
            return Response.success(weatherTempComparison);
        } catch (Exception e) {
            log.error("查询天气对比数据出错", e);
            return Response.fail();
        }
    }

    @ApiOperation("小时均温")
    @PostMapping("/avg")
    public Response avg(@RequestBody TimeRange timeRange) {
        LocalDateTime start = LocalTimeUtils.longToLocalDateTime(timeRange.getStart());
        LocalDateTime end = LocalTimeUtils.longToLocalDateTime(timeRange.getEnd());
        QueryWrapper<WeatherHour> wrapper = new QueryWrapper<>();
        wrapper.eq("forecastType",1)
                .ge("weatherTime", start)
                .le("weatherTime", end);
        wrapper.select("avg(temperature) as avgTemperature ");
        Map<String, Object> map = weatherHourService.getMap(wrapper);
        return Response.success(map);
    }

}
