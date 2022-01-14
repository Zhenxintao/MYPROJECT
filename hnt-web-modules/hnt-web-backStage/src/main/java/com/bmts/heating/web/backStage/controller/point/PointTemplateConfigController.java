package com.bmts.heating.web.backStage.controller.point;

import com.bmts.heating.commons.basement.model.db.entity.PointTemplateConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.FreezeDto;
import com.bmts.heating.commons.entiy.baseInfo.request.PointTemplateConfigDto;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.point.PointTemplateConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "模板管理")
@RestController
@RequestMapping("/pointTemplateConfig")
@Slf4j
public class PointTemplateConfigController {

    @Autowired
    private PointTemplateConfigService pointTemplateConfigService;

    @ApiOperation(value = "新增")
    @PostMapping
    public Response insert(@RequestBody PointTemplateConfig info, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) {
            info.setUserId(userId);
        }
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setCreateUser(userName);
        }
        return pointTemplateConfigService.insert(info);
    }

    @ApiOperation(value = "修改")
    @PutMapping
    public Response update(@RequestBody PointTemplateConfig info, HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setUpdateUser(userName);
        }
        return pointTemplateConfigService.update(info);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping
    public Response delete(@RequestParam int id) {
        return pointTemplateConfigService.delete(id);
    }

    @ApiOperation(value = "详情",response = PointTemplateConfig.class)
    @GetMapping
    public Response detail(@RequestParam int id) {
        return pointTemplateConfigService.detail(id);
    }

    @ApiOperation(value = "查询",response = PointTemplateConfig[].class)
    @PostMapping("/query")
    public Response query(@RequestBody PointTemplateConfigDto dto) {
        return pointTemplateConfigService.query(dto);
    }

    @ApiOperation(value = "冻结或解冻")
    @PostMapping("/freeze")
    public Response freeze(@RequestBody FreezeDto dto, HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            dto.setName(userName);
        }
        return pointTemplateConfigService.freeze(dto);
    }

}
