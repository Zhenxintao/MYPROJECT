package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.AlarmStorage;
import com.bmts.heating.commons.db.mapper.AlarmStorageMapper;
import com.bmts.heating.commons.db.service.AlarmStorageService;
import org.springframework.stereotype.Service;

@Service
public class AlarmStorageServiceImpl extends ServiceImpl<AlarmStorageMapper, AlarmStorage> implements AlarmStorageService {
}
