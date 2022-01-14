package com.bmts.heating.web.backStage.controller.point;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.response.PointConfigResponse;
import com.bmts.heating.commons.entiy.baseInfo.pojo.PointConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigAddDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigDeleteDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigStandardQueryDto;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.point.PointConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "配置点管理")
@RestController
@RequestMapping("/pointConfig")
public class PointConfigController {
    @Autowired
    private PointConfigService pointConfigService;


    @ApiOperation(value = "根据机组批量新增")
    @PostMapping("/addBatch")
    public Response addBatch(@RequestBody PointConfigAddDto info, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) {
            info.getPointConfig().setUserId(userId);
        }
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.getPointConfig().setCreateUser(userName);
        }
        return pointConfigService.addBatch(info);
    }

    @ApiOperation(value = "批量删除")
    @PostMapping("/deleteBatch")
    public Response deleteBatch(@RequestBody List<PointConfig> param) {
        return pointConfigService.deleteBatch(param);
    }

    @ApiOperation(value = "单标准参量删除")
    @PostMapping("/delete")
    public Response delete(@RequestBody PointConfigDeleteDto param) {
        return pointConfigService.delete(param);
    }


    @ApiOperation(value = "分页查询",response = PointConfigResponse[].class)
    @PostMapping("/page")
    public Response page(@RequestBody PointConfigDto dto) {
        return pointConfigService.page(dto);
    }


    @ApiOperation(value = "修改")
    @PutMapping
    public Response update(@RequestBody PointConfig info, HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setUpdateUser(userName);
        }
        return pointConfigService.update(info);
    }

    @ApiOperation(value = "详情",response = PointConfigResponse.class)
    @GetMapping("/{id}")
    public Response detail(@PathVariable Integer id) {
        return pointConfigService.detail(id);
    }

    @ApiOperation(value = "加载未添加标准参量/带搜索",response = PointStandard[].class)
    @PostMapping("/none")
    public Response loadOtherPointStandardSearch(@RequestBody PointConfigStandardQueryDto dto) {
        return pointConfigService.loadOtherPointStandardSearch(dto);
    }

    @ApiOperation(value = "分页查询热源配置点",response = PointConfigResponse[].class)
    @PostMapping("/pageSource")
    public Response pageSource(@RequestBody PointConfigDto dto) {
        return pointConfigService.pageSource(dto);
    }

    @ApiOperation("批量保存系统点配置")
    @PostMapping("/insertPointConfig")
    public Response insertPointConfig(@RequestBody  List<PointConfig> dto) {
       return pointConfigService.insertPointConfig(dto);
    }
    @ApiOperation("查询某系统下已配置站点信息")
    @GetMapping("/queryPointConfigExist/{id}")
    public Response queryPointConfigExist(@PathVariable Integer id)
    {
       return pointConfigService.queryPointConfigExist(id);
    }
}
