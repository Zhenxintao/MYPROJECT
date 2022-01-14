//package com.bmts.heating.commons.db.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.Wrapper;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.bmts.heating.commons.basement.model.db.entity.PointCollectConfig;
//import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
//import com.bmts.heating.commons.basement.model.db.response.PointCollectConfigResponse;
//import com.bmts.heating.commons.db.mapper.PointCollectConfigMapper;
//import com.bmts.heating.commons.db.mapper.PointConfigMapper;
//import com.bmts.heating.commons.db.service.PointCollectConfigService;
//import com.bmts.heating.commons.entiy.baseInfo.response.CabinetPointResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
// * <p>
// * 服务实现类
// * </p>
// *
// * @author naming
// * @since 2020-11-09
// */
//@Service
//public class PointCollectConfigServiceImpl extends ServiceImpl<PointCollectConfigMapper, PointCollectConfig> implements PointCollectConfigService {
//
//    @Autowired
//    private PointConfigMapper mapper;
//
//    @Override
//    public Page page(Page<PointCollectConfigResponse> page, Wrapper wrapper) {
//
//        return mapper.page(page, wrapper);
//    }
//
//    @Override
//    public PointCollectConfigResponse emptyInfo(Integer id) {
//        return mapper.emptyInfo(id);
//    }
//
////    @Override
////    public Page<PointStandard> loadOtherPoint(Page<PointStandard> page, Integer heatSystemId) {
////        return mapper.loadOtherPoint(page, heatSystemId);
////    }
////
////    @Override
////    public Page<PointStandard> loadOtherPoint(Page<PointStandard> page, Integer heatSystemId, String keWord) {
////        return mapper.loadOtherPointAndSearch(page, heatSystemId, keWord);
////    }
//
//    @Override
//    public List<CabinetPointResponse> pointsAndSystem(int cabinetId) {
//        QueryWrapper<CabinetPointResponse> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("hc.id", cabinetId);
//        List<CabinetPointResponse> cabinetPointResponses = mapper.loadPointsByCabinet(queryWrapper);
//        return cabinetPointResponses;
//    }
//}
