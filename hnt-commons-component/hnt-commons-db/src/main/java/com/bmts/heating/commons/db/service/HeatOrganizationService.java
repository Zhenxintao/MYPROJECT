package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.HeatOrganization;
import com.bmts.heating.commons.basement.model.db.response.CommonTree;
import com.bmts.heating.commons.basement.model.db.response.HeatNetSourceTree;
import com.bmts.heating.commons.basement.model.db.response.OrgAndStation;
import com.bmts.heating.commons.basement.model.db.response.OrgAndStationTree;
import com.bmts.heating.commons.entiy.baseInfo.request.QueryTreeDto;

import java.util.*;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
public interface HeatOrganizationService extends IService<HeatOrganization> {

	int insert(HeatOrganization heatOrganization);

	List<HeatOrganization> queryAll();

	int deleteByIds(List<Integer> ids);


	List<OrgAndStationTree> findStationAndSystem(List<Integer> ids);

	List<OrgAndStationTree> queryOrganizationTreeAndStation(Integer level);
}
