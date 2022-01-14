package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.db.entity.DefaultRealHeaders;
import com.bmts.heating.commons.db.service.DefaultRealHeadersService;
import com.bmts.heating.commons.entiy.baseInfo.response.DefaultRealHeadersDto;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "表头管理")
@RestController
@RequestMapping("/defaultRealHeaders")
public class DefaultRealHeadersJogger {

    @Autowired
    private DefaultRealHeadersService defaultRealHeadersService;


    @ApiOperation("查询")
    @GetMapping("/query/{type}")
    public Response query(@PathVariable int type) {
        QueryWrapper<DefaultRealHeadersDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("drh.type", type);
        return Response.success(defaultRealHeadersService.listHeaders(queryWrapper));
    }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Response add(@RequestBody DefaultRealHeaders param) {
        if (param.getType() == null || param.getPointStandardId() == null) {
            return Response.fail("参数错误！");
        }
        // 处理重复
        QueryWrapper<DefaultRealHeaders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pointStandardId", param.getPointStandardId());
        queryWrapper.eq("type", param.getType());
        DefaultRealHeaders one = defaultRealHeadersService.getOne(queryWrapper);
        if (one != null) {
            return Response.fail("该参量表头已经存在！不能重复添加！");
        }
        return defaultRealHeadersService.save(param) ? Response.success() : Response.fail("添加失败！");
    }


    @ApiOperation("批量-添加")
    @PostMapping("/addBatch")
    public Response addBatch(@RequestBody List<DefaultRealHeaders> param) {
        if (CollectionUtils.isEmpty(param)) {
            return Response.fail("参数为空！");
        }
        List<DefaultRealHeaders> listAdd = new ArrayList<>();
        List<DefaultRealHeaders> listFail = new ArrayList<>();
        for (DefaultRealHeaders headers : param) {
            if (headers.getPointStandardId() == null | headers.getType() == null) {
                listFail.add(headers);
            } else {
                listAdd.add(headers);
            }
        }
        Map<Integer, List<DefaultRealHeaders>> collect = listAdd.stream().collect(Collectors.groupingBy(e -> e.getType()));
        boolean bool = false;
        for (Integer type : collect.keySet()) {
            List<DefaultRealHeaders> defaultRealHeaders = collect.get(type);
            // 删除原来的数据
            QueryWrapper<DefaultRealHeaders> delWrapper = new QueryWrapper<>();
            delWrapper.eq("type", type);
            defaultRealHeadersService.remove(delWrapper);
            if (!CollectionUtils.isEmpty(defaultRealHeaders)) {
                bool = defaultRealHeadersService.saveBatch(defaultRealHeaders);
            }
        }
        if (bool) {
            return Response.success(listFail);
        } else {
            return Response.fail("批量添加失败！");
        }
    }


    @ApiOperation("修改")
    @PutMapping
    public Response update(@RequestBody DefaultRealHeaders param) {
        if (param.getId() == null) {
            return Response.fail("参数为空！");
        }
        DefaultRealHeaders byId = defaultRealHeadersService.getById(param.getId());
        if (byId == null) {
            return Response.fail("没有查询到数据！");
        }
        return defaultRealHeadersService.updateById(param) ? Response.success() : Response.fail("修改失败！");
    }

    @ApiOperation("删除")
    @DeleteMapping
    public Response delete(@RequestParam int id) {
        if (defaultRealHeadersService.removeById(id)) {
            return Response.success();
        }
        return Response.fail();
    }


}
