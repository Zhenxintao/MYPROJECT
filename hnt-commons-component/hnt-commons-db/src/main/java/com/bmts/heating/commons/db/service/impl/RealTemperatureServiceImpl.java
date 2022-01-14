package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.RealTemperature;
import com.bmts.heating.commons.db.mapper.RealTemperatureMapper;
import com.bmts.heating.commons.db.service.RealTemperatureService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
@Service
public class RealTemperatureServiceImpl extends ServiceImpl<RealTemperatureMapper, RealTemperature> implements RealTemperatureService {

}
