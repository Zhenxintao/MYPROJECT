package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.HeatSource;
import com.bmts.heating.commons.basement.model.db.response.CommonTree;
import com.bmts.heating.commons.basement.model.db.response.HeatSourceResponse;
import com.bmts.heating.commons.db.mapper.HeatSourceMapper;
import com.bmts.heating.commons.db.service.HeatSourceService;
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
public class HeatSourceServiceImpl extends ServiceImpl<HeatSourceMapper, HeatSource> implements HeatSourceService {
    @Autowired
    HeatSourceMapper heatSourceMapper;

    @Override
    public Page<HeatSourceResponse> queryHeatSource(Page page, QueryWrapper queryWrapper) {
        return heatSourceMapper.queryHeatSource(page, queryWrapper);
    }

    @Override
    public HeatSourceResponse heatSourceArea(int id) {
        return heatSourceMapper.heatSourceArea(id);
    }


    @Override
    public List<CommonTree> sourceSystemTree(Wrapper wrapper) {
        return heatSourceMapper.sourceSystemTree(wrapper);
    }
}
