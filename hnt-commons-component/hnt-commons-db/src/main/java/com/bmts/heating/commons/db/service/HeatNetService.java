package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.db.entity.HeatNet;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.NetSource;
import com.bmts.heating.commons.db.mapper.HeatNetMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
public interface HeatNetService extends IService<HeatNet> {

	List<NetSource> sourceNet(QueryWrapper queryWrapper);
}
