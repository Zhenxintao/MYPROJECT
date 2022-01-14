package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.bussiness.baseInformation.app.handler.EsHandler;
import com.bmts.heating.commons.basement.model.db.entity.EsDocConfig;
import com.bmts.heating.commons.basement.model.db.entity.EsDocConfigResponse;
import com.bmts.heating.commons.db.service.EsDocConfigService;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: EsDocConfigJoggle
 * @Description: ES 配置信息
 * @Author: pxf
 * @Date: 2021/1/15 15:13
 * @Version: 1.0
 */

@Api(tags = "ES配置信息")
@RestController
@RequestMapping("/esdoc")
@Slf4j
public class EsDocConfigJoggle {

    @Autowired
    private EsDocConfigService esDocConfigService;
    @Autowired
    private EsHandler esHandler;

    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Response page(@RequestBody BaseDto dto) {
        Page<EsDocConfigResponse> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
        QueryWrapper<Object> wrapper = new QueryWrapper<>();
        return Response.success(esDocConfigService.page(page,wrapper));
    }

    @ApiOperation("详情")
    @GetMapping
    public Response detail(@RequestParam int id) {
        return Response.success(esDocConfigService.getById(id));
    }

    @ApiOperation("修改")
    @PutMapping
    public Response update(@RequestBody EsDocConfig info) {
        EsDocConfig byId = esDocConfigService.getById(info.getId());
        if (byId == null) {
            return Response.fail("数据库不存在该条数据！");
        }
        EsDocConfig update = new EsDocConfig();
        update.setId(info.getId());
        update.setIsConverge(info.getIsConverge());
        update.setConvergeTypeDay(info.getConvergeTypeDay());
        update.setConvergeTypeHour(info.getConvergeTypeHour());
        if(esDocConfigService.updateById(update)){
            esHandler.configColumn(byId.getPointName(),byId.getDataType());
            return Response.success();
        }
        return Response.fail();
    }

    @ApiOperation("修改")
    @GetMapping("/mapping")
    public void getsdasd(){
        List<EsDocConfig> list = esDocConfigService.list();
        for (EsDocConfig e : list) {
            if (e.getIsConverge()){
                esHandler.configColumn(e.getPointName(),e.getDataType());
            }
        }
    }
}
