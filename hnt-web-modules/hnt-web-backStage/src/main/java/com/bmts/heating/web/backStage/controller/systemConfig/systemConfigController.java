package com.bmts.heating.web.backStage.controller.systemConfig;

import com.auth0.jwt.JWT;
import com.bmts.heating.commons.basement.model.db.entity.WebPageConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.RepairOrderImageDto;
import com.bmts.heating.commons.entiy.gathersearch.request.SystemConfigDto;
import com.bmts.heating.commons.jwt.annotation.PassToken;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.systemConfig.SystemConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Api(tags = "系统配置管理")
@RestController
@RequestMapping("systemConfig")
public class systemConfigController {
    @Autowired
    private SystemConfigService systemConfigService;

    @PostMapping("/saveDataAlarmConfig")
    @ApiOperation("保存数据报警设置")
    public Response saveDataAlarmConfig(@RequestBody WebPageConfig dto, HttpServletRequest request) {
        Integer getId = JwtUtils.getUserId(request);
        dto.setUserId(getId);
        return systemConfigService.saveDataAlarmConfig(dto);
    }

    @PostMapping("/searchDataAlarmConfig")
    @ApiOperation("保存数据报警查询")
    public Response searchDataAlarmConfig(@RequestBody SystemConfigDto dto, HttpServletRequest request) {
        Integer getId = JwtUtils.getUserId(request);
        dto.setUserId(getId);
        return systemConfigService.searchDataAlarmConfig(dto);
    }

    @ApiOperation("查询数据表格状态")
    @GetMapping("/searchDataStatus")
    public Response searchDataStatus(HttpServletRequest request) {
        Integer getId = JwtUtils.getUserId(request);
        return systemConfigService.searchDataStatus(getId);
    }

    @ApiOperation("查询数据表格全局抖动状态")
    @GetMapping("/tableRunConfigStatus")
    public Response tableRunConfigStatus() {
        return systemConfigService.tableRunConfigStatus();
    }

    @ApiOperation("系统信息Logo上传")
    @PostMapping("/uploadImage")
//  @ResponseBody
    public Response uploadImage(@RequestParam MultipartFile image,@RequestParam String systemName) {
        RepairOrderImageDto dto = new RepairOrderImageDto();
        dto.setSystemName(systemName);
        return systemConfigService.uploadImage(image,dto);
    }

    @ApiOperation("系统信息Logo查询")
    @GetMapping("/queryImage")
    @PassToken
    public Response queryImage() {
        return systemConfigService.queryImage();
    }

}
