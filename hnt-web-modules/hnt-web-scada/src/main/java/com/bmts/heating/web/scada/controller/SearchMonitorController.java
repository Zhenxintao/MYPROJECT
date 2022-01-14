package com.bmts.heating.web.scada.controller;

import com.bmts.heating.commons.basement.model.db.entity.HeatOrganization;
import com.bmts.heating.commons.basement.model.db.response.*;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatTransferStationDto;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatTransferStationScadaDto;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardSearchDto;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.entiy.baseInfo.response.DefaultRealHeadersDto;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.scada.service.base.SearchMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Api(tags = "查询管理")
@RestController
@RequestMapping("search")
public class SearchMonitorController {

    @Autowired
    private SearchMonitorService searchMonitorService;

    @ApiOperation(value = "分页查询热力站", response = HeatTransferStationResponse.class)
    @PostMapping("/station")
    public Response station(@RequestBody HeatTransferStationScadaDto dto, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) {
            dto.setUserId(userId);
        }
        dto.setStatus(true);
        return searchMonitorService.pageStation(dto);
    }

    @ApiOperation(value = "分页查询热网", response = HeatNetResponse.class)
    @PostMapping("/heatNet")
    public Response heatNet(@RequestBody BaseDto dto) {
        return searchMonitorService.pageHeatNet(dto);
    }

    @ApiOperation(value = "查询公司", response = HeatOrganization.class)
    @GetMapping("/organization")
    public Response organization(HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        return searchMonitorService.queryAll(userId);
    }

    @ApiOperation(value = "分页查询热源", response = HeatSourceResponse.class)
    @PostMapping("/heatSource")
    public Response heatSource(@RequestBody BaseDto dto) {
        return searchMonitorService.pageHeatSource(dto);
    }

    @ApiOperation(value = "分页查询标准点表", response = PointStandardResponse.class)
    @PostMapping("/pointStandard")
    public Response pointStandard(@RequestBody PointStandardSearchDto dto) {
        return searchMonitorService.pagePointStandard(dto);
    }

    @ApiOperation(value = "标准点表查询更多", response = PointStandardResponse.class)
    @PostMapping("/moreStandard")
    public Response moreStandard(@RequestBody PointStandardSearchDto dto) {
        return searchMonitorService.moreStandard(dto);
    }

    @ApiOperation(value = "查询默认表头", response = DefaultRealHeadersDto.class)
    @PostMapping("/defaultRealHeaders/{type}")
    public Response defaultRealHeaders(@PathVariable int type) {
        return searchMonitorService.listHeaders(type);
    }


    @ApiOperation(value = "分页搜索热力站-关联控制柜", response = HeatTransferStationInfo.class)
    @PostMapping("/stationInfo")
    public Response stationInfo(@RequestBody HeatTransferStationDto dto, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) {
            dto.setUserId(userId);
        }
        dto.setStatus(true);
        return searchMonitorService.pageStationInfo(dto);
    }

}
