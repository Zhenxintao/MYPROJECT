package com.bmts.heating.bussiness.baseInformation.app.converter;

import com.bmts.heating.commons.basement.model.db.entity.RealTemperature;
import com.bmts.heating.commons.basement.model.db.entity.WeatherForecast;
import com.bmts.heating.commons.entiy.baseInfo.request.RealTemperatureDto;
import com.bmts.heating.commons.entiy.baseInfo.request.WeatherForecastDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WeatherForecastConverter {

    WeatherForecastConverter INSTANCE = Mappers.getMapper(WeatherForecastConverter.class);

    WeatherForecast dtoToPojo(WeatherForecastDto dto);

    RealTemperature dtoToPojo(RealTemperatureDto dto);


}
