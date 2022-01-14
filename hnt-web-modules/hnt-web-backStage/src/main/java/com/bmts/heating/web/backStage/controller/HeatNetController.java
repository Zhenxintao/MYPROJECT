package com.bmts.heating.web.backStage.controller;

import com.bmts.heating.commons.basement.model.db.entity.HeatNet;
import com.bmts.heating.commons.basement.model.db.response.HeatNetResponse;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatNetDto;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.HeatNetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "热网管理")
@RestController
@RequestMapping("/heatNet")
public class HeatNetController {

    @Autowired
    private HeatNetService heatNetService;

    @ApiOperation(value = "新增")
    @PostMapping
    public Response insert(@RequestBody HeatNet info, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) {
            info.setUserId(userId);
        }
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setCreateUser(userName);
        }
        return heatNetService.insert(info);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping
    public Response delete(@RequestParam int id) {
        return heatNetService.delete(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping
    public Response update(@RequestBody HeatNet info, HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setUpdateUser(userName);
        }
        return heatNetService.update(info);
    }

    @ApiOperation(value = "详情",response = HeatNetResponse.class)
    @GetMapping
    public Response detail(@RequestParam int id) {
        return heatNetService.detail(id);
    }

    @ApiOperation(value = "查询",response = HeatNetResponse[].class)
    @PostMapping("/query")
    public Response query(@RequestBody HeatNetDto dto) {
        return heatNetService.query(dto);
    }

}
