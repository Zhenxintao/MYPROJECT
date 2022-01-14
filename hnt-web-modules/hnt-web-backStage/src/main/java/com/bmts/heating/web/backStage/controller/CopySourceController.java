package com.bmts.heating.web.backStage.controller;

import com.bmts.heating.commons.entiy.baseInfo.request.copy.CopyDto;
import com.bmts.heating.commons.entiy.baseInfo.request.copy.CopyHeatSourceDto;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.CopySourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(tags = "复制热源管理")
@RestController
@RequestMapping("/copySource")
public class CopySourceController {

    @Autowired
    private CopySourceService copySourceService;

    @ApiOperation(value = "复制到现有热源",response = Map.class)
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
        return copySourceService.toExist(info);
    }


    @ApiOperation(value = "复制到新建热源",response = Map.class)
    @PostMapping("/copyNew")
    public Response copyNew(@RequestBody CopyHeatSourceDto dto, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) {
            dto.setUserId(userId);
        }
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            dto.setUserName(userName);
        }
        return copySourceService.copyNew(dto);
    }


    @ApiOperation(value = "读取复制源控制柜及机组信息",response = Map[].class)
    @GetMapping
    public Response querySource(@RequestParam int sourceId) {
        return copySourceService.querySource(sourceId);
    }


}
