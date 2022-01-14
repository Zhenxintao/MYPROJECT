package com.bmts.heating.web.scada.controller.source;

import com.bmts.heating.commons.entiy.baseInfo.request.HeatSourceTableDto;
import com.bmts.heating.commons.entiy.gathersearch.response.cache.SourceCacheRealValue;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.scada.service.monitor.SourceMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@Api(tags = "参数汇总-热源")
@RequestMapping("monitor/source")
public class SourceMonitorController {

    @Autowired
    private SourceMonitorService sourceMonitorService;

    @ApiOperation(value = "表格", response = SourceCacheRealValue.class)
    @PostMapping("/table")
    public Response table(@RequestBody HeatSourceTableDto dto, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        return Response.success(sourceMonitorService.table(dto,userId));
    }
}
