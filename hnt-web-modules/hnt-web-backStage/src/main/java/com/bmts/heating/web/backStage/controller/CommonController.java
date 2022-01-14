package com.bmts.heating.web.backStage.controller;

import com.bmts.heating.commons.basement.model.db.response.PointStandardResponse;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author naming
 * @description
 * @date 2021/8/19 10:35
 **/

@Api(tags = "实时数据公用查询")
@RequestMapping("/common")
@RestController
public class CommonController {
    @Autowired
    private BackRestTemplate template;
    private final String baseServer = "bussiness_baseInfomation";

    @ApiOperation(value = "根据标准点", response = PointStandardResponse.class)
    @GetMapping("/queryUnitType/{pointUnitName}/{level}")
    public Response queryUnitType(@PathVariable String pointUnitName, @PathVariable Integer level) {
        return template.doHttp("/pointStandard/queryType/".concat(pointUnitName) + "/" + level, null, baseServer, Response.class, HttpMethod.GET);
    }


    @ApiOperation(value = "查询单位分类", response = String.class)
    @GetMapping("/queryType")
    public Response queryPointUnitType() {
        return Response.success(template.doHttp("/pointUnit/queryType/", null, baseServer, String[].class, HttpMethod.GET));
    }
}

