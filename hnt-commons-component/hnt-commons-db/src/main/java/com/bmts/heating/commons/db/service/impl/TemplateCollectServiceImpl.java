//package com.bmts.heating.commons.db.service.impl;
//
//
//import com.baomidou.mybatisplus.core.conditions.Wrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
//import com.bmts.heating.commons.basement.model.db.entity.TemplateCollect;
//import com.bmts.heating.commons.basement.model.db.response.template.TemplateTabInfoResponse;
//import com.bmts.heating.commons.db.mapper.TemplateCollectMapper;
//import com.bmts.heating.commons.db.service.TemplateCollectService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Collection;
//
///**
// * <p>
// * 服务实现类
// * </p>
// *
// * @author naming
// * @since 2020-11-16
// */
//@Service
//public class TemplateCollectServiceImpl extends ServiceImpl<TemplateCollectMapper, TemplateCollect> implements TemplateCollectService {
//
//	@Autowired
//	private TemplateCollectMapper templateCollectMapper;
//
//	public Page<PointStandard> loadOwnPoint(Page page, Integer id) {
//		return templateCollectMapper.loadOwnPoint(page, id);
//	}
//
//	public Page<PointStandard> loadOtherPoint(Page page, Integer templateId) {
//		return templateCollectMapper.loadOtherPoint(page, templateId);
//	}
//
//	public Page<PointStandard> loadOtherPoint(Page page, Integer templateId, String keyWord) {
//		return templateCollectMapper.loadOtherPointAndSearch(page, templateId, keyWord);
//	}
//
//	@Override
//	public Page pageUnion(Page page, Wrapper wrapper) {
//		return templateCollectMapper.pageUnion(page, wrapper);
//	}
//
//
//	public int copyCollectTemplate(Integer templateId, Integer heatSystemId) {
//		templateCollectMapper.copyCollectTemplate(templateId, heatSystemId);
//		return 1;
//	}
//
//	@Override
//	public Collection<TemplateTabInfoResponse> queryTabInfo(Integer templateId) {
//		return templateCollectMapper.queryTabInfo(templateId);
//	}
//}
