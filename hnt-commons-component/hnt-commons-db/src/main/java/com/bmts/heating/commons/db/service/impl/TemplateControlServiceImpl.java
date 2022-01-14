//package com.bmts.heating.commons.db.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.Wrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
//import com.bmts.heating.commons.basement.model.db.entity.TemplateControl;
//import com.bmts.heating.commons.basement.model.db.response.template.TemplateTabInfoResponse;
//import com.bmts.heating.commons.db.mapper.TemplateControlMapper;
//import com.bmts.heating.commons.db.service.TemplateControlService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Collection;
//
///**
// * <p>
// *  服务实现类
// * </p>
// *
// * @author naming
// * @since 2020-11-16
// */
//@Service
//public class TemplateControlServiceImpl extends ServiceImpl<TemplateControlMapper, TemplateControl> implements TemplateControlService {
//
//	@Autowired
//	private TemplateControlMapper templateControlMapper;
//
//	public Page<PointStandard> loadOwnPoint(Page page,Integer id){
//		return templateControlMapper.loadOwnPoint(page, id);
//	}
//
//	public Page<PointStandard> loadOtherPoint(Page page,Integer id){
//		return templateControlMapper.loadOtherPoint(page, id);
//	}
//
//	public Page<PointStandard> loadOtherPoint(Page page, Integer templateId, String keyWord) {
//		return templateControlMapper.loadOtherPointAndSearch(page, templateId, keyWord);
//	}
//
//	@Override
//	public Page pageUnion(Page page, Wrapper wrapper) {
//		return templateControlMapper.pageUnion(page,wrapper);
//	}
//
//	public int copyControlTemplate(Integer templateId,Integer heatSystemId){
//		templateControlMapper.copyControlTemplate(templateId, heatSystemId);
//		return 1;
//	}
//
//	@Override
//	public Collection<TemplateTabInfoResponse> queryTabInfo(Integer templateId) {
//		return templateControlMapper.queryTabInfo(templateId);
//	}
//}
