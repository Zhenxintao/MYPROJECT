package com.bmts.heating.bussiness.baseInformation.app.converter;

import com.bmts.heating.commons.entiy.baseInfo.pojo.TemplatePoint;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TemplatePointConverter {

	TemplatePointConverter INSTANCE = Mappers.getMapper(TemplatePointConverter.class);

	com.bmts.heating.commons.basement.model.db.entity.TemplatePoint dtoToPojo(TemplatePoint dto);

}
