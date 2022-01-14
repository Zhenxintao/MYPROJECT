package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.bussiness.baseInformation.app.ann.ClearCache;
import com.bmts.heating.commons.basement.model.db.entity.HeatNet;
import com.bmts.heating.commons.basement.model.db.response.HeatNetResponse;
import com.bmts.heating.commons.basement.utils.PinYinUtils;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.db.mapper.HeatNetMapper;
import com.bmts.heating.commons.db.service.HeatNetService;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatNetDto;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.entiy.energy.AreaManagerDto;
import com.bmts.heating.commons.redis.utils.RedisKeys;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Api(tags = "热网管理")
@RestController
@RequestMapping("/heatNet")
@Slf4j
public class HeatNetJoggle {
    @Autowired
    HeatNetService heatNetService;
    @Autowired
    HeatNetMapper heatNetMapper;
    @Autowired
    private AreaManagerJoggle areaManagerJoggle;

    @ApiOperation("新增")
    @PostMapping
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response insert(@RequestBody HeatNet info) {
        Response response = Response.fail();
        info.setCreateTime(LocalDateTime.now());
        //首字母简拼
        String JP = PinYinUtils.toFirstChar(info.getName());
        info.setLogogram(JP);

        HeatNet one = heatNetService.getOne(Wrappers.<HeatNet>lambdaQuery().eq(HeatNet::getName, info.getName()));
        if (one != null) {
            return Response.fail("该名称已存在！");
        }
        HeatNet codeOne = heatNetService.getOne(Wrappers.<HeatNet>lambdaQuery().eq(HeatNet::getCode, info.getCode()));
        if (codeOne != null) {
            return Response.fail("该热网编号已存在！");
        }
        // 热网编号
        info.setSyncNumber(Integer.parseInt(info.getCode()));
        if (heatNetService.save(info)) {
            AreaManagerDto dto = new AreaManagerDto();
            dto.setLevel(TreeLevel.HeatNet.level());
            dto.setRelevanceId(info.getId());
            dto.setArea(info.getHeatArea());
            return areaManagerJoggle.update(dto);
//            return Response.success();
        }

        return response;
    }

    @ApiOperation("修改")
    @PutMapping
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response update(@RequestBody HeatNet info) {
        Response response = Response.fail();
        HeatNet heatNet = heatNetService.getById(info.getId());
        if (heatNet == null) {
            return Response.fail();
        }
        HeatNet one = heatNetService.getOne(Wrappers.<HeatNet>lambdaQuery().eq(HeatNet::getName, info.getName()));
        if (one != null && !Objects.equals(one.getId(), info.getId())) {
            return Response.fail("该名称已存在！");
        }
        HeatNet codeOne = heatNetService.getOne(Wrappers.<HeatNet>lambdaQuery().eq(HeatNet::getCode, info.getCode()));
        if (codeOne != null && !Objects.equals(codeOne.getId(), info.getId())) {
            return Response.fail("该热网编号已存在！");
        }
        String JP = PinYinUtils.toFirstChar(info.getName());//首字母简拼
        info.setLogogram(JP);
        info.setUpdateTime(LocalDateTime.now());
        // 热网编号
        info.setSyncNumber(Integer.parseInt(info.getCode()));
        if (heatNetService.updateById(info)) {
            AreaManagerDto dto = new AreaManagerDto();
            dto.setLevel(TreeLevel.HeatNet.level());
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
        if (heatNetService.removeById(id))
            return Response.success();
        return response;
    }

    @ApiOperation("详情")
    @GetMapping
    public Response detail(@RequestParam int id) {
//        HeatNet info = heatNetService.getById(id);
//        return Response.success(info);
        QueryWrapper<HeatNetResponse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hn.id", id);
        Page<HeatNetResponse> heatNetResponsePage = heatNetMapper.queryHeatNet(new Page<HeatNetResponse>(1, 1), queryWrapper);
        List<HeatNetResponse> recordList = heatNetResponsePage.getRecords();
        HeatNetResponse response = null;
        if (!CollectionUtils.isEmpty(recordList)) {
            response = recordList.get(0);
        }
        return Response.success(response);
    }

    @ApiOperation("查询")
    @PostMapping("/query")
    public Response query(@RequestBody HeatNetDto dto) throws MicroException {

        Response response = Response.fail();
        try {
            QueryWrapper<HeatNetResponse> queryWrapper = new QueryWrapper<>();
//            WapperSortUtils.sortWrapper(queryWrapper, dto);
            if (StringUtils.isNotBlank(dto.getKeyWord())) {
                Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
                Matcher heatNet = pattern.matcher(dto.getKeyWord());/**判断是否包含中文*/
                if (heatNet.find()) {/**包含中文*/
                    queryWrapper.like("hn.name", dto.getKeyWord());
                } else {/**简拼*/
                    queryWrapper.like("hn.logogram", dto.getKeyWord());
                }
            }
            if (dto.getType() != 0 && StringUtils.isNotBlank(String.valueOf(dto.getType())))
                queryWrapper.eq("hn.type", dto.getType());
            return Response.success(heatNetMapper.queryHeatNet(new Page<>(dto.getCurrentPage(), dto.getPageCount()), queryWrapper));

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return response;
        }
    }

}
