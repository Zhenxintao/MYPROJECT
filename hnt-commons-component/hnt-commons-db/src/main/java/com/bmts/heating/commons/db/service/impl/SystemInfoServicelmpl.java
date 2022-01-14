package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.SystemInfo;
import com.bmts.heating.commons.db.mapper.SystemInfoMapper;
import com.bmts.heating.commons.db.service.SystemInfoService;
import org.springframework.stereotype.Service;

@Service
public class SystemInfoServicelmpl extends ServiceImpl<SystemInfoMapper, SystemInfo> implements SystemInfoService {
}
