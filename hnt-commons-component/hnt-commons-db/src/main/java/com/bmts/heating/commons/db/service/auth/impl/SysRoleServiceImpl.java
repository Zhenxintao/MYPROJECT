package com.bmts.heating.commons.db.service.auth.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.bmts.heating.commons.basement.model.db.entity.SysRole;
import com.bmts.heating.commons.db.mapper.auth.SysRoleMapper;
import com.bmts.heating.commons.db.service.auth.SysRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author naming
 * @since 2020-08-05
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

}
