package com.bmts.heating.commons.db.service.second.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.second.db.entity.PointConfigSecond;
import com.bmts.heating.commons.db.mapper.second.PointConfigSecondMapper;
import com.bmts.heating.commons.db.service.second.PointConfigSecondService;
import com.bmts.heating.commons.entiy.second.request.device.PointConfigSecondDto;
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
public class PointConfigSecondServiceImpl extends ServiceImpl<PointConfigSecondMapper, PointConfigSecond> implements PointConfigSecondService {

    @Autowired
    private PointConfigSecondMapper mapper;

    @Override
    public List<PointConfigSecondDto> secondQueryPoint(QueryWrapper wrapper) {
        return mapper.secondQueryPoint(wrapper);
    }
}
