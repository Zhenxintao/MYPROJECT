package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.HeatCabinet;
import com.bmts.heating.commons.basement.model.db.response.HeatCabinetResponse;
import com.bmts.heating.commons.db.mapper.HeatCabinetMapper;
import com.bmts.heating.commons.db.service.HeatCabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
@Service
public class HeatCabinetServiceImpl extends ServiceImpl<HeatCabinetMapper, HeatCabinet> implements HeatCabinetService {

    @Autowired
    private HeatCabinetMapper heatCabinetMapper;

    @Override
    public List<HeatCabinetResponse> queryCabinet(QueryWrapper queryWrapper) {
        return heatCabinetMapper.queryCabinet(queryWrapper);
    }
}
