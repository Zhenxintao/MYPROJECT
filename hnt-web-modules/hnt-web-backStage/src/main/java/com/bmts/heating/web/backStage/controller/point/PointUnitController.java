package com.bmts.heating.web.backStage.controller.point;

import com.bmts.heating.commons.basement.model.db.entity.PointUnit;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.point.PointUnitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "单位表管理")
@RestController
@RequestMapping("/pointUnit")
@Slf4j
public class PointUnitController {

    @Autowired
    private PointUnitService pointUnitService;

    @ApiOperation(value = "新增")
    @PostMapping
    public Response insert(@RequestBody PointUnit info, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) {
            info.setUserId(userId);
        }
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setCreateUser(userName);
        }
        return pointUnitService.insert(info);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping
    public Response delete(@RequestParam int id) {
        return pointUnitService.delete(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping
    public Response update(@RequestBody PointUnit info, HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setUpdateUser(userName);
        }
        return pointUnitService.update(info);
    }

    @ApiOperation(value = "详情",response = PointUnit.class)
    @GetMapping
    public Response detail(@RequestParam int id) {
        return pointUnitService.detail(id);
    }

    @ApiOperation(value = "查询",response = PointUnit[].class)
    @PostMapping("/query")
    public Response queryByMap(@RequestBody BaseDto dto) {
        return pointUnitService.queryByMap(dto);
    }

}
