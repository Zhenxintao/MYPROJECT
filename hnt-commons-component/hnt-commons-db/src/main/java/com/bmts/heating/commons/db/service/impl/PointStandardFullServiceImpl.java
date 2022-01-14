package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.entity.PointStandardFull;
import com.bmts.heating.commons.basement.model.db.response.PointStandardResponse;
import com.bmts.heating.commons.db.mapper.PointStandardFullMapper;
import com.bmts.heating.commons.db.mapper.PointStandardMapper;
import com.bmts.heating.commons.db.service.PointStandardFullService;
import com.bmts.heating.commons.db.service.PointStandardService;
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
public class PointStandardFullServiceImpl extends ServiceImpl<PointStandardFullMapper, PointStandardFull> implements PointStandardFullService {

    @Autowired
    PointStandardFullMapper pointStandardFullMapper;

}
