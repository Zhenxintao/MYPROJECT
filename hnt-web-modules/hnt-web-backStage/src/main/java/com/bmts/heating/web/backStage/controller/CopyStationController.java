package com.bmts.heating.web.backStage.controller;

import com.bmts.heating.commons.entiy.baseInfo.request.copy.CopyDto;
import com.bmts.heating.commons.entiy.baseInfo.request.copy.CopyNewDto;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.CopyStationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(tags = "复制站管理")
@RestController
@RequestMapping("/copy")
public class CopyStationController {

    @Autowired
    private CopyStationService copyStationService;

    @ApiOperation(value = "复制到现有站点",response = Map.class)
    @PostMapping("/exist")
    public Response exist(@RequestBody CopyDto info, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) {
            info.setUserId(userId);
        }
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setUserName(userName);
        }
        return copyStationService.toExist(info);
    }

    @ApiOperation(value = "读取复制源控制柜及机组信息",response = Map[].class)
    @GetMapping
    public Response querySource(@RequestParam int heatStationId) {
        return copyStationService.querySource(heatStationId);
    }

    @ApiOperation(value = "复制到新建站点",response = Map.class)
    @PostMapping("/copyNew")
    public Response copyNew(@RequestBody CopyNewDto dto, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) {
            dto.setUserId(userId);
        }
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            dto.setUserName(userName);
        }
        return copyStationService.copyNew(dto);
    }


}
