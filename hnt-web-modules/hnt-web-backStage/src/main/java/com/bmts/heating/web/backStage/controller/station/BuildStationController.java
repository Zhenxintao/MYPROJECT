package com.bmts.heating.web.backStage.controller.station;

import com.bmts.heating.commons.basement.model.db.response.CommonTree;
import com.bmts.heating.commons.entiy.baseInfo.request.station.BuildStationDto;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.station.BuildStationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "配置站点")
@Slf4j
@RequestMapping("/configStation")
public class BuildStationController {
    @Autowired
    private BuildStationService buildStationService;

    @ApiOperation(value = "配置站点")
    @PostMapping
    public Response configStation(@RequestBody BuildStationDto station, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) {
            station.setUserId(userId);
        }
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            station.setCreateUser(userName);
        }
        return buildStationService.configStation(station);

    }


    @ApiOperation(value = "查询模板 tree" ,response = CommonTree[].class)
    @PostMapping("/queryTeplateTree")
    public Response queryTemplateTree() {
        return buildStationService.queryTemplateTree();
    }
}





















