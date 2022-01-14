package com.bmts.heating.commons.db.service.impl;


import com.bmts.heating.commons.basement.model.db.entity.WeatherDay;
import com.bmts.heating.commons.db.mapper.WeatherDayMapper;
import com.bmts.heating.commons.db.service.WeatherDayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author naming
 * @since 2021-04-25
 */
@Service
public class WeatherDayServiceImpl extends ServiceImpl<WeatherDayMapper, WeatherDay> implements WeatherDayService {

}
