package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.bussiness.baseInformation.app.utils.WrapperSortUtils;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.basement.model.enums.DeviceType;
import com.bmts.heating.commons.db.service.CraftService;
import com.bmts.heating.commons.db.service.CraftTemplateService;
import com.bmts.heating.commons.entiy.baseInfo.request.CraftTemplateDto;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/3/22 17:26
 **/
@Api(tags = "工艺图配置")
@RestController
@RequestMapping("/craft")
@Slf4j
public class CraftJoggle {

    @Autowired
    CraftService craftService;
    @Autowired
    CraftTemplateService craftTemplateService;

    @ApiOperation(value = "配置工艺图")
    @PutMapping
    public Response config(@RequestBody Craft craft) {
        Craft info = craftService.getOne(Wrappers.<Craft>lambdaQuery().eq(Craft::getRelevanceId, craft.getRelevanceId()).eq(Craft::getType, craft.getType()));
        if (info != null) {
            info.setContent(craft.getContent());
            if (!craftService.updateById(info))
                return Response.fail("操作失败");
        } else if (!craftService.save(craft)) {
            return Response.fail("操作失败");
        }

        return Response.success();
    }

    @ApiOperation(value = "查询工艺图", response = Craft.class)
    @GetMapping("/{type}/{id}")
    public Response query(@PathVariable int type, @PathVariable int id) {
        Craft info = craftService.getOne(Wrappers.<Craft>lambdaQuery().eq(Craft::getRelevanceId, id).eq(Craft::getType, type));
        return Response.success(info);
    }


    @ApiOperation(value = "批量配置工艺图")
    @PostMapping("/configBatch")
    public Response configBatch(@RequestBody CraftBatch craftBatch) {
        if (craftBatch.getSourceId() != null) {
            /**
             * 复制模式
             */
            Craft info = craftService.getOne(Wrappers.<Craft>lambdaQuery().eq(Craft::getRelevanceId, craftBatch.getSourceId()).eq(Craft::getType, craftBatch.getType()));
            return getResponse(craftBatch, info.getContent());
        } else
            return getResponse(craftBatch, craftBatch.getContent());

    }

    private Response getResponse(@RequestBody CraftBatch craftBatch, String content) {
        craftService.remove(Wrappers.<Craft>lambdaQuery().eq(Craft::getType, craftBatch.getType()).in(Craft::getRelevanceId, craftBatch.getTargetIds()));
        List<Craft> entites = new ArrayList<>();
        craftBatch.getTargetIds().forEach(x -> {
            Craft model = new Craft();
            model.setContent(content);
            model.setRelevanceId(x);
            model.setType(craftBatch.getType());
            if (craftBatch.getLevel() != null) {
                model.setLevel(craftBatch.getLevel());
            }
            if (craftBatch.getTemplateId() != null) {
                model.setTemplateId(craftBatch.getTemplateId());
            }
            entites.add(model);
        });
        return craftService.saveBatch(entites) ? Response.success() : Response.fail("保存失败");
    }

    @ApiOperation(value = "批量删除工艺图配置")
    @DeleteMapping("/delBatch")
    public Response delBatch(@RequestBody BatchDelete batchDelete) {
        try {
            craftService.remove(Wrappers.<Craft>lambdaQuery().eq(Craft::getType, batchDelete.getType()).in(Craft::getRelevanceId, batchDelete.getIds()));
            return Response.success();
        } catch (Exception e) {
            log.error("删除工艺图失败{}", e);
            return Response.fail("删除失败");
        }

    }


    @ApiOperation("批量删除工艺图模板")
    @DeleteMapping("/template/batch")
    public Response delBatchTemplate(@RequestBody BatchDelete batchDelete) {
        try {
            if (craftTemplateService.removeByIds(batchDelete.getIds())) {
//                craftService.remove(Wrappers.<Craft>lambdaQuery().in(Craft::getTemplateId, batchDelete.getIds()));
                return Response.success();
            } else return Response.fail("删除失败");
        } catch (Exception e) {
            log.error("删除失败工艺图模板{}", e);
            return Response.fail();
        }
    }

