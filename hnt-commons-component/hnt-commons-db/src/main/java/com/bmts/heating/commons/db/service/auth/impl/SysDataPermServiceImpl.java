package com.bmts.heating.commons.db.service.auth.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.SysDataPerm;
import com.bmts.heating.commons.db.mapper.auth.SysDataPermMapper;
import com.bmts.heating.commons.db.service.auth.SysDataPermService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author naming
 * @since 2020-12-11
 */
@Service
public class SysDataPermServiceImpl extends ServiceImpl<SysDataPermMapper, SysDataPerm> implements SysDataPermService {

}
