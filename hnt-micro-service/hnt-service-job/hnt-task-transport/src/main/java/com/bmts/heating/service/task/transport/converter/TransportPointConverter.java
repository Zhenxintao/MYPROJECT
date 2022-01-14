package com.bmts.heating.service.task.transport.converter;

import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.PointInfo;
import com.bmts.heating.commons.entiy.second.request.point.TransportPoint;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransportPointConverter {

    TransportPointConverter INSTANCE = Mappers.getMapper(TransportPointConverter.class);

    List<PointInfo> domainToInfo(List<TransportPoint> points);
}
