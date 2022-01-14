package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.WeatherForecast;
import com.bmts.heating.commons.db.mapper.WeatherForecastMapper;
import com.bmts.heating.commons.db.service.WeatherForecastService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
@Service
public class WeatherForecastServiceImpl extends ServiceImpl<WeatherForecastMapper, WeatherForecast> implements WeatherForecastService {

}
