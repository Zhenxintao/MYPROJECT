package com.bmts.heating.commons.db.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.LogOperationBalance;
import com.bmts.heating.commons.basement.model.db.entity.LogOperationControl;
import com.bmts.heating.commons.db.mapper.LogOperationBalanceMapper;
import com.bmts.heating.commons.db.mapper.LogOperationControlMapper;
import com.bmts.heating.commons.db.service.LogOperationBalanceService;
import com.bmts.heating.commons.db.service.LogOperationControlService;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.basement.BalanceLogDto;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LogOperationBalanceServiceImpl extends ServiceImpl<LogOperationBalanceMapper, LogOperationBalance> implements LogOperationBalanceService {

}
