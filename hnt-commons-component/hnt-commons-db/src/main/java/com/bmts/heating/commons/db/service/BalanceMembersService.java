package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.BalanceMembers;
import com.bmts.heating.commons.entiy.balance.pojo.BalanceSystemInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author naming
 * @since 2021-01-29
 */
public interface BalanceMembersService extends IService<BalanceMembers> {
    //全网平衡某热网系统信息接口
    List<BalanceSystemInfo> balanceSystemInfo(QueryWrapper queryWrapper);
}
