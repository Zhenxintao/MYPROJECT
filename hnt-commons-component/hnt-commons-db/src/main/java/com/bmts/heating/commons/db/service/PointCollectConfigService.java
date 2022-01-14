package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.PointCollectConfig;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.response.PointCollectConfigResponse;
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
public interface PointCollectConfigService extends IService<PointCollectConfig> {

	Page<PointCollectConfigResponse> page(Page<PointCollectConfigResponse> page, Wrapper wrapper);

	PointCollectConfigResponse emptyInfo(Integer id);

//	Page<PointStandard> loadOtherPoint(Page<PointStandard> page, Integer heatSystemId);
//
//	Page<PointStandard> loadOtherPoint(Page<PointStandard> page, Integer heatSystemId, String keWord);
	List<CabinetPointResponse> pointsAndSystem(int cabinetId);
}
