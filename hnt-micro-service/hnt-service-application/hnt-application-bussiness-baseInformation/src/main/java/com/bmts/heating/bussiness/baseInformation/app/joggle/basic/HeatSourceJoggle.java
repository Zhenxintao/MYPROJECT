package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.bussiness.baseInformation.app.ann.ClearCache;
import com.bmts.heating.commons.basement.model.db.entity.HeatSource;
import com.bmts.heating.commons.basement.model.db.response.HeatSourceResponse;
import com.bmts.heating.commons.basement.utils.PinYinUtils;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.db.service.HeatSourceService;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatSourceDto;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.entiy.energy.AreaManagerDto;
import com.bmts.heating.commons.redis.utils.RedisKeys;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Api(tags = "热源管理")
@RestController
@RequestMapping("/heatSource")
@Slf4j
public class HeatSourceJoggle {
    @Autowired
    HeatSourceService heatSourceService;
    @Autowired
    private AreaManagerJoggle areaManagerJoggle;

    @ApiOperation("新增")
    @PostMapping
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response insert(@RequestBody HeatSource info) {
        Response response = Response.fail();
        HeatSource one = heatSourceService.getOne(Wrappers.<HeatSource>lambdaQuery().eq(HeatSource::getName, info.getName()));
        if (one != null) {
            return Response.fail("该热源名称已存在！请更改热源名称！");
        }
        HeatSource codeOne = heatSourceService.getOne(Wrappers.<HeatSource>lambdaQuery().eq(HeatSource::getCode, info.getCode()));
        if (codeOne != null) {
            return Response.fail("该热源编号名称已存在！");
        }
        // 热源编号
        info.setSyncNumber(Integer.parseInt(info.getCode()));
        info.setCreateTime(LocalDateTime.now());
        String jP = PinYinUtils.toFirstChar(info.getName());//首字母简拼
        info.setLogogram(jP);
        if (heatSourceService.save(info)) {
            AreaManagerDto dto = new AreaManagerDto();
            dto.setLevel(TreeLevel.HeatSource.level());
            dto.setRelevanceId(info.getId());
            dto.setArea(info.getHeatArea());
            return areaManagerJoggle.update(dto);
        }
        return response;
    }

    @ApiOperation("修改")
    @PutMapping
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response update(@RequestBody HeatSource info) {
        Response response = Response.fail();
        HeatSource heatSource = heatSourceService.getById(info.getId());
        if (heatSource == null) {
            return Response.fail();
        }
        HeatSource one = heatSourceService.getOne(Wrappers.<HeatSource>lambdaQuery().eq(HeatSource::getName, info.getName()));
        if (one != null && !Objects.equals(one.getId(), info.getId())) {
            return Response.fail("该热源名称已存在！请更改热源名称！");
        }
        HeatSource codeOne = heatSourceService.getOne(Wrappers.<HeatSource>lambdaQuery().eq(HeatSource::getCode, info.getCode()));
        if (codeOne != null && !Objects.equals(codeOne.getId(), info.getId())) {
            return Response.fail("该热源编号已存在！");
        }
        // 热源编号
        info.setSyncNumber(Integer.parseInt(info.getCode()));
        String JP = PinYinUtils.toFirstChar(info.getName());//首字母简拼
        info.setLogogram(JP);
        info.setUpdateTime(LocalDateTime.now());

        if (heatSourceService.updateById(info)) {
            AreaManagerDto dto = new AreaManagerDto();
            dto.setLevel(TreeLevel.HeatSource.level());
            dto.setRelevanceId(info.getId());
            dto.setArea(info.getHeatArea());
            return areaManagerJoggle.update(dto);
        }
        return response;
    }

    @ApiOperation("删除")
    @DeleteMapping
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response delete(@RequestParam int id) {
        Response response = Response.fail();
        if (heatSourceService.removeById(id))
            return Response.success();
        return response;
    }

    @ApiOperation("详情")
    @GetMapping
    public Response detail(@RequestParam int id) {
//        HeatSource info = heatSourceService.getById(id);
//        return Response.success(info);
        QueryWrapper<HeatSourceResponse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hs.id", id);
        return Response.success(heatSourceService.queryHeatSource(new Page<HeatSourceResponse>(1, 1), queryWrapper));
    }

    @ApiOperation("查询")
    @PostMapping("/query")
    public Response query(@RequestBody HeatSourceDto dto) throws IOException, MicroException {

        QueryWrapper<HeatSourceResponse> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(dto.getKeyWord())) {
            Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher heatSource = pattern.matcher(dto.getKeyWord());//判断是否包含中文
            if (heatSource.find()) {//包含中文
                queryWrapper.like("hs.name", dto.getKeyWord());
            } else {//简拼
                queryWrapper.like("hs.logogram", dto.getKeyWord());
            }
        }
//            queryWrapper.like("hs.name", dto.getKeyWord());
        if (dto.getType() != 0 && StringUtils.isNotBlank(String.valueOf(dto.getType())))
            queryWrapper.eq("hs.type", dto.getType());
        return Response.success(heatSourceService.queryHeatSource(new Page<HeatSourceResponse>(dto.getCurrentPage(), dto.getPageCount()), queryWrapper));
    }
}
