package com.bmts.heating.web.scada.controller.common;

import com.bmts.heating.commons.basement.model.db.response.PointStandardResponse;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.scada.controller.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author naming
 * @description
 * @date 2021/1/10 17:51
 **/
@Api(tags = "实时数据公用查询")
@RequestMapping("/common")
@RestController
public class CommonController extends BaseController {

    @ApiOperation(value = "根据标准点", response = PointStandardResponse.class)
    @GetMapping("/queryUnitType/{pointUnitName}/{level}")
    public Response queryUnitType(@PathVariable String pointUnitName, @PathVariable Integer level) {
        return template.doHttp("/pointStandard/queryType/".concat(pointUnitName) + "/" + level, null, baseServer, Response.class, HttpMethod.GET);
    }

    @ApiOperation("查询当前登陆用户是否禁用了报警 true为禁用 false为开启")
    @GetMapping("/queryUserAlarm")
    public Response queryUserAlarm(HttpServletRequest request) {
        return template.doHttp("/alarm/queryUserAlarm/" + JwtUtils.getUserId(request), null, gathServer, Response.class, HttpMethod.GET);
    }

    @ApiOperation("设置用户是否接收报警，true 为禁用报警 false 为启用报警")
    @GetMapping("/setUserAlarm/{status}")
    public Response setUserAlarm(@PathVariable boolean status, HttpServletRequest request) {
        return template.doHttp("/alarm/userAlarm/" + JwtUtils.getUserId(request) + "/" + status, null, gathServer, Response.class, HttpMethod.GET);
    }

    @ApiOperation(value = "查询单位分类", response = String.class)
    @GetMapping("/queryType")
    public Response queryPointUnitType() {
        return Response.success(template.doHttp("/pointUnit/queryType/", null, baseServer, String[].class, HttpMethod.GET));
    }
}
