package com.bmts.heating.service.task.storm.converter;

import com.bmts.heating.commons.basement.model.cache.PointCache;
import com.bmts.heating.commons.utils.msmq.PointL;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PointCacheConverter {

    PointCacheConverter INSTANCE = Mappers.getMapper(PointCacheConverter.class);

    List<PointL> toList(List<PointCache> caches);


}
