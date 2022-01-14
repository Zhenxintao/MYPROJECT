package com.bmts.heating.web.backStage.service.base;

import com.bmts.heating.commons.entiy.baseInfo.request.monitor.MonitorDto;
import com.bmts.heating.commons.entiy.baseInfo.response.CommonTree;
import com.bmts.heating.commons.utils.restful.Response;

import java.util.List;

public interface TreeService {


    List<CommonTree> getOrgStationTree(MonitorDto dto);

    Response getOrgSystemTree(MonitorDto dto);
}
