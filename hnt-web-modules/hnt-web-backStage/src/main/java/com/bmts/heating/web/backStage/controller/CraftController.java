package com.bmts.heating.web.backStage.controller;

import com.bmts.heating.commons.basement.model.db.entity.BatchDelete;
import com.bmts.heating.commons.basement.model.db.entity.Craft;
import com.bmts.heating.commons.basement.model.db.entity.CraftBatch;
import com.bmts.heating.commons.basement.model.db.entity.CraftTemplate;
import com.bmts.heating.commons.entiy.baseInfo.request.CraftTemplateDto;
import com.bmts.heating.commons.entiy.baseInfo.request.CraftTemplateQueryDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

/**
 * @author naming
 * @description
 * @date 2021/3/22 16:23
 **/
@Api(tags = "工艺图配置")
@RestController
@RequestMapping("/craft")
public class CraftController {
    @Autowired
    private BackRestTemplate backRestTemplate;

    private final String baseServer = "bussiness_baseInfomation";

    private final String baseUrl = "/craft";

    @ApiOperation(value = "配置工艺图")
    @PutMapping
    public Response config(@RequestBody Craft craft) {
        return backRestTemplate.doHttp(baseUrl, craft, baseServer, Response.class, HttpMethod.PUT);
    }

    @ApiOperation(value = "查询工艺图", response = Craft.class)
    @GetMapping("/{type}/{id}")
    public Response query(@PathVariable int type, @PathVariable int id) {
        return backRestTemplate.get(baseUrl + "/" + type + "/" + id, baseServer);
    }

    /**
     * 配置热力站、或热源 批量
     */
    @ApiOperation(value = "批量配置工艺图")
    @PostMapping("/configBatch")
    public Response configBatch(@RequestBody CraftBatch craftBatch) {
        if (craftBatch.getTargetIds().size() == 0) {
            return Response.fail("参数错误");
        }
        return backRestTemplate.doHttp(baseUrl.concat("/configBatch"), craftBatch, baseServer, Response.class, HttpMethod.POST);
    }

    @ApiOperation(value = "批量删除工艺图配置")
    @DeleteMapping("/delBatch")
    public Response delBatch(@RequestBody BatchDelete batchDelete) {
        if (batchDelete.getIds().size() == 0) {
            return Response.paramError();
        }
        return backRestTemplate.doHttp(baseUrl.concat("/delBatch"), batchDelete, baseServer, Response.class, HttpMethod.DELETE);
    }

    @ApiOperation("批量删除工艺图模板")
    @DeleteMapping("/template/batch")
    public Response delBatchTemplate(@RequestBody BatchDelete batchDelete) {
        if (batchDelete.getIds().size() == 0 || batchDelete.getType() == null) {
            return Response.paramError();
        }
        return backRestTemplate.doHttp(baseUrl.concat("/template/batch"), batchDelete, baseServer, Response.class, HttpMethod.DELETE);

    }

    @ApiOperation("查询工艺图模板")
    @PostMapping("/template/query/{type}")
    public Response queryTemplates(@RequestBody CraftTemplateQueryDto dto, @PathVariable Integer type) {
        return backRestTemplate.doHttp(baseUrl.concat("/template/query/"+type), dto, baseServer, Response.class, HttpMethod.POST);
    }

    @ApiOperation("添加模板")
    @PostMapping("/template/add")
    public Response addTemplate(@RequestBody CraftTemplate template) {
        return backRestTemplate.doHttp(baseUrl.concat("/template/add"), template, baseServer, Response.class, HttpMethod.POST);
    }

    @ApiOperation(value = "加载单个工艺图模板", response = CraftTemplate.class)
    @GetMapping("/template/{id}")
    public Response loadTemplate(@PathVariable int id) {
        return backRestTemplate.get(baseUrl + "/template/" + id, baseServer);
    }

    @ApiOperation("修改模板")
    @PutMapping("/template/upt")
    public Response uptTemplate(@RequestBody CraftTemplate template) {
        return backRestTemplate.doHttp(baseUrl.concat("/template/upt"), template, baseServer, Response.class, HttpMethod.PUT);
    }

    @ApiOperation("批量应用模板")
    @PostMapping("/template/apply")
    public Response applyTemplate(@RequestBody CraftTemplateDto dto) {
        return backRestTemplate.doHttp(baseUrl.concat("/template/apply"), dto, baseServer, Response.class, HttpMethod.POST);

    }

    @ApiOperation(value = "查询模板工艺图的应用的所有系统", response = CraftTemplate.class)
    @GetMapping("/template/use/{level}/{id}")
    public Response queryUseTemp(@PathVariable int level,@PathVariable int id) {
        return backRestTemplate.get(baseUrl + "/template/use/" +level+"/"+ id, baseServer);
    }
}
