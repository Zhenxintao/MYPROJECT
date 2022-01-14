package com.bmts.heating.web.forecast.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.ForecastWeatherCoef;
import com.bmts.heating.commons.basement.model.db.request.ForecastWeatherCoefDto;
import com.bmts.heating.commons.basement.model.db.request.Weather;
import com.bmts.heating.commons.basement.model.db.request.WindDirection;
import com.bmts.heating.commons.basement.model.db.request.WindPower;
import com.bmts.heating.commons.db.mapper.ForecastWeatherCoefMapper;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.forecast.service.ForecastWeatherCoefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForecastWeatherCoefServiceImpl extends ServiceImpl<ForecastWeatherCoefMapper, ForecastWeatherCoef> implements ForecastWeatherCoefService {

    @Autowired
    private ForecastWeatherCoefMapper forecastWeatherCoefMapper;

    @Override
    public Response insertForecastWeatherCoef(ForecastWeatherCoefDto forecastWeatherCoefdto) {
        String weather = JSONObject.toJSONString(forecastWeatherCoefdto.getWeather());
        String windPower = JSONObject.toJSONString(forecastWeatherCoefdto.getWindPower());
        String windDirection = JSONObject.toJSONString(forecastWeatherCoefdto.getWindDirection());
        ForecastWeatherCoef forecastWeatherCoef1 = new ForecastWeatherCoef();
        forecastWeatherCoef1.setWeather(weather);
        forecastWeatherCoef1.setWindPower(windPower);
        forecastWeatherCoef1.setWindDirection(windDirection);
        int insert = forecastWeatherCoefMapper.insert(forecastWeatherCoef1);
        if (insert != 0) {
            return Response.success(insert);
        }
        return Response.fail("添加失败");
    }

    @Override
    public Response findForecastWeatherCoef(BaseDto baseDto) {
        QueryWrapper<ForecastWeatherCoef> queryWrapper = new QueryWrapper<>();
        Page<ForecastWeatherCoef> page = new Page<>(baseDto.getCurrentPage(), baseDto.getPageCount());
        if (baseDto.getKeyWord() != null) {
            queryWrapper.like("weather", baseDto.getKeyWord()).or().like("wind_power", baseDto.getKeyWord()).or().like("wind_direction", baseDto.getKeyWord());
        }
        IPage<ForecastWeatherCoef> forecastWeatherCoefIPage = forecastWeatherCoefMapper.selectPage(page, queryWrapper);
        for (ForecastWeatherCoef record : forecastWeatherCoefIPage.getRecords()) {
            List<Weather> weather = (List<Weather>) JSONObject.parse(record.getWeather());
            List<WindPower> windPower = (List<WindPower>) JSONObject.parse(record.getWindPower());
            List<WindDirection> windDirection = (List<WindDirection>) JSONObject.parse(record.getWindDirection());
            record.setWeathers(weather);
            record.setWindPowers(windPower);
            record.setWindDirections(windDirection);
            System.out.println(record);
        }
        if (forecastWeatherCoefIPage == null) {
            return Response.fail("未查询到数据");
        }
        return Response.success(forecastWeatherCoefIPage);
    }

    @Override
    public Response updateForecastWeatherCoef(ForecastWeatherCoefDto forecastWeatherCoefDto) {
        ForecastWeatherCoef forecastWeatherCoef2 = forecastWeatherCoefMapper.selectById(forecastWeatherCoefDto.getId());
        if (forecastWeatherCoef2 != null) {
            forecastWeatherCoef2.setWindPower(JSONObject.toJSONString(forecastWeatherCoefDto.getWindPower()));
            forecastWeatherCoef2.setWeather(JSONObject.toJSONString(forecastWeatherCoefDto.getWeather()));
            forecastWeatherCoef2.setWindDirection(JSONObject.toJSONString(forecastWeatherCoefDto.getWindDirection()));
            int count = forecastWeatherCoefMapper.updateById(forecastWeatherCoef2);
            if (count == 0) {
                return Response.fail("修改失败");
            }
            return Response.success("修改成功");

        }
        return Response.fail("该预报系数不存在");
    }

    @Override
    public Response deleteForecastWeatherCoef(int id) {
        ForecastWeatherCoef forecastWeatherCoef = forecastWeatherCoefMapper.selectById(id);
        if (forecastWeatherCoef != null) {
            int i = forecastWeatherCoefMapper.deleteById(id);
            if (i == 0) {
                return Response.fail("删除失败");
            }
            return Response.success("删除成功");
        }
        return Response.fail("该预报系数不存在");
    }
}
