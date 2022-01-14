package com.bmts.heating.web.backStage.controller;

import com.bmts.heating.commons.basement.model.db.entity.HeatCabinet;
import com.bmts.heating.commons.basement.model.db.response.CommonTree;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.HeatCabinetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "控制柜管理")
@RestController
@RequestMapping("/heatCabinet")
public class HeatCabinetController {

    @Autowired
    private HeatCabinetService heatCabinetService;

    @ApiOperation(value = "新增")
    @PostMapping
    public Response insert(@RequestBody HeatCabinet info, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) {
            info.setUserId(userId);
        }
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setCreateUser(userName);
        }
        return heatCabinetService.insert(info);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping
    public Response delete(@RequestParam int id) {
        return heatCabinetService.delete(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping
    public Response update(@RequestBody HeatCabinet info, HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setUpdateUser(userName);
        }
        return heatCabinetService.update(info);
    }

    @ApiOperation(value = "详情",response = HeatCabinet.class)
    @GetMapping
    public Response detail(@RequestParam int id) {
        return heatCabinetService.detail(id);
    }

    @ApiOperation(value = "查询所有控制柜",response = HeatCabinet[].class)
    @PostMapping("/query")
    public Response query() {
        return heatCabinetService.query();
    }

    @ApiOperation(value = "查询所属热力站下的控制柜",response = HeatCabinet[].class)
    @GetMapping("/queryByStationId")
    public Response queryByStationId(@RequestParam int id) {
        return heatCabinetService.queryByStationId(id);
    }

    @ApiOperation(value = "查询控热力站所有控制柜和所属机组",response = CommonTree[].class)
    @GetMapping("/queryCs")
    public Response queryByMap(@RequestParam int id) {
        return heatCabinetService.queryByMap(id);
    }


    @ApiOperation(value = "查询热源下的控制柜",response = HeatCabinet[].class)
    @GetMapping("/queryBySourceId")
    public Response queryBySourceId(@RequestParam int id) {
        return heatCabinetService.queryBySourceId(id);
    }

    @ApiOperation(value = "查询热源下所有控制柜和所属机组",response = CommonTree[].class)
    @GetMapping("/SourceCs")
    public Response SourceCs(@RequestParam int id) {
        return heatCabinetService.SourceCs(id);
    }

}
