package com.bmts.heating.commons.db.service.impl;

import com.bmts.heating.commons.basement.model.db.entity.PointTemplateConfig;
import com.bmts.heating.commons.db.mapper.PointTemplateConfigMapper;
import com.bmts.heating.commons.db.service.PointTemplateConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
@Service
public class PointTemplateConfigServiceImpl extends ServiceImpl<PointTemplateConfigMapper, PointTemplateConfig> implements PointTemplateConfigService {

	@Autowired
	private PointTemplateConfigMapper pointTemplateConfigMapper;

	@Override
	public Boolean saveTemplate(PointTemplateConfig entry,String type,int id) {
		return null;
	}

}
