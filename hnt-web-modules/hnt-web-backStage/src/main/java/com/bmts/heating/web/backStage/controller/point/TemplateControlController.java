//package com.bmts.heating.web.backStage.controller;
//
//
//import com.bmts.heating.commons.entiy.baseInfo.pojo.TemplateControl;
//import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplateControlAddDto;
//import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplateControlDto;
//import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplateDeleteDto;
//import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplateLoadStandardDto;
//import com.bmts.heating.commons.jwt.utils.JwtUtils;
//import com.bmts.heating.commons.utils.restful.Response;
//import com.bmts.heating.web.backStage.service.TemplateControlService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.CollectionUtils;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Collection;
//
//@Api(tags = "控制点模板管理")
//@RestController
//@RequestMapping("/templateControlJoogle")
//@Slf4j
//public class TemplateControlController {
//    @Autowired
//    private TemplateControlService templateControlService;
//
//    @PostMapping("/page")
//    public Response page(@RequestBody TemplateControlDto dto) {
//        return templateControlService.page(dto);
//    }
//
//
//    @ApiOperation("加载已添加标准参量")
//    @GetMapping("/own/{id}")
//    public Response loadOwnPointStandard(@PathVariable int id) {
//        return templateControlService.loadOwnPointStandard(id);
//    }
//
//    @ApiOperation("加载未添加标准参量")
//    @GetMapping("/none/{id}")
//    public Response loadOtherPointStandard(@PathVariable int id) {
//        return templateControlService.loadOtherPointStandard(id);
//    }
//
//    @ApiOperation("加载未添加标准参量/带搜索")
//    @PostMapping("/none")
//    public Response loadOtherPointStandardSearch(@RequestBody TemplateLoadStandardDto dto) {
//        return templateControlService.loadOtherPointStandardSearch(dto);
//    }
//
//    @ApiOperation("添加控制量模板参量")
//    @PostMapping("/addBatch")
//    public Response addPointStandard(@RequestBody TemplateControlAddDto dto, HttpServletRequest request) {
//        Integer userId = null;
//        Integer jwtUserID = JwtUtils.getUserId(request);
//        if (jwtUserID != null) {
//            userId = jwtUserID;
//        }
//        String userName = null;
//        String jwtUserName = JwtUtils.getUserName(request);
//        if (StringUtils.isNotBlank(jwtUserName)) {
//            userName = jwtUserName;
//        }
//        Collection<TemplateControl> templateControls = dto.getTemplateControls();
//        if (!CollectionUtils.isEmpty(templateControls)) {
//            for (TemplateControl template : templateControls) {
//                template.setUserId(userId);
//                template.setCreateUser(userName);
//            }
//        }
//        return templateControlService.addPointStandard(dto);
//    }
//
//    @ApiOperation("删除模板参量")
//    @PostMapping("/deleteBatch")
//    public Response deletePointStandard(@RequestBody TemplateDeleteDto dto) {
//        return templateControlService.deletePointStandard(dto);
//    }
//
//    @ApiOperation("模板详情信息")
//    @GetMapping("/{id}")
//    public Response info(@PathVariable String id) {
//        return templateControlService.info(id);
//    }
//
//    @ApiOperation("修改")
//    @PutMapping()
//    public Response update(@RequestBody TemplateControl entry, HttpServletRequest request) {
//        String userName = JwtUtils.getUserName(request);
//        if (StringUtils.isNotBlank(userName)) {
//            entry.setUpdateUser(userName);
//        }
//        return templateControlService.update(entry);
//    }
//
//    @ApiOperation("删除")
//    @DeleteMapping("{id}")
//    public Response delete(@PathVariable int id) {
//        return templateControlService.delete(id);
//    }
//
//}
