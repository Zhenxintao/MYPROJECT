package com.bmts.heating.bussiness.baseInformation.app.converter;

import com.bmts.heating.commons.entiy.baseInfo.sync.update.HeatSystemInitDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface HeatSystemConverter {

    HeatSystemConverter INSTANCE = Mappers.getMapper(HeatSystemConverter.class);

    com.bmts.heating.commons.basement.model.db.entity.HeatSystem domain2dto(HeatSystemInitDto initDto);


}
