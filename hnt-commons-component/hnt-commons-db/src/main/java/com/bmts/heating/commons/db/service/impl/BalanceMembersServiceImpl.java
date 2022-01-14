package com.bmts.heating.commons.db.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.db.entity.BalanceMembers;
import com.bmts.heating.commons.db.mapper.BalanceMembersMapper;
import com.bmts.heating.commons.db.service.BalanceMembersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.entiy.balance.pojo.BalanceSystemInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author naming
 * @since 2021-01-29
 */
@Service
public class BalanceMembersServiceImpl extends ServiceImpl<BalanceMembersMapper, BalanceMembers> implements BalanceMembersService {
    @Autowired
    private  BalanceMembersMapper balanceMembersMapper;
    @Override
    public List<BalanceSystemInfo> balanceSystemInfo(QueryWrapper queryWrapper)
    {
       return balanceMembersMapper.balanceSystemInfo(queryWrapper);
    }
}
