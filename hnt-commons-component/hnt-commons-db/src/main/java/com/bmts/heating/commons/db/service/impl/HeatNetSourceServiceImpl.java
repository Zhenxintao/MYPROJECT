package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.HeatNetSource;
import com.bmts.heating.commons.db.mapper.HeatNetSourceMapper;
import com.bmts.heating.commons.db.service.HeatNetSourceService;
import org.springframework.stereotype.Service;

@Service
public class HeatNetSourceServiceImpl  extends ServiceImpl<HeatNetSourceMapper, HeatNetSource> implements HeatNetSourceService {
}
