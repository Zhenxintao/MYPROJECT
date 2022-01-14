//package com.bmts.heating.web.backStage.controller;
//
//import com.bmts.heating.commons.entiy.baseInfo.pojo.TemplateCollect;
//import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplateCollectAddDto;
//import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplateCollectDto;
//import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplateDeleteDto;
//import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplateLoadStandardDto;
//import com.bmts.heating.commons.jwt.utils.JwtUtils;
//import com.bmts.heating.commons.utils.restful.Response;
//import com.bmts.heating.web.backStage.service.TemplateCollectService;
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
//@Api(tags = "采集点模板管理")
//@RestController
//@RequestMapping("/templateCollectJoogle")
//@Slf4j
//public class TemplateCollectController {
//
//    @Autowired
//    private TemplateCollectService templateCollectService;
//
//    @ApiOperation("分页查询")
//    @PostMapping("/page")
//    public Response page(@RequestBody TemplateCollectDto dto) {
//        return templateCollectService.page(dto);
//    }
//
//    @ApiOperation("删除")
//    @DeleteMapping("{id}")
//    public Response delete(@PathVariable int id) {
//        return templateCollectService.delete(id);
//    }
//
//    @ApiOperation("加载已添加标准参量")
//    @GetMapping("/own/{id}")
//    public Response loadOwnPointStandard(@PathVariable int id) {
//        return templateCollectService.loadOwnPointStandard(id);
//    }
//
//    @ApiOperation("加载未添加标准参量")
//    @GetMapping("/none/{id}")
//    public Response loadOtherPointStandard(@PathVariable int id) {
//        return templateCollectService.loadOtherPointStandard(id);
//    }
//
//    @ApiOperation("加载未添加标准参量/带搜索")
//    @PostMapping("/none")
//    public Response loadOtherPointStandardSearch(@RequestBody TemplateLoadStandardDto dto) {
//        return templateCollectService.loadOtherPointStandardSearch(dto);
//    }
//
//    @ApiOperation("添加模板采集量")
//    @PostMapping("/addBatch")
//    public Response addPointStandard(@RequestBody TemplateCollectAddDto dto, HttpServletRequest request) {
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
//        Collection<TemplateCollect> templateCollects = dto.getTemplateCollects();
//        if (!CollectionUtils.isEmpty(templateCollects)) {
//            for (TemplateCollect template : templateCollects) {
//                template.setUserId(userId);
//                template.setCreateUser(userName);
//            }
//        }
//
//        return templateCollectService.addPointStandard(dto);
//    }
//
//    @ApiOperation("删除模板参量")
//    @PostMapping("/deleteBatch")
//    public Response deletePointStandard(@RequestBody TemplateDeleteDto dto) {
//        return templateCollectService.deletePointStandard(dto);
//    }
//
//    @ApiOperation("模板详情信息")
//    @GetMapping("/{id}")
//    public Response info(@PathVariable Integer id) {
//        return templateCollectService.info(id);
//    }
//
//    @ApiOperation("修改")
//    @PutMapping()
//    public Response update(@RequestBody TemplateCollect entry, HttpServletRequest request) {
//        String userName = JwtUtils.getUserName(request);
//        if (StringUtils.isNotBlank(userName)) {
//            entry.setUpdateUser(userName);
//        }
//        return templateCollectService.update(entry);
//    }
//
//}
