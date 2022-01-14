package com.bmts.heating.web.backStage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.db.entity.CommonHeatSeason;
import com.bmts.heating.commons.basement.model.db.entity.CommonHeatSeasonDetail;
import com.bmts.heating.commons.entiy.baseInfo.request.CommonHeatSeasonDto;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.CommonHeatSeasonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Api(tags = "供暖季管理")
@RestController
@RequestMapping("/commonHeatSeason")
public class CommonHeatSeasonController {

    @Autowired
    private CommonHeatSeasonService commonHeatSeasonService;

    @ApiOperation(value = "新增")
    @PostMapping
    public Response insert(@RequestBody CommonHeatSeason info, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) {
            info.setUserId(userId);
        }
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setCreateUser(userName);
        }
        return commonHeatSeasonService.insert(info);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping
    public Response delete(@RequestParam int id) {
        return commonHeatSeasonService.delete(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping
    public Response update(@RequestBody CommonHeatSeason info, HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setUpdateUser(userName);
        }
        return commonHeatSeasonService.update(info);
    }

    @ApiOperation(value = "详情",response = CommonHeatSeason.class)
    @GetMapping
    public Response detail(@RequestParam int id) {
        return commonHeatSeasonService.detail(id);
    }

    @ApiOperation(value = "查询",response = CommonHeatSeason.class)
    @PostMapping("/query")
    public Response query(@RequestBody CommonHeatSeasonDto dto) {
        return commonHeatSeasonService.query(dto);
    }

    @ApiOperation(value = "供暖季详情查询",response = CommonHeatSeasonDetail[].class)
    @GetMapping("/queryDetail")
    public Response queryDetail(@RequestParam int id)
    {
       return commonHeatSeasonService.queryDetail(id);
    }

    @ApiOperation("供暖季详情通过Id查询")
    @GetMapping("/queryHeatSeasonDetailById")
    public Response queryHeatSeasonDetailById(@RequestParam int id) {
        return commonHeatSeasonService.queryHeatSeasonDetailById(id);
    }

    @ApiOperation(value = "供暖季详情添加")
    @PostMapping("/insertDetail")
    public  Response insertDetail(@RequestBody CommonHeatSeasonDetail com)
    {
        return commonHeatSeasonService.insertDetail(com);
    }

    @ApiOperation(value = "供暖季详情修改")
    @PostMapping("/updateDetail")
    public  Response updateDetail(@RequestBody CommonHeatSeasonDetail com)
    {
        return commonHeatSeasonService.updateDetail(com);
    }

    @ApiOperation(value = "供暖季详情删除")
    @GetMapping("/deleteDetail")
    public  Response deleteDetail(@RequestParam int id)
    {
        return commonHeatSeasonService.deleteDetail(id);
    }

    @ApiOperation(value = "根据主供暖季删除")
    @GetMapping("/deleteAllDetail")
    public  Response deleteAllDetail(@RequestParam int id)
    {
        return commonHeatSeasonService.deleteAllDetail(id);
    }

    @ApiOperation("查询供暖季至今天数")
    @GetMapping("/heatSeasonDayNumber")
    public Response heatSeasonDayNumber() {
        return commonHeatSeasonService.heatSeasonDayNumber();
    }
}
