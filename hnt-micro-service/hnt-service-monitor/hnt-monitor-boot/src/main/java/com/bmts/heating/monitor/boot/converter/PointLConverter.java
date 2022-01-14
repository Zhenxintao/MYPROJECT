package com.bmts.heating.monitor.boot.converter;

import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.PointInfo;
import com.bmts.heating.commons.utils.msmq.PointL;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PointLConverter {

    PointLConverter INSTANCE = Mappers.getMapper(PointLConverter.class);

    List<PointInfo> domainToInfo(List<PointL> pointList);
}
