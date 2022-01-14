package com.bmts.heating.web.backStage.service.base.impl;

import com.bmts.heating.commons.entiy.baseInfo.request.monitor.MonitorDto;
import com.bmts.heating.commons.entiy.baseInfo.response.CommonTree;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.base.TreeService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class TreeServiceImpl implements TreeService {

    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";


    @Override
    public List<CommonTree> getOrgStationTree(MonitorDto dto) {
        return Arrays.asList(backRestTemplate.post("monitor/orgStationTree", dto, baseServer, CommonTree[].class));
    }

    @Override
    public Response getOrgSystemTree(MonitorDto dto) {
        return backRestTemplate.post("monitor/orgSystemTree", dto, baseServer);
    }


}
