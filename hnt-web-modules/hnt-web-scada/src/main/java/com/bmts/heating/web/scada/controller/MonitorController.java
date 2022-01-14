package com.bmts.heating.web.scada.controller;

import com.bmts.heating.commons.basement.model.db.request.QueryShowPowerDto;
import com.bmts.heating.commons.entiy.baseInfo.request.monitor.*;
import com.bmts.heating.commons.entiy.baseInfo.response.CommonTree;
import com.bmts.heating.commons.entiy.gathersearch.response.cache.StationCacheRealValue;
import com.bmts.heating.commons.jwt.annotation.PassToken;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.scada.service.monitor.MonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "参数汇总")
@RequestMapping("monitor")
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    @ApiOperation(value = "超标站点查询", response = StationCacheRealValue.class)
    @PostMapping("table/beyond")
    public Response beyond(@RequestBody MonitorBeyondDto dto, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) {
            dto.setUserId(userId);
        }
        try {
            Map<String, Object> map = monitorService.getBeyond(dto);
            return Response.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.success(new String[]{});
        }
    }


    @ApiOperation(value = "多条件获取表格数据", response = StationCacheRealValue.class)
    @PostMapping("table/data")
    public Response getTableData(@RequestBody MonitorDto dto, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) dto.setUserId(userId);
        try {
            Map<String, Object> map = monitorService.getBigTable(dto);
            return Response.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.success(new String[]{});
        }
    }

    @ApiOperation(value = "站、源、公司获取表格数据", response = StationCacheRealValue.class)
    @PostMapping("table/data/single")
    public Response getTableDataSearch(@RequestBody MonitorSingleDto dto, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) dto.setUserId(userId);
        try {
            Map<String, Object> map = monitorService.getBigTable(dto);
            return Response.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.success(new String[]{});
        }

    }

    @ApiOperation(value = "获取表格-组织架构树", response = CommonTree.class)
    @PostMapping("table/edit/tree")
    public Response getStationTree(@RequestBody MonitorDto dto, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) dto.setUserId(userId);
        try {
            dto.setStatus(true);
            List<CommonTree> res = monitorService.getOrgStationTree(dto);
            return Response.success(res);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.success();
        }
    }

    @ApiOperation(value = "获取系统-组织架构树", response = CommonTree.class)
    @PostMapping("table/edit/systemTree")
    public Response getSystemTree(@RequestBody MonitorDto dto, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) dto.setUserId(userId);
        try {
            dto.setStatus(true);
            List<CommonTree> res = monitorService.getOrgSystemTree(dto);
            return Response.success(res);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.success();
        }
    }

    @ApiOperation("获取系统实时点位值")
    @PostMapping("/getSystemRealTimeData")
    public Response getSystemRealTimeData(@RequestBody QuerySystemDto dto) {
        return Response.success(monitorService.getSystemRealData(dto));
    }

    @ApiOperation("热源及换热站实时数据")
    @PostMapping("/selShowPower")
    @PassToken
    public Response selShowPower(@RequestBody QueryShowPowerDto dto) {
        return Response.success(monitorService.selShowPower(dto));
    }

    @ApiOperation("水压温度图")
    @PostMapping("/queryHydrostaticTempDiagram")
    public Response queryHydrostaticTempDiagram(@RequestBody QueryHydrostaticTempDto dto) {
        return Response.success(monitorService.queryHydrostaticTempDiagram(dto));
    }


    @ApiOperation(value = "获取热源-系统树", response = CommonTree.class)
    @PostMapping("table/edit/sourceSystemTree")
    public Response sourceSystemTree(@RequestBody MonitorDto dto, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) dto.setUserId(userId);
        try {
            dto.setStatus(true);
            List<CommonTree> res = monitorService.sourceSystemTree(dto);
            return Response.success(res);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.success();
        }
    }


}
