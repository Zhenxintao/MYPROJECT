package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.entity.TemplatePoint;
import com.bmts.heating.commons.db.mapper.TemplatePointMapper;
import com.bmts.heating.commons.db.service.TemplatePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TemplatePointServiceImpl extends ServiceImpl<TemplatePointMapper, TemplatePoint> implements TemplatePointService {

	@Autowired
	private TemplatePointMapper mapper;


	public Page<PointStandard> loadOtherPoint(Page page, Integer id){
		return mapper.loadOtherPoint(page, id);
	}

	public Page<PointStandard> loadOtherPoint(Page page, Integer templateId, String keyWord) {
		return mapper.loadOtherPointAndSearch(page, templateId, keyWord);
	}

	@Override
	public Page pageUnion(Page page, Wrapper wrapper) {
		return mapper.pageUnion(page,wrapper);
	}

	@Override
	public int copyCollectTemplate(Integer templateId, Wrapper wrapper) {
		mapper.copyControlTemplate(templateId,wrapper);
		return 1;
	}
}
