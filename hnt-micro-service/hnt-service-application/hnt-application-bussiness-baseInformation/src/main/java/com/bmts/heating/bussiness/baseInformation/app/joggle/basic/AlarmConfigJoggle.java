package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.bussiness.baseInformation.app.ann.ClearCache;
import com.bmts.heating.commons.basement.model.db.entity.AlarmConfig;
import com.bmts.heating.commons.db.service.AlarmConfigService;
import com.bmts.heating.commons.entiy.baseInfo.request.AlarmConfigDto;
import com.bmts.heating.commons.redis.utils.RedisKeys;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Api(tags = "报警配置管理")
@RestController
@RequestMapping("/alarmConfig")
@Slf4j
public class AlarmConfigJoggle {

    @Autowired
    AlarmConfigService alarmConfigService;

    @ApiOperation("新增")
    @PostMapping
    public Response insert(@RequestBody AlarmConfig info) {
        Response response = Response.fail();
        info.setCreateTime(LocalDateTime.now());
        if (alarmConfigService.save(info))
            return Response.success();
        return response;
    }

    @ApiOperation("删除")
    @DeleteMapping
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response delete(@RequestParam int id) {
        Response response = Response.fail();
        QueryWrapper<AlarmConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        if (alarmConfigService.removeById(id)) {
            return Response.success();
        }
        return response;
    }

    @ApiOperation("修改")
    @PutMapping
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response update(@RequestBody AlarmConfig info) {
        Response response = Response.fail();
        AlarmConfig alarmConfig = alarmConfigService.getById(info.getId());
        if (alarmConfig == null)
            return Response.fail();
        info.setUpdateTime(LocalDateTime.now());
        if (alarmConfigService.updateById(info))
            return Response.success();
        return response;
    }

    @ApiOperation("详情")
    @GetMapping
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response detail(@RequestParam int id) {
        AlarmConfig info = alarmConfigService.getById(id);
        return Response.success(info);
    }

    @ApiOperation("查询")
    @PostMapping("/query")
    public Response queryByMap(@RequestBody AlarmConfigDto dto) {
        try {
            QueryWrapper<AlarmConfig> queryWrapper = new QueryWrapper<>();
            if(StringUtils.isNotBlank(dto.getKeyWord()))
            {
                queryWrapper.like("name",dto.getKeyWord());
            }
            Page<AlarmConfig> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
            Page<AlarmConfig> pages = alarmConfigService.page(page, queryWrapper);
            return Response.success(pages);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail();
        }
    }
}
