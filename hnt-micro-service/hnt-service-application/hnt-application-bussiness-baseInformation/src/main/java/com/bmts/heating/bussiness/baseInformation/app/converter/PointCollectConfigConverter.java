package com.bmts.heating.bussiness.baseInformation.app.converter;

import com.bmts.heating.commons.entiy.baseInfo.pojo.PointCollectConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel="spring")
public interface PointCollectConfigConverter {

	PointCollectConfigConverter INSTANCE = Mappers.getMapper(PointCollectConfigConverter.class);

	com.bmts.heating.commons.basement.model.db.entity.PointCollectConfig domain2dto(PointCollectConfig pointCollectConfig);

	List<com.bmts.heating.commons.basement.model.db.entity.PointCollectConfig> domain2dto(List<PointCollectConfig> pointCollectConfigList);


}
