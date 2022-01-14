package com.bmts.heating.bussiness.baseInformation.app.converter;

import com.bmts.heating.commons.entiy.baseInfo.pojo.TemplateCollect;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel="spring")
public interface TemplateCollectConverter {

	TemplateCollectConverter INSTANCE = Mappers.getMapper(TemplateCollectConverter.class);

	com.bmts.heating.commons.basement.model.db.entity.TemplateCollect domain2dto(TemplateCollect templateControl);

	List<com.bmts.heating.commons.basement.model.db.entity.TemplateCollect> domain2dto(List<TemplateCollect> templateCollectList);

}
