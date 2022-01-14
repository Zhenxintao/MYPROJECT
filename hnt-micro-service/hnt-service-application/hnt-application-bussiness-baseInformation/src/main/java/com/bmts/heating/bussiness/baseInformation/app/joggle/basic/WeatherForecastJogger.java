package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.bussiness.baseInformation.app.config.WeatherForecastConfig;
import com.bmts.heating.commons.basement.model.db.entity.RealTemperature;
import com.bmts.heating.commons.basement.model.db.entity.WeatherDay;
import com.bmts.heating.commons.basement.model.db.entity.WeatherForecast;
import com.bmts.heating.commons.basement.model.db.entity.WeatherHour;
import com.bmts.heating.commons.db.service.RealTemperatureService;
import com.bmts.heating.commons.db.service.WeatherDayService;
import com.bmts.heating.commons.db.service.WeatherForecastService;
import com.bmts.heating.commons.db.service.WeatherHourService;
import com.bmts.heating.commons.entiy.baseInfo.request.QueryWeatherDto;
import com.bmts.heating.commons.entiy.baseInfo.request.WeatherForecastRegisterDto;
import com.bmts.heating.commons.entiy.common.WeatherType;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.client.spi.PreInvocationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Api(tags = "天气预报管理")
@RestController
@RequestMapping("/weather")
public class WeatherForecastJogger {

    @Autowired
    WeatherHourService weatherHourService;
    @Autowired
    WeatherDayService weatherDayService;

    @ApiOperation("查询某天天气预报")
    @GetMapping("/day/{day}")
    public WeatherDay queryWeatherDay(@PathVariable String day) {
        WeatherDay one = weatherDayService.getOne(Wrappers.<WeatherDay>lambdaQuery().eq(WeatherDay::getForecastTime, day));
        if (one == null) one = new WeatherDay();
        return one;
    }

    @ApiOperation("查询天预报")
    @PostMapping("/queryWeatherForecast")
    public Response queryWeatherForecast(@RequestBody QueryWeatherDto queryWeatherDto) {
        try {
            IPage<WeatherDay> page = new Page<>(queryWeatherDto.getCurrentPage(), queryWeatherDto.getPageCount());
            QueryWrapper<WeatherDay> weatherForecast = new QueryWrapper<>();
            if (queryWeatherDto.getStartTime() != null) {
                weatherForecast.ge("forecastTime", queryWeatherDto.getStartTime());
            }
            if (queryWeatherDto.getEndTime() != null) {
                weatherForecast.le("forecastTime", queryWeatherDto.getEndTime());
            }
            return Response.success(weatherDayService.page(page, weatherForecast));
        } catch (Exception e) {
            log.error("查询天预报失败！{}", e.getMessage());
            return Response.fail();
        }

    }

    @ApiOperation("查询小时天气预报")
    @PostMapping("/queryRealTemperature")
    public Response queryRealTemperature(@RequestBody QueryWeatherDto queryWeatherDto) {
        try {
            IPage<WeatherHour> page = new Page<>(queryWeatherDto.getCurrentPage(), queryWeatherDto.getPageCount());
            QueryWrapper<WeatherHour> realTemperature = new QueryWrapper<>();
            if (queryWeatherDto.getStartTime() != null) {
                realTemperature.ge("weatherTime", queryWeatherDto.getStartTime());
            }
            if (queryWeatherDto.getEndTime() != null) {
                realTemperature.le("weatherTime", queryWeatherDto.getEndTime());
            }
            if (queryWeatherDto.getType() != 0)
                realTemperature.eq("forecastType", queryWeatherDto.getType());
            return Response.success(weatherHourService.page(page, realTemperature));
        } catch (Exception e) {
            log.error("查询小时天气预报失败！", e.getMessage());
            return Response.fail();
        }

    }

//    public static void main(String[] args) {
//        Date date = new Date();
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
//        String current = format.format(date);
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.HOUR_OF_DAY, -1);
//        Date dateBeforeDay = calendar.getTime();
//        String beforeDay = format.format(dateBeforeDay);
//        calendar.setTime(date);
//        calendar.add(Calendar.YEAR, -1);
//        String beforeYear = format.format(calendar.getTime());
//        System.out.print(current + beforeDay + beforeYear);
//    }

    @ApiOperation("天气对比")
    @PostMapping("/queryTempComparison")
    public Response queryTempComparison() {
        try {
            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
            String current = format.format(date);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, -1);
            Date dateBeforeDay = calendar.getTime();
            String beforeDay = format.format(dateBeforeDay);
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, -1);
            String beforeYear = format.format(calendar.getTime());
            QueryWrapper<WeatherHour> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("weatherTime", current).or().eq("weatherTime", beforeDay).or().eq("weatherTime", beforeYear).orderByDesc("weatherTime");
            return Response.success(weatherHourService.list(queryWrapper));
        } catch (Exception e) {
            log.error("查询天气对比数据出错", e);
            return Response.fail();
        }
    }

    @PostMapping("/real")
    public Response addReal(@RequestBody WeatherHour dto) {
        weatherHourService.remove(Wrappers.<WeatherHour>lambdaQuery().eq(WeatherHour::getWeatherTime, dto.getWeatherTime()).eq(WeatherHour::getForecastType, dto.getForecastType()));
        dto.setCreateTime(LocalDateTime.now());
        return weatherHourService.save(dto) ? Response.success() : Response.fail("操作失败");
    }

    @PostMapping("/forecast")
    public Response addDay(@RequestBody WeatherDay dto) {
        weatherDayService.remove(Wrappers.<WeatherDay>lambdaQuery().eq(WeatherDay::getForecastTime, dto.getForecastTime()));
        dto.setCreateTime(LocalDateTime.now());
        return weatherDayService.save(dto) ? Response.success() : Response.fail("操作失败");
    }

}
