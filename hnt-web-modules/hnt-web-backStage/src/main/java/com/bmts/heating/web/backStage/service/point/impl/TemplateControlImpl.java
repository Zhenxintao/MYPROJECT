//package com.bmts.heating.web.backStage.service.impl;
//
//import com.bmts.heating.commons.container.performance.config.SavantServices;
//import com.bmts.heating.commons.entiy.baseInfo.pojo.TemplateControl;
//import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplateControlAddDto;
//import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplateControlDto;
//import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplateDeleteDto;
//import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplateLoadStandardDto;
//import com.bmts.heating.commons.utils.restful.Response;
//import com.bmts.heating.web.backStage.service.TemplateControlService;
//import com.bmts.heating.web.backStage.utils.BackRestTemplate;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//@Slf4j
//@Service
//public class TemplateControlImpl extends SavantServices implements TemplateControlService {
//
//    @Autowired
//    private BackRestTemplate tsccRestTemplate;
//
//    private final String baseServer = "bussiness_baseInfomation";
//
//    @Override
//    @PostMapping("/page")
//    public Response page(@RequestBody TemplateControlDto dto) {
//        return tsccRestTemplate.post("/templateControlJoogle/page", dto, baseServer);
//    }
//
//    @Override
//    public Response loadOwnPointStandard(int id) {
//        return tsccRestTemplate.get("/templateControlJoogle/own/" + id, baseServer);
//    }
//
//    @Override
//    public Response loadOtherPointStandard(int id) {
//        return tsccRestTemplate.get("/templateControlJoogle/none/" + id, baseServer);
//    }
//
//    @Override
//    public Response loadOtherPointStandardSearch(TemplateLoadStandardDto dto) {
//        return tsccRestTemplate.post("/templateControlJoogle/none", dto, baseServer);
//    }
//
//    @Override
//    public Response addPointStandard(TemplateControlAddDto dto) {
//        return tsccRestTemplate.post("/templateControlJoogle/addBatch", dto, baseServer);
//    }
//
//    @Override
//    public Response deletePointStandard(TemplateDeleteDto dto) {
//        return tsccRestTemplate.post("/templateControlJoogle/deleteBatch", dto, baseServer);
//    }
//
//    @Override
//    public Response info(String id) {
//        return tsccRestTemplate.get("/templateControlJoogle/" + id, baseServer);
//    }
//
//    @Override
//    public Response update(TemplateControl entry) {
//        return tsccRestTemplate.put("/templateControlJoogle", entry, baseServer);
//    }
//
//    @Override
//    public Response delete(int id) {
//        return tsccRestTemplate.delete("/templateControlJoogle/" + id, baseServer);
//    }
//
//
//}
