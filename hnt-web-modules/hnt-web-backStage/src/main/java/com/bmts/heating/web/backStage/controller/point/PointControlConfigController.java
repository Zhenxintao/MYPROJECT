//package com.bmts.heating.web.backStage.controller;
//
//import com.bmts.heating.commons.basement.model.db.entity.PointControlConfig;
//import com.bmts.heating.commons.entiy.baseInfo.request.PointControlConfigDto;
//import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointCollectDeleteDto;
//import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigStandardQueryDto;
//import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointControlConfigAddDto;
//import com.bmts.heating.commons.jwt.utils.JwtUtils;
//import com.bmts.heating.commons.utils.restful.Response;
//import com.bmts.heating.web.backStage.service.PointControlConfigService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//
//@Api(tags = "控制点管理")
//@RestController
//@RequestMapping("/pointControlConfig")
//public class PointControlConfigController {
//
//    @Autowired
//    private PointControlConfigService pointControlConfigService;
//
//
//    @ApiOperation("根据机组批量新增")
//    @PostMapping("/addBatch")
//    public Response addBatch(@RequestBody PointControlConfigAddDto info, HttpServletRequest request) {
//        Integer userId = JwtUtils.getUserId(request);
//        if (userId != null) {
//            info.getPointControlConfig().setUserId(userId);
//        }
//        String userName = JwtUtils.getUserName(request);
//        if (StringUtils.isNotBlank(userName)) {
//            info.getPointControlConfig().setCreateUser(userName);
//        }
//        return pointControlConfigService.addBatch(info);
//    }
//
//    @ApiOperation("批量删除")
//    @PostMapping("/deleteBatch")
//    public Response deleteBatch(@RequestBody List<PointControlConfig> pointControlConfigs) {
//        return pointControlConfigService.deleteBatch(pointControlConfigs);
//    }
//
//    @ApiOperation("单标准参量删除")
//    @PostMapping("/delete")
//    public Response delete(@RequestBody PointCollectDeleteDto param) {
//        return pointControlConfigService.delete(param);
//    }
//
//    @ApiOperation("分页查询")
//    @PostMapping("/page")
//    public Response page(@RequestBody PointControlConfigDto dto) {
//        return pointControlConfigService.page(dto);
//    }
//
//
//    @ApiOperation("修改")
//    @PutMapping
//    public Response update(@RequestBody PointControlConfig info, HttpServletRequest request) {
//        String userName = JwtUtils.getUserName(request);
//        if (StringUtils.isNotBlank(userName)) {
//            info.setUpdateUser(userName);
//        }
//        return pointControlConfigService.update(info);
//    }
//
//    @ApiOperation("详情")
//    @GetMapping("/{id}")
//    public Response detail(@PathVariable Integer id) {
//        return pointControlConfigService.detail(id);
//    }
//
//    @ApiOperation("加载未添加标准参量/带搜索")
//    @PostMapping("/none")
//    public Response loadOtherPointStandardSearch(@RequestBody PointConfigStandardQueryDto dto) {
//        return pointControlConfigService.loadOtherPointStandardSearch(dto);
//    }
//
//}
