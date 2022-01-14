package com.bmts.heating.bussiness.baseInformation.app.converter;

import com.bmts.heating.commons.entiy.baseInfo.pojo.PointControlConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel="spring")
public interface PointControlConfigConverter {

	PointControlConfigConverter INSTANCE = Mappers.getMapper(PointControlConfigConverter.class);

	com.bmts.heating.commons.basement.model.db.entity.PointControlConfig domain2dto(PointControlConfig pointControlConfig);

	List<com.bmts.heating.commons.basement.model.db.entity.PointControlConfig> domain2dto(List<PointControlConfig> pointControlConfigList);

}
