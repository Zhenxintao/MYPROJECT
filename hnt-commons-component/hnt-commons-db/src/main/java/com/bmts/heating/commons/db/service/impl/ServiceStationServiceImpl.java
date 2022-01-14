package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.ServiceStation;
import com.bmts.heating.commons.db.mapper.ServiceStationMapper;
import com.bmts.heating.commons.db.service.ServiceStationService;
import org.springframework.stereotype.Service;

@Service
public class ServiceStationServiceImpl extends ServiceImpl<ServiceStationMapper, ServiceStation> implements ServiceStationService {
}
