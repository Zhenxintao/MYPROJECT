package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.entity.TemplatePoint;

public interface TemplatePointService  extends IService<TemplatePoint> {

	Page<PointStandard> loadOtherPoint(Page page, Integer id);

	Page<PointStandard> loadOtherPoint(Page page, Integer templateId, String keyWord);

	Page pageUnion(Page page, Wrapper wrapper);

	int copyCollectTemplate(Integer templateId, Wrapper wrapper);
}
