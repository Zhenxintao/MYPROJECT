package com.bmts.heating.bussiness.baseInformation.app.converter;

import com.bmts.heating.commons.entiy.baseInfo.sync.update.HeatCabinetInitDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface HeatCabinetConverter {

    HeatCabinetConverter INSTANCE = Mappers.getMapper(HeatCabinetConverter.class);

    com.bmts.heating.commons.basement.model.db.entity.HeatCabinet domain2dto(HeatCabinetInitDto initDto);


}
