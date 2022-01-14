package com.bmts.heating.web.scada.converter;

import com.bmts.heating.commons.entiy.baseInfo.cache.PointCache;
import com.bmts.heating.commons.entiy.converge.HeatStationRealResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PointLRealConverter {

    PointLRealConverter INSTANCE = Mappers.getMapper(PointLRealConverter.class);

    @Mappings({
            @Mapping(source = "pointName", target = "pointName"),
            @Mapping(source = "value", target = "pointValue"),
            @Mapping(source = "timeStrap", target = "timeStamp")
    })
    HeatStationRealResponseDto.PointReal domainToInfo(PointCache pointCache);


}
