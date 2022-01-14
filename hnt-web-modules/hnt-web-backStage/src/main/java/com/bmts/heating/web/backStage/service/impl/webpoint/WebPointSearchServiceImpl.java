package com.bmts.heating.web.backStage.service.impl.webpoint;

import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointTypeSearchDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.SelectPointConfigDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.webpoint.WebPointSearchService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WebPointSearchServiceImpl extends SavantServices implements WebPointSearchService {
    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

//    @Override
//    public Response queryUnitType(String pointUnitName) {
//        return backRestTemplate.doHttp("/pointStandard/queryType/".concat(pointUnitName), null, baseServer, Response.class, HttpMethod.GET);
//    }

    @Override
    public Response queryType() {
        return backRestTemplate.doHttp("/selectPointConfig/queryType", null, baseServer, Response.class, HttpMethod.GET);
    }

    @Override
    public Response selectPointConfig(SelectPointConfigDto selectPointConfigDto) {
        try {
            return  backRestTemplate.doHttp("/selectPointConfig/configPoint",selectPointConfigDto,baseServer,Response.class, HttpMethod.POST);
        }catch (Exception e){
            return Response.fail();
        }
    }

    @Override
    public Response searchPointConfigStatus(String pageKey) {
        try {
            return  backRestTemplate.doHttp("/selectPointConfig/pagePointConfigStatus/".concat(pageKey),null,baseServer,Response.class, HttpMethod.GET);
        }catch (Exception e){
            return Response.fail();
        }
    }

    @Override
    public Response queryPointType(PointTypeSearchDto pointTypeSearchDto) {
        try {
            return  backRestTemplate.doHttp("/selectPointConfig/queryPointType",pointTypeSearchDto,baseServer,Response.class, HttpMethod.POST);
        }catch (Exception e){
            return Response.fail();
        }
    }
}
