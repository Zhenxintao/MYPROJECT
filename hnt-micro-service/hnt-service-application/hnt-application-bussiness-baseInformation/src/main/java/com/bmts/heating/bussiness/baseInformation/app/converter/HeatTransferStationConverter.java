package com.bmts.heating.bussiness.baseInformation.app.converter;

import com.bmts.heating.commons.entiy.baseInfo.pojo.HeatTransferStation;
import com.bmts.heating.commons.entiy.baseInfo.sync.update.HeatStationInitDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HeatTransferStationConverter {

    HeatTransferStationConverter INSTANCE = Mappers.getMapper(HeatTransferStationConverter.class);

    com.bmts.heating.commons.basement.model.db.entity.HeatTransferStation domain2dto(HeatTransferStation heatTransferStation);

    List<com.bmts.heating.commons.basement.model.db.entity.HeatTransferStation> domain2dto(List<HeatTransferStation> heatTransferStationList);

    com.bmts.heating.commons.basement.model.db.entity.HeatTransferStation dtoToPo(HeatStationInitDto initDto);

}
