package com.bmts.heating.web.scada.converter;

import com.bmts.heating.commons.entiy.baseInfo.cache.PointCache;
import com.bmts.heating.commons.entiy.baseInfo.response.HeatSourcePointRealData;
import com.bmts.heating.commons.entiy.gathersearch.response.cache.PointDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SourcePointLRealConverter {

    SourcePointLRealConverter INSTANCE = Mappers.getMapper(SourcePointLRealConverter.class);

    @Mappings({
            @Mapping(source = "columnName", target = "pointName"),
            @Mapping(source = "value", target = "pointValue"),
            @Mapping(source = "timeStamp", target = "timeStamp")
    })
    HeatSourcePointRealData domainToInfo(PointDetail pointDetail);

    @Mappings({
            @Mapping(source = "pointName", target = "pointName"),
            @Mapping(source = "value", target = "pointValue"),
            @Mapping(source = "timeStrap", target = "timeStamp")
    })
    HeatSourcePointRealData domainToInfo2(PointCache pointCache);

}
