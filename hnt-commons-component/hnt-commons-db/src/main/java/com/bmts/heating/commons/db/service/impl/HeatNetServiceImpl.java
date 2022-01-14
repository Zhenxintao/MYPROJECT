package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.db.entity.HeatNet;
import com.bmts.heating.commons.basement.model.db.entity.NetSource;
import com.bmts.heating.commons.db.mapper.HeatNetMapper;
import com.bmts.heating.commons.db.service.HeatNetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
@Service
public class HeatNetServiceImpl extends ServiceImpl<HeatNetMapper, HeatNet> implements HeatNetService {

	@Autowired
	private HeatNetMapper mapper;

	@Override
	public List<NetSource> sourceNet(QueryWrapper queryWrapper) {
		return mapper.netJoinSource(queryWrapper);
	}
}
