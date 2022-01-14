package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.entity.TemplateCollect;
import com.bmts.heating.commons.basement.model.db.response.template.TemplateTabInfoResponse;

import java.util.Collection;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author naming
 * @since 2020-11-16
 */
public interface TemplateCollectService extends IService<TemplateCollect> {

	Page<PointStandard> loadOwnPoint(Page page, Integer id);

	Page<PointStandard> loadOtherPoint(Page page ,Integer templateId);

	Page<PointStandard> loadOtherPoint(Page page ,Integer templateId, String keyWord);

	Page pageUnion(Page page , Wrapper wrapper);

	int copyCollectTemplate(Integer templateId,Integer heatSystemId);

	Collection<TemplateTabInfoResponse> queryTabInfo(Integer templateId);
}
