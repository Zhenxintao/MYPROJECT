//package com.bmts.heating.commons.db.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.Wrapper;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.bmts.heating.commons.basement.model.db.entity.PointControlConfig;
//import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
//import com.bmts.heating.commons.basement.model.db.response.PointControlConfigResponse;
//import com.bmts.heating.commons.db.mapper.PointControlConfigMapper;
//import com.bmts.heating.commons.db.service.PointControlConfigService;
//import com.bmts.heating.commons.entiy.baseInfo.response.CabinetPointResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
// * <p>
// *  服务实现类
// * </p>
// *
// * @author naming
// * @since 2020-11-09
// */
//@Service
//public class PointControlConfigServiceImpl extends ServiceImpl<PointControlConfigMapper, PointControlConfig> implements PointControlConfigService {
//
//	@Autowired
//	private PointControlConfigMapper mapper;
//
//	public Page<PointControlConfigResponse> page(Page page, Wrapper wrapper){
//		return mapper.page(page,wrapper);
//	}
//
//	@Override
//	public PointControlConfigResponse emptyInfo(Integer id) {
//		return mapper.emptyInfo(id);
//	}
//
//	@Override
//	public Page<PointStandard> loadOtherPoint(Page<PointStandard> page, Integer heatSystemId){
//		return mapper.loadOtherPoint(page, heatSystemId);
//	}
//
//	@Override
//	public Page<PointStandard> loadOtherPoint(Page<PointStandard> page, Integer heatSystemId, String keWord){
//		return mapper.loadOtherPointAndSearch(page, heatSystemId,keWord);
//	}
//	@Override
//	public List<CabinetPointResponse> pointsAndSystem(int cabinetId)
//	{
//		QueryWrapper<CabinetPointResponse> queryWrapper=new QueryWrapper<>();
//		queryWrapper.eq("cabinetId",cabinetId);
//		List<CabinetPointResponse> cabinetPointResponses = mapper.loadPointsByCabinet(queryWrapper);
//		return cabinetPointResponses;
//	}
//}
