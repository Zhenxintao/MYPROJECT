package com.bmts.heating.bussiness.baseInformation.app.converter;

import com.bmts.heating.commons.entiy.baseInfo.pojo.HeatSource;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HeatSourceConverter {

    HeatSourceConverter INSTANCE = Mappers.getMapper(HeatSourceConverter.class);

    com.bmts.heating.commons.basement.model.db.entity.HeatSource domain2dto(HeatSource heatSource);

    List<com.bmts.heating.commons.basement.model.db.entity.HeatSource> domain2dto(List<HeatSource> heatSourceList);

}
