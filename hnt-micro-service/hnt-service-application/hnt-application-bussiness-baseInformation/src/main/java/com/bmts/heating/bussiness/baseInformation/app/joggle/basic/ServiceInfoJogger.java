package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.alibaba.fastjson.JSON;
import com.bmts.heating.commons.basement.model.db.response.ServiceInfoResponse;
import com.bmts.heating.commons.db.mapper.ServiceStationMapper;
import com.bmts.heating.commons.entiy.baseInfo.request.ServiceInfoDto;
import com.bmts.heating.commons.entiy.baseInfo.response.ServiceChargeResponse;
import com.bmts.heating.commons.entiy.baseInfo.response.ServiceComplaintResponse;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Api(tags = "客服收费信息")
@RestController
@RequestMapping("/serviceInfo")
@Slf4j
public class ServiceInfoJogger {
    @Autowired
    private TSCCRestTemplate template;
    String serviceComplaintUrl = "http://100.1.2.5:9918";

    @ApiOperation("查询换热站客服信息")
    @GetMapping("/queryServiceStationInfo")
    public Response queryServiceStationInfo(@RequestParam String id) {
        ServiceInfoResponse serviceInfoResponse = new ServiceInfoResponse();
        List<ServiceComplaintResponse> serviceComplaintResponseList = serviceComplaintResponse();
        ServiceComplaintResponse serviceComplaintResponse = serviceComplaintResponseList.stream().filter(s -> Objects.equals(s.getId(),id)).findFirst().orElse(null);
        if (serviceComplaintResponse != null) {
            serviceInfoResponse.setComplaintRate(serviceComplaintResponse.getRate());
        }
        List<ServiceChargeResponse> serviceChargeResponseList = serviceChargeResponse();
        ServiceChargeResponse serviceChargeResponse = serviceChargeResponseList.stream().filter(s -> Objects.equals(s.getId(),id)).findFirst().orElse(null);
        if (serviceChargeResponse != null) {
            serviceInfoResponse.setChargeRate(serviceChargeResponse.getRate());
        }
        return Response.success(serviceInfoResponse);
    }

    public List<ServiceComplaintResponse> serviceComplaintResponse() {
        try {
            ServiceInfoDto serviceInfoDto = new ServiceInfoDto() {{
                setKey(8);
                setUsername("bmts");
                setPwd("bmts2021");
            }};
            Response response = template.postBalance(serviceComplaintUrl, "/LeadCockpitUtil/getSourceData", serviceInfoDto, Response.class);
            if (response.getCode() != 200) {
                System.out.println("调取华夏客服投诉率接口返回失败！");
                return null;
            } else {
                List<ServiceComplaintResponse> serviceComplaintResponseList = JSON.parseArray(JSON.toJSONString(response.getData()), ServiceComplaintResponse.class);
                return serviceComplaintResponseList;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<ServiceChargeResponse> serviceChargeResponse() {
        try {
            ServiceInfoDto serviceInfoDto = new ServiceInfoDto() {{
                setKey(9);
                setUsername("bmts");
                setPwd("bmts2021");
            }};
            Response response = template.postBalance(serviceComplaintUrl, "/LeadCockpitUtil/getSourceData", serviceInfoDto, Response.class);
            if (response.getCode() != 200) {
                System.out.println("调取华夏客服收费率接口返回失败！");
                return null;
            } else {
                List<ServiceChargeResponse> serviceChargeResponseList = JSON.parseArray(JSON.toJSONString(response.getData()), ServiceChargeResponse.class);
                return serviceChargeResponseList;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}
