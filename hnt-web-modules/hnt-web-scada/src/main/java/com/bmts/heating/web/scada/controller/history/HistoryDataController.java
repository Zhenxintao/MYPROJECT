package com.bmts.heating.web.scada.controller.history;

import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.entiy.gathersearch.request.ProductReportDto;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryHistoryDataDto;
import com.bmts.heating.commons.entiy.gathersearch.request.SingleStationRepoDto;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.scada.service.history.HistoryDataService;
import com.bmts.heating.web.scada.service.history.RepoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Api(tags = "历史报表(TDENGEINE)")
@RestController
@RequestMapping("/historyData")
public class HistoryDataController {

    @Autowired
    private HistoryDataService historyDataService;

    @ApiOperation("换热站或热源历史数据信息")
    @PostMapping("/queryHistoryData")
    public Response queryHistoryData(@RequestBody QueryHistoryDataDto dto) {
        return historyDataService.queryHistoryData(dto);
    }

    @ApiOperation("获取热源基础数据全部信息")
    @GetMapping("/querySourceInfoAll")
    public Response querySourceInfoAll() {
        return historyDataService.querySourceAllInfo(-1, -1);
    }
    @ApiOperation("根据权限获取热源基础数据信息")
    @GetMapping("/querySourceInfo")
    public Response querySourceInfo(HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        return historyDataService.querySourceAllInfo(null, userId);
    }

}
