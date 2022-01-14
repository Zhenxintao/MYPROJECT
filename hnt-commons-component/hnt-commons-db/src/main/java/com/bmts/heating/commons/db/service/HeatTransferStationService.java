package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.HeatTransferStation;
import com.bmts.heating.commons.basement.model.db.response.CommonTree;
import com.bmts.heating.commons.basement.model.db.response.HeatTransferStationInfo;
import com.bmts.heating.commons.basement.model.db.response.HeatTransferStationResponse;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
public interface HeatTransferStationService extends IService<HeatTransferStation> {


    IPage<HeatTransferStationResponse> queryStationPage(Page<HeatTransferStationResponse> page, Wrapper wrapper);

    List<HeatTransferStationResponse> queryStationList(Wrapper wrapper);

    List<CommonTree> queryStationTree(Wrapper wrapper);

    boolean clear(List<Integer> stationId);

    IPage<HeatTransferStationInfo> pageStation(Page<HeatTransferStationInfo> page, Wrapper wrapper);

    List<CommonTree> querySystemTree(Wrapper wrapper);
}
