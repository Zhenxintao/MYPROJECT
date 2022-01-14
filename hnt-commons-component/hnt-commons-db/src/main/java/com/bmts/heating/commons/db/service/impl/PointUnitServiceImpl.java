package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.PointUnit;
import com.bmts.heating.commons.basement.model.db.response.PointStandardResponse;
import com.bmts.heating.commons.db.mapper.PointUnitMapper;
import com.bmts.heating.commons.db.service.PointUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-11-27
 */
@Service
public class PointUnitServiceImpl extends ServiceImpl<PointUnitMapper, PointUnit> implements PointUnitService {

    @Autowired
    private PointUnitMapper pointUnitMapper;

    @Override
    public List<PointStandardResponse> listPoint(QueryWrapper queryWrapper) {
        return pointUnitMapper.listPoint(queryWrapper);
    }
}
