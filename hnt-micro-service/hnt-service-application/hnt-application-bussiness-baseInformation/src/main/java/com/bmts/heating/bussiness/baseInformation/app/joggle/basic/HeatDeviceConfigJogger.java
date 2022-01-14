package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.HeatDeviceConfig;
import com.bmts.heating.commons.basement.model.db.entity.HeatSource;
import com.bmts.heating.commons.basement.model.db.entity.HeatTransferStation;
import com.bmts.heating.commons.basement.model.db.response.HeatSourceResponse;
import com.bmts.heating.commons.db.service.HeatDeviceConfigService;
import com.bmts.heating.commons.db.service.HeatSourceService;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatDeviceConfigDetail;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatDeviceConfigDto;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatDeviceConfigListDto;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.entiy.baseInfo.response.HeatDeviceConfigResponse;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Api(tags = "设备关联管理")
@RestController
@RequestMapping("/heatDeviceConfig")
public class HeatDeviceConfigJogger {

    @Autowired
    private HeatDeviceConfigService heatDeviceConfigService;

    @Autowired
    private HeatSourceService heatSourceService;


    @ApiOperation("新增")
    @PostMapping
    public Response insert(@RequestBody HeatDeviceConfigListDto dto) {
        if (!CollectionUtils.isEmpty(dto.getRelevanceId())) {
            String msg = "";
            List<HeatDeviceConfig> addList = new ArrayList<>();
            for (Integer relevanceId : dto.getRelevanceId()) {
                QueryWrapper<HeatDeviceConfig> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("relevanceId", relevanceId);
                queryWrapper.eq("type", dto.getType());
                queryWrapper.eq("deviceConfigId", dto.getDeviceConfigId());
                HeatDeviceConfig loadHeatDeviceConfig = heatDeviceConfigService.getOne(queryWrapper);
                if (loadHeatDeviceConfig != null) {
                    msg = "已经添加过的就不在重复添加了！";
                } else {
                    HeatDeviceConfig heatDeviceConfig = new HeatDeviceConfig();
                    heatDeviceConfig.setRelevanceId(relevanceId);
                    heatDeviceConfig.setDeviceConfigId(dto.getDeviceConfigId());
                    heatDeviceConfig.setType(dto.getType());
                    heatDeviceConfig.setCreateTime(LocalDateTime.now());
                    addList.add(heatDeviceConfig);
                }
            }
            if (!CollectionUtils.isEmpty(addList)) {
                if (heatDeviceConfigService.saveBatch(addList)) {
                    Response success = Response.success();
                    success.setMsg(msg);
                    return success;
                } else {
                    return Response.fail();
                }
            }
        }
        return Response.success();
    }

    @ApiOperation("删除")
    @DeleteMapping
    public Response delete(@RequestBody List<Integer> ids) {
        heatDeviceConfigService.removeByIds(ids);
        return Response.success();
    }

    @ApiOperation("修改")
    @PutMapping
    public Response update(@RequestBody HeatDeviceConfig update) {
        if (update.getId() == null) {
            return Response.fail("id不能为空");
        }
        update.setUpdateTime(LocalDateTime.now());
        return Response.success(heatDeviceConfigService.updateById(update));
    }


    @ApiOperation("详情")
    @PostMapping("/detail")
    public Response detail(@RequestBody HeatDeviceConfigDetail detail) {
        if (detail.getId() == null) {
            return Response.fail("id 不能为空！");
        }
        Page<HeatDeviceConfig> page = new Page<>(1, 1);
        QueryWrapper<HeatDeviceConfig> queryWrapper = new QueryWrapper<>();
        if (detail.getType() == 1) {
            // 查询热力站
            queryWrapper.eq("hdc.type", detail.getType());
            queryWrapper.eq("hdc.id", detail.getId());
            IPage<HeatDeviceConfigResponse> heatDeviceConfigPage = heatDeviceConfigService.pageHeatStation(page, queryWrapper);
            return Response.success(heatDeviceConfigPage.getRecords().get(0));
        }
        if (detail.getType() == 2) {
            // 表示查询热源
            queryWrapper.eq("hdc.type", detail.getType());
            queryWrapper.eq("hdc.id", detail.getId());
            IPage<HeatDeviceConfigResponse> heatDeviceConfigPage2 = heatDeviceConfigService.pageHeatSource(page, queryWrapper);
            return Response.success(heatDeviceConfigPage2.getRecords().get(0));
        }
        return Response.success();
    }


    @ApiOperation("查询")
    @PostMapping("/page")
    public Response page(@RequestBody HeatDeviceConfigDto dto) {
        Page<HeatDeviceConfig> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
        QueryWrapper<HeatDeviceConfig> queryWrapper = new QueryWrapper<>();
        if (dto.getType() == 1) {
            // 查询热力站
            queryWrapper.eq("hdc.type", dto.getType());
            if (dto.getDeviceConfigId() != 0) {
                queryWrapper.eq("hdc.deviceConfigId", dto.getDeviceConfigId());
            }
            IPage<HeatDeviceConfigResponse> heatDeviceConfigPage = heatDeviceConfigService.pageHeatStation(page, queryWrapper);
            return Response.success(heatDeviceConfigPage);
        }
        if (dto.getType() == 2) {
            // 表示查询热源
            queryWrapper.eq("hdc.type", dto.getType());
            if (dto.getDeviceConfigId() != 0) {
                queryWrapper.eq("hdc.deviceConfigId", dto.getDeviceConfigId());
            }
            IPage<HeatDeviceConfigResponse> heatDeviceConfigPage2 = heatDeviceConfigService.pageHeatSource(page, queryWrapper);
            return Response.success(heatDeviceConfigPage2);
        }
        return Response.success();
    }


    @ApiOperation("查询热源")
    @PostMapping("/sourcePage")
    public Response sourcePage(@RequestBody BaseDto dto) {
        QueryWrapper<HeatSourceResponse> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("hdc.relevanceId");
        if (StringUtils.isNotBlank(dto.getKeyWord())) {
            Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher heatSource = pattern.matcher(dto.getKeyWord());//判断是否包含中文
            if (heatSource.find()) {//包含中文
                queryWrapper.like("hs.name", dto.getKeyWord());
            } else {//简拼
                queryWrapper.like("hs.logogram", dto.getKeyWord());
            }
        }
        return Response.success(heatDeviceConfigService.heatSourcePage(new Page<HeatSource>(dto.getCurrentPage(), dto.getPageCount()), queryWrapper));
    }

    @ApiOperation("查询热力站")
    @PostMapping("/heatStationPage")
    public Response heatStationPage(@RequestBody BaseDto dto) {
        QueryWrapper<HeatTransferStation> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("hdc.relevanceId");
        if (StringUtils.isNotBlank(dto.getKeyWord())) {
            Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher heatSource = pattern.matcher(dto.getKeyWord());//判断是否包含中文
            if (heatSource.find()) {//包含中文
                queryWrapper.like("hts.name", dto.getKeyWord());
            } else {//简拼
                queryWrapper.like("hts.logogram", dto.getKeyWord());
            }
        }
        return Response.success(heatDeviceConfigService.heatStationPage(new Page<HeatTransferStation>(dto.getCurrentPage(), dto.getPageCount()), queryWrapper));
    }


}
