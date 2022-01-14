package com.bmts.heating.commons.db.service.auth.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.SysUser;
import com.bmts.heating.commons.db.mapper.auth.SysUserMapper;
import com.bmts.heating.commons.db.service.auth.SysUserService;
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
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

}
