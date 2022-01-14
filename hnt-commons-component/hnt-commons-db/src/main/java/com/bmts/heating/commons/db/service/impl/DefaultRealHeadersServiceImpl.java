package com.bmts.heating.commons.db.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.DefaultRealHeaders;
import com.bmts.heating.commons.db.mapper.DefaultRealHeadersMapper;
import com.bmts.heating.commons.db.service.DefaultRealHeadersService;
import com.bmts.heating.commons.entiy.baseInfo.response.DefaultRealHeadersDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 业务  service  实现类
 * </p>
 *
 * @author PXF
 * @since 2020-11-16
 */
@Service
public class DefaultRealHeadersServiceImpl extends ServiceImpl<DefaultRealHeadersMapper, DefaultRealHeaders> implements DefaultRealHeadersService {

    @Autowired
    private DefaultRealHeadersMapper defaultRealHeadersMapper;


    @Override
    public List<DefaultRealHeadersDto> listHeaders(QueryWrapper queryWrapper) {
        return defaultRealHeadersMapper.listHeaders(queryWrapper);
    }
}
