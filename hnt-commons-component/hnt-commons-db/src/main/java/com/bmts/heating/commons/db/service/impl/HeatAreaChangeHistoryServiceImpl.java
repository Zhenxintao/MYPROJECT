package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.HeatAreaChangeHistory;
import com.bmts.heating.commons.basement.model.db.response.HeatAreaChangeHistoryResponse;
import com.bmts.heating.commons.db.mapper.HeatAreaChangeHistoryMapper;
import com.bmts.heating.commons.db.service.HeatAreaChangeHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HeatAreaChangeHistoryServiceImpl extends ServiceImpl<HeatAreaChangeHistoryMapper, HeatAreaChangeHistory> implements HeatAreaChangeHistoryService {
    @Autowired
    private  HeatAreaChangeHistoryMapper heatAreaChangeHistoryMapper;


    @Override
    public Page<HeatAreaChangeHistoryResponse> queryHeatAreaChangeHistory(Page page, QueryWrapper queryWrapper, String type) {

        switch (type){
            case "source":
                return heatAreaChangeHistoryMapper.queryHeatSourceAreaChangeHistory(page,queryWrapper);
            case "net":
                return heatAreaChangeHistoryMapper.queryHeatNetAreaChangeHistory(page,queryWrapper);
            case "station":
                return heatAreaChangeHistoryMapper.queryHeatStationAreaChangeHistory(page,queryWrapper);
            case "system":
                return heatAreaChangeHistoryMapper.queryHeatSystemAreaChangeHistory(page,queryWrapper);
        }
        return  new Page<HeatAreaChangeHistoryResponse>();

    }
}
