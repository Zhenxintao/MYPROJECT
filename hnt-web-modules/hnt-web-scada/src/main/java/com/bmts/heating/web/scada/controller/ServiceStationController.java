package com.bmts.heating.web.scada.controller;

import com.bmts.heating.commons.basement.model.db.entity.ServiceStation;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.scada.service.base.ServiceStationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "客服收费")
@RestController
@RequestMapping("serviceStation")
public class ServiceStationController {
    @Autowired
    private ServiceStationService service;

    @ApiOperation(value = "查询换热站客服信息",response = ServiceStation.class)
    @GetMapping("/queryServiceStationInfo")
    public Response queryServiceStationInfo(@RequestParam String id) {
        return service.queryServiceStationInfo(id);
    }
}
