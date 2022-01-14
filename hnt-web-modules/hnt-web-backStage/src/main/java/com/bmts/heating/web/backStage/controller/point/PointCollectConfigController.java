//package com.bmts.heating.web.backStage.controller;
//
//import com.bmts.heating.commons.basement.model.db.entity.PointCollectConfig;
//import com.bmts.heating.commons.entiy.baseInfo.request.PointCollectConfigDto;
//import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointCollectConfigAddDto;
//import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointCollectDeleteDto;
//import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointConfigStandardQueryDto;
//import com.bmts.heating.commons.jwt.utils.JwtUtils;
//import com.bmts.heating.commons.utils.restful.Response;
//import com.bmts.heating.web.backStage.service.PointCollectConfigService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//
//@Api(tags = "采集点管理")
//@RestController
//@RequestMapping("/pointCollectConfig")
//public class PointCollectConfigController {
//
//    @Autowired
//    private PointCollectConfigService pointCollectConfigService;
//
//
//    @ApiOperation("根据机组批量新增")
//    @PostMapping("/addBatch")
//    public Response addBatch(@RequestBody PointCollectConfigAddDto info, HttpServletRequest request) {
//        Integer userId = JwtUtils.getUserId(request);
//        if (userId != null) {
//            info.getPointCollectConfig().setUserId(userId);
//        }
//        String userName = JwtUtils.getUserName(request);
//        if (StringUtils.isNotBlank(userName)) {
//            info.getPointCollectConfig().setCreateUser(userName);
//        }
//        return pointCollectConfigService.addBatch(info);
//    }
//
//    @ApiOperation("批量删除")
//    @PostMapping("/deleteBatch")
//    public Response deleteBatch(@RequestBody List<PointCollectConfig> param) {
//        return pointCollectConfigService.deleteBatch(param);
//    }
//
//    @ApiOperation("单标准参量删除")
//    @PostMapping("/delete")
//    public Response delete(@RequestBody PointCollectDeleteDto param) {
//        return pointCollectConfigService.delete(param);
//    }
//
//
//    @ApiOperation("分页查询")
//    @PostMapping("/page")
//    public Response page(@RequestBody PointCollectConfigDto dto) {
//        return pointCollectConfigService.page(dto);
//    }
//
//
//    @ApiOperation("修改")
//    @PutMapping
//    public Response update(@RequestBody PointCollectConfig info, HttpServletRequest request) {
//        String userName = JwtUtils.getUserName(request);
//        if (StringUtils.isNotBlank(userName)) {
//            info.setUpdateUser(userName);
//        }
//        return pointCollectConfigService.update(info);
//    }
//
//    @ApiOperation("详情")
//    @GetMapping("/{id}")
//    public Response detail(@PathVariable Integer id) {
//        return pointCollectConfigService.detail(id);
//    }
//
//    @ApiOperation("加载未添加标准参量/带搜索")
//    @PostMapping("/none")
//    public Response loadOtherPointStandardSearch(@RequestBody PointConfigStandardQueryDto dto) {
//       return pointCollectConfigService.loadOtherPointStandardSearch(dto);
//    }
//
//}
