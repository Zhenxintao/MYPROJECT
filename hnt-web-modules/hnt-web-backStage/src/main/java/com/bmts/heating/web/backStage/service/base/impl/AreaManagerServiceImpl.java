package com.bmts.heating.web.backStage.service.base.impl;

import com.bmts.heating.commons.entiy.energy.AreaChangeHistoryDto;
import com.bmts.heating.commons.entiy.energy.AreaManagerDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.base.AreaManagerService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AreaManagerServiceImpl implements AreaManagerService {

    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    private final String url = "/areaManager/";


    @Override
    public Response update(AreaManagerDto dto) {
        return backRestTemplate.post(url + "update", dto, baseServer);
    }

    @Override
    public Response list(Integer id) {
        return backRestTemplate.get(url + id, baseServer);
    }

    @Override
    public Response getAreaChangeHistoryList(AreaChangeHistoryDto dto) {
        return backRestTemplate.post(url + "heatAreaChangeHistory", dto, baseServer);
    }


}
