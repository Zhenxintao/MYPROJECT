package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.PointAlarmView;
import com.bmts.heating.commons.basement.model.db.entity.PointConfig;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.response.PointConfigResponse;
import com.bmts.heating.commons.db.mapper.PointConfigMapper;
import com.bmts.heating.commons.db.service.PointAlarmViewService;
import com.bmts.heating.commons.db.service.PointConfigService;
import com.bmts.heating.commons.db.service.PointStandardAlarmViewService;
import com.bmts.heating.commons.entiy.baseInfo.response.CabinetPointResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author naming
 * @since 2021-01-29
 */
@Service
public class PointConfigServiceImpl extends ServiceImpl<PointConfigMapper, PointConfig> implements PointConfigService {
//	@Override
//	public Page<PointConfigResponse> page(Page page, Wrapper wrapper) {
//		return null;
//	}
//
//	@Override
//	public PointCollectConfigResponse emptyInfo(Integer id) {
//		return null;
//	}
//
//	@Override
//	public Page<PointStandard> loadOtherPoint(Page<PointStandard> page, int level, Integer relevanceId) {
//		return null;
//	}
//
//	@Override
//	public Page<PointStandard> loadOtherPoint(Page<PointStandard> page, int level, Integer relevanceId, String keWord) {
//		return null;
//	}


    @Autowired
    private PointConfigMapper mapper;

    @Autowired
    private PointStandardAlarmViewService pointStandardAlarmViewService;
    @Autowired
    private PointAlarmViewService pointAlarmViewService;


    @Override
    public Page<PointConfigResponse> page(Page page, Wrapper wrapper) {
        return mapper.page(page, wrapper);
    }

    @Override
    public PointConfigResponse emptyInfo(Integer id) {
        return mapper.emptyInfo(id);
    }

    @Override
    public Page<PointStandard> loadOtherPoint(Page<PointStandard> page, int level, Integer relevanceId) {
        return mapper.loadOtherPoint(page, level, relevanceId);
    }

    @Override
    public Page<PointStandard> loadOtherPoint(Page<PointStandard> page, int level, Integer relevanceId, String keWord) {
        return mapper.loadOtherPointAndSearch(page, level, relevanceId, keWord);
    }

    @Override
    public List<CabinetPointResponse> pointsAndSystem(int cabinetId) {
        QueryWrapper<CabinetPointResponse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hc.id", cabinetId);
        List<CabinetPointResponse> cabinetPointResponses = mapper.loadPointsByCabinet(queryWrapper);
        return cabinetPointResponses;
    }

    @Override
    public Boolean updateBatchStandard(String descriptionJson, int pointStandardId) {
        QueryWrapper<PointConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pointStandardId", pointStandardId);
        List<PointConfig> list = this.list(queryWrapper);
        List<PointConfig> updateList = new ArrayList<>();
        for (PointConfig pointConfig : list) {
            PointConfig update = new PointConfig();
            update.setId(pointConfig.getId());
//            update.setDescriptionJson(descriptionJson);
            updateList.add(update);
        }
        boolean bool = false;
        if (!CollectionUtils.isEmpty(updateList)) {
            bool = this.updateBatchById(updateList);
        }
        return bool;
    }

    @Override
    public List<PointAlarmView> queryAlarm(QueryWrapper queryWrapper) {
        // 查询标准点报警值信息 QueryWrapper<PointAlarmView>
//        List<PointAlarmView> listStandar = pointStandardAlarmViewService.listAlarm(queryWrapper);
        // 查询单独配置的点报警值信息
        List<PointAlarmView> listPoint = pointAlarmViewService.list(queryWrapper);
//        if (!CollectionUtils.isEmpty(listPoint)) {
//            listStandar.addAll(listPoint);
//        }
        return listPoint;
    }
}