    @ApiOperation("查询工艺图模板")
    @PostMapping("/template/query/{type}")
    public Response queryTemplates(@RequestBody BaseDto dto, @PathVariable Integer type) {
        try {
            QueryWrapper<CraftTemplate> queryWrapper = new QueryWrapper<>();
            WrapperSortUtils.sortWrapper(queryWrapper, dto);
            if (StringUtils.isNotBlank(dto.getKeyWord()))
                queryWrapper.like("name", dto.getKeyWord());
            if (type != null)
                queryWrapper.eq("type", type);
            return Response.success(craftTemplateService.page(new Page<>(dto.getCurrentPage(), dto.getPageCount()), queryWrapper));
        } catch (Exception e) {
            log.error("查询工艺图模板出错");
            return Response.success();
        }
    }

    @ApiOperation("添加模板")
    @PostMapping("/template/add")
    public Response addTemplate(@RequestBody CraftTemplate template) {
        return craftTemplateService.save(template) ? Response.success() : Response.fail();
    }

    @ApiOperation("修改模板")
    @PutMapping("/template/upt")
    public Response uptTemplate(@RequestBody CraftTemplate template) {
        if(template.getId() ==null){
            return Response.fail();
        }
        boolean updateStatus = craftTemplateService.updateById(template);
        if(updateStatus){
            // 进行应用该模板的站，源，系统都进行相应修改
            List<Craft> listCraft = craftService.list(Wrappers.<Craft>lambdaQuery().eq(Craft::getTemplateId, template.getId()));
            List<Craft> updateCraft= new ArrayList<>();
            if(StringUtils.isNotBlank(template.getContent())){
                listCraft.stream().forEach(e->{
                    Craft craft = new Craft();
                    craft.setContent(template.getContent());
                    craft.setId(e.getId());
                    updateCraft.add(craft);
                });
            }
            if(!CollectionUtils.isEmpty(updateCraft)){
                updateStatus =craftService.updateBatchById(updateCraft);
            }
            return updateStatus ? Response.success() : Response.fail();
        }else{
            return  Response.fail();
        }
    }

    @ApiOperation("批量应用模板")
    @PostMapping("/template/apply")
    public Response applyTemplate(@RequestBody CraftTemplateDto dto) {
        try {
            CraftTemplate craftTemplate = craftTemplateService.getById(dto.getTemplateId());
            List<Craft> list = new ArrayList<>();
            for (Integer relevanceId : dto.getRelevanceIds()) {
                Craft craft = new Craft();
                craft.setContent(craftTemplate.getContent());
                craft.setType(dto.getType());
                craft.setRelevanceId(relevanceId);
                craft.setLevel(dto.getLevel());
                if (dto.getTemplateId() != null) {
                    craft.setTemplateId(dto.getTemplateId());
                }
                list.add(craft);
            }
            craftService.remove(Wrappers.<Craft>lambdaQuery().eq(Craft::getType, dto.getType()).in(Craft::getRelevanceId, dto.getRelevanceIds()));
            craftService.saveBatch(list);
        } catch (Exception e) {
            log.error("批量应用模板出错");
            return Response.fail();
        }
        return Response.success();

    }

    @ApiOperation(value = "查询工艺图", response = Craft.class)
    @GetMapping("/template/{id}")
    public Response loadTemplate(@PathVariable int id) {
        CraftTemplate info = craftTemplateService.getById(id);
        return Response.success(info);
    }

    @ApiOperation(value = "查询模板工艺图的应用的所有系统", response = Craft.class)
    @GetMapping("/template/use/{level}/{id}")
    public Response queryUseTemp(@PathVariable int level,@PathVariable int id) {
        // 查询模板应用的系统id
        List<Craft> list = craftService.list(Wrappers.<Craft>lambdaQuery().eq(Craft::getTemplateId, id).eq(Craft::getLevel, level));
        return Response.success(list);
    }

}
