//package com.bmts.heating.web.backStage.service.impl;
//
//import com.bmts.heating.commons.entiy.baseInfo.pojo.TemplateCollect;
//import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplateCollectAddDto;
//import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplateCollectDto;
//import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplateDeleteDto;
//import com.bmts.heating.commons.entiy.baseInfo.request.template.TemplateLoadStandardDto;
//import com.bmts.heating.commons.utils.restful.Response;
//import com.bmts.heating.web.backStage.service.TemplateCollectService;
//import com.bmts.heating.web.backStage.utils.BackRestTemplate;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//public class TemplateCollectImpl implements TemplateCollectService {
//
//    @Autowired
//    private BackRestTemplate tsccRestTemplate;
//
//    private final String baseServer = "bussiness_baseInfomation";
//
//
//    @Override
//    public Response page(TemplateCollectDto dto) {
//        return tsccRestTemplate.post("templateCollectJoogle/page", dto, baseServer);
//    }
//
//    @Override
//    public Response delete(int id) {
//        return tsccRestTemplate.delete("templateCollectJoogle/" + id, baseServer);
//    }
//
//
//    @Override
//    public Response loadOwnPointStandard(int id) {
//        return tsccRestTemplate.get("/templateCollectJoogle/own/" + id, baseServer);
//    }
//
//    @Override
//    public Response loadOtherPointStandard(int id) {
//        return tsccRestTemplate.get("/templateCollectJoogle/none/" + id, baseServer);
//    }
//
//    @Override
//    public Response loadOtherPointStandardSearch(TemplateLoadStandardDto dto) {
//        return tsccRestTemplate.post("/templateCollectJoogle/none", dto, baseServer);
//    }
//
//    @Override
//    public Response addPointStandard(TemplateCollectAddDto dto) {
//        return tsccRestTemplate.post("/templateCollectJoogle/addBatch", dto, baseServer);
//    }
//
//    @Override
//    public Response deletePointStandard(TemplateDeleteDto dto) {
//        return tsccRestTemplate.post("/templateCollectJoogle/deleteBatch", dto, baseServer);
//    }
//
//    @Override
//    public Response info(Integer id) {
//        return tsccRestTemplate.get("/templateCollectJoogle/" + id, baseServer);
//    }
//
//
//    @Override
//    public Response update(TemplateCollect entry) {
//        return tsccRestTemplate.put("/templateCollectJoogle", entry, baseServer);
//    }
//
//}
