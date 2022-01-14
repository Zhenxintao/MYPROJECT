package com.bmts.heating.bussiness.common.converter;

import com.bmts.heating.commons.basement.model.cache.PointCache;
import com.bmts.heating.commons.utils.msmq.PointL;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IssueConverter {

    IssueConverter INSTANCE = Mappers.getMapper(IssueConverter.class);

    PointL dtoToPojo(PointCache cache);

    List<PointL> toList(List<PointCache> caches);


}
