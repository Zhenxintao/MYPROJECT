package com.bmts.heating.commons.db.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.DeviceConfig;
import com.bmts.heating.commons.db.mapper.DeviceConfigMapper;
import com.bmts.heating.commons.db.service.DeviceConfigService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务  service  实现类
 * </p>
 *
 * @author naming
 * @since 2020-11-16
 */
@Service
public class DeviceConfigServiceImpl extends ServiceImpl<DeviceConfigMapper, DeviceConfig> implements DeviceConfigService {

}
