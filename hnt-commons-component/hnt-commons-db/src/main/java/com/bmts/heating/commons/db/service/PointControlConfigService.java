package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.PointControlConfig;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.response.PointControlConfigResponse;
import com.bmts.heating.commons.entiy.baseInfo.response.CabinetPointResponse;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
public interface PointControlConfigService extends IService<PointControlConfig> {

	Page<PointControlConfigResponse> page(Page page, Wrapper wrapper);

	PointControlConfigResponse emptyInfo(Integer id);

	Page<PointStandard> loadOtherPoint(Page<PointStandard> page, Integer heatSystemId);

	Page<PointStandard> loadOtherPoint(Page<PointStandard> page, Integer heatSystemId, String keWord);
	List<CabinetPointResponse> pointsAndSystem(int cabinetId);
}
