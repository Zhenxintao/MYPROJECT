package com.bmts.heating.bussiness.baseInformation.app.converter;

import com.bmts.heating.commons.entiy.baseInfo.pojo.TemplateControl;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel="spring")
public interface TemplateControlConverter {
	TemplateControlConverter INSTANCE = Mappers.getMapper(TemplateControlConverter.class);

	com.bmts.heating.commons.basement.model.db.entity.TemplateControl domain2dto(TemplateControl templateControl);

	List<com.bmts.heating.commons.basement.model.db.entity.TemplateControl> domain2dto(List<TemplateControl> templateControlList);


}
