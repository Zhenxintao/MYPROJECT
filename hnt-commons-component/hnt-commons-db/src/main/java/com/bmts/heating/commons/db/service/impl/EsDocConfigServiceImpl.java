package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.EsDocConfig;
import com.bmts.heating.commons.basement.model.db.entity.EsDocConfigResponse;
import com.bmts.heating.commons.db.mapper.EsDocConfigMapper;
import com.bmts.heating.commons.db.service.EsDocConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author naming
 * @since 2021-01-12
 */
@Service
public class EsDocConfigServiceImpl extends ServiceImpl<EsDocConfigMapper, EsDocConfig> implements EsDocConfigService {

	@Autowired
	private EsDocConfigMapper mapper;

	@Override
	public Page<EsDocConfigResponse> page(Page page, Wrapper wrapper) {
		return mapper.page(page,wrapper);
	}
}
