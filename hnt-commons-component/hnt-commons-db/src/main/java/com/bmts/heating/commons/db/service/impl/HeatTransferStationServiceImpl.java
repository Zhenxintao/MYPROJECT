package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.HeatTransferStation;
import com.bmts.heating.commons.basement.model.db.entity.PointCollectConfig;
import com.bmts.heating.commons.basement.model.db.response.CabinetPoint;
import com.bmts.heating.commons.basement.model.db.response.CommonTree;
import com.bmts.heating.commons.basement.model.db.response.HeatTransferStationInfo;
import com.bmts.heating.commons.basement.model.db.response.HeatTransferStationResponse;
import com.bmts.heating.commons.db.mapper.HeatCabinetMapper;
import com.bmts.heating.commons.db.mapper.HeatSystemMapper;
import com.bmts.heating.commons.db.mapper.HeatTransferStationMapper;
import com.bmts.heating.commons.db.mapper.PointConfigMapper;
import com.bmts.heating.commons.db.service.HeatTransferStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
@Service
public class HeatTransferStationServiceImpl extends ServiceImpl<HeatTransferStationMapper, HeatTransferStation> implements HeatTransferStationService {

    @Autowired
    private HeatTransferStationMapper heatTransferStationMapper;
    @Autowired
    private HeatCabinetMapper heatCabinetMapper;
    @Autowired
    private HeatSystemMapper heatSystemMapper;
    @Autowired
    private PointConfigMapper pointConfigMapper;

    @Override
    public IPage<HeatTransferStationResponse> queryStationPage(Page<HeatTransferStationResponse> page, Wrapper wrapper) {
        return heatTransferStationMapper.queryStationPage(page, wrapper);
    }

    @Override
    public List<HeatTransferStationResponse> queryStationList(Wrapper wrapper) {
        return heatTransferStationMapper.queryStationList(wrapper);
    }

    @Override
    public List<CommonTree> queryStationTree(Wrapper wrapper) {
        return heatTransferStationMapper.queryStationTree(wrapper);
    }

    @Override
    public boolean clear(List<Integer> stationId) {
        try {
            List<CabinetPoint> cabinetPoints =
                    heatTransferStationMapper.queryCollectPointAndCabinet
                            (new QueryWrapper<PointCollectConfig>().in("hts.id", stationId));
//            List<CabinetPoint> cabinetPoints1 = heatTransferStationMapper.queryControlPointAndCabinet
//                    (new QueryWrapper<PointControlConfig>().in("hts.id", stationId));
            if (cabinetPoints != null && cabinetPoints.size() > 0)
                pointConfigMapper.deleteBatchIds(cabinetPoints.stream().map(CabinetPoint::getPointConfigId).collect(Collectors.toList()));
//            if (cabinetPoints1 != null && cabinetPoints.size() > 0)
//                pointControlConfigMapper.deleteBatchIds(cabinetPoints1.stream().map(CabinetPoint::getPointConfigId).collect(Collectors.toList()));
//            assert cabinetPoints != null;
//            cabinetPoints.addAll(cabinetPoints1);
            deal(cabinetPoints);
            return true;
        } catch (Exception e) {
            log.error("clear station config cause exception {}", e);
            return false;
        }

    }


    private void deal(List<CabinetPoint> cabinetPoints) {
        List<Integer> cabiNetIds = cabinetPoints.stream().map(CabinetPoint::getId).filter(x -> x != null).distinct().collect(Collectors.toList());
        if (cabiNetIds != null && cabiNetIds.size() > 0)
            heatCabinetMapper.deleteBatchIds(cabiNetIds);
        List<Integer> systemIds = cabinetPoints.stream().map(CabinetPoint::getHeatSystemId).filter(x -> x != null).distinct().collect(Collectors.toList());
        if (systemIds != null && systemIds.size() > 0)
            heatSystemMapper.deleteBatchIds(systemIds);
    }


    @Override
    public IPage<HeatTransferStationInfo> pageStation(Page<HeatTransferStationInfo> page, Wrapper wrapper) {
        return heatTransferStationMapper.pageStation(page, wrapper);
    }

    @Override
    public List<CommonTree> querySystemTree(Wrapper wrapper) {
        return heatTransferStationMapper.querySystemTree(wrapper);
    }
}













