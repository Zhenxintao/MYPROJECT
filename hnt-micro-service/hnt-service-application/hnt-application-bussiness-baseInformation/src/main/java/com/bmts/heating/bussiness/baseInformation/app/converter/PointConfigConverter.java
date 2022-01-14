package com.bmts.heating.bussiness.baseInformation.app.converter;

import com.bmts.heating.commons.entiy.baseInfo.pojo.PointConfig;
import com.bmts.heating.commons.entiy.baseInfo.sync.update.HeatPointInitDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PointConfigConverter {

    PointConfigConverter INSTANCE = Mappers.getMapper(PointConfigConverter.class);

    com.bmts.heating.commons.basement.model.db.entity.PointConfig domain2dto(PointConfig pointConfig);

    List<com.bmts.heating.commons.basement.model.db.entity.PointConfig> domain2dto(List<PointConfig> pointConfigs);

    com.bmts.heating.commons.basement.model.db.entity.PointConfig dtoToPo(HeatPointInitDto initDto);
}
