package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.EsDocConfig;
import com.bmts.heating.commons.basement.model.db.entity.EsDocConfigResponse;
import com.bmts.heating.commons.basement.model.db.response.PointConfigResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author naming
 * @since 2021-01-12
 */
public interface EsDocConfigService extends IService<EsDocConfig> {
	Page<EsDocConfigResponse> page(Page page, Wrapper wrapper);
}
