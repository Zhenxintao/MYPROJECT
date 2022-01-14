package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.HeatAreaChangeHistory;
import com.bmts.heating.commons.basement.model.db.entity.HeatCabinet;
import com.bmts.heating.commons.basement.model.db.entity.SystemInfo;
import com.bmts.heating.commons.basement.model.db.entity.WebPageConfig;
import com.bmts.heating.commons.db.service.HeatAreaChangeHistoryService;
import com.bmts.heating.commons.db.service.SystemInfoService;
import com.bmts.heating.commons.db.service.WebPageConfigService;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatAreaChangeDto;
import com.bmts.heating.commons.entiy.baseInfo.request.QueryAreaChangeDto;
import com.bmts.heating.commons.entiy.baseInfo.request.RepairOrderImageDto;
import com.bmts.heating.commons.entiy.baseInfo.response.HeatAreaChangeResponse;
import com.bmts.heating.commons.entiy.common.BackWebPage;
import com.bmts.heating.commons.entiy.gathersearch.request.SystemConfigDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.compute.common.ConvertArea;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Api(tags = "系统配置管理")
@RequestMapping("/systemConfig")
public class SystemConfigJoggle {
    @Autowired
    private WebPageConfigService webPageConfigService;
    @Autowired
    private SystemInfoService systemInfoService;
    @Autowired
    private HeatAreaChangeHistoryService heatAreaChangeHistoryService;

    @ApiOperation("保存数据报警设置")
    @PostMapping("/saveDataAlarmConfig")
    public Response saveDataAlarmConfig(@RequestBody WebPageConfig dto) {
        try {
            if (dto.getConfigKey().equals(BackWebPage.Tablerunconfig.value())) {
                boolean status = webPageConfigService.remove(Wrappers.<WebPageConfig>lambdaQuery().eq(WebPageConfig::getConfigKey, dto.getConfigKey()));
                if (status) {
                    return Response.success(webPageConfigService.save(dto));
                } else {
                    return Response.fail();
                }
            } else {
                WebPageConfig webPageConfig = webPageConfigService.getOne(Wrappers.<WebPageConfig>lambdaQuery().eq(WebPageConfig::getConfigKey, dto.getConfigKey()).eq(WebPageConfig::getUserId, dto.getUserId()));
                if (webPageConfig != null) {
                    boolean result = webPageConfigService.remove(Wrappers.<WebPageConfig>lambdaQuery().eq(WebPageConfig::getConfigKey, dto.getConfigKey()).eq(WebPageConfig::getUserId, dto.getUserId()));
                    if (result) {
                        return Response.success(webPageConfigService.save(dto));
                    } else {
                        return Response.fail();
                    }
                } else {
                    return Response.success(webPageConfigService.save(dto));
                }
            }
        } catch (Exception e) {
            return Response.fail();
        }


    }

    @ApiOperation("查询数据报警配置信息")
    @PostMapping("/searchDataAlarmConfig")
    public Response searchDataAlarmConfig(@RequestBody SystemConfigDto dto) {
        try {
            QueryWrapper<WebPageConfig> webPageConfigQueryWrapper = new QueryWrapper<>();
            webPageConfigQueryWrapper.in("configKey", dto.getConfigKey()).eq("userId", dto.getUserId());
            List<WebPageConfig> webPageConfigList = webPageConfigService.list(webPageConfigQueryWrapper);
            return Response.success(webPageConfigList);
        } catch (Exception e) {
            return Response.fail();
        }

    }

    @ApiOperation("查询数据表格全局抖动状态")
    @GetMapping("/tableRunConfigStatus")
    public Response tableRunConfigStatus() {
        try {
            WebPageConfig webPageConfig = webPageConfigService.getOne(Wrappers.<WebPageConfig>lambdaQuery().eq(WebPageConfig::getConfigKey, BackWebPage.Tablerunconfig.value()));
            return Response.success(webPageConfig.getJsonConfig());
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("查询数据表格状态")
    @GetMapping("/searchDataStatus")
    public Response searchDataStatus(@RequestParam Integer id) {
        try {
            List<WebPageConfig> webPageConfiglist = webPageConfigService.list(Wrappers.<WebPageConfig>lambdaQuery()
                    .eq(WebPageConfig::getConfigKey, BackWebPage.Tablerunconfigchild.value())
                    .eq(WebPageConfig::getUserId, id)
                    .or()
                    .eq(WebPageConfig::getConfigKey, BackWebPage.Tablerunconfig.value()));
            WebPageConfig webPageConfig = webPageConfiglist.stream().filter(s -> s.getConfigKey().equals(BackWebPage.Tablerunconfigchild.value())).findFirst().orElse(null);
            WebPageConfig webGetTablerunconfig = webPageConfiglist.stream().filter(s -> s.getConfigKey().equals(BackWebPage.Tablerunconfig.value())).findFirst().orElse(null);
            if (webGetTablerunconfig.getJsonConfig().equals("true")) {
                if (webPageConfig != null) {
                    return Response.success(webPageConfig.getJsonConfig());
                } else {
                    return Response.success(webGetTablerunconfig.getJsonConfig());
                }
            } else {
                return Response.success(webGetTablerunconfig.getJsonConfig());
            }

        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("系统信息上传图片")
    @PostMapping("/uploadImage")
//    @ResponseBody
    public Response uploadImage(@RequestParam MultipartFile image, RepairOrderImageDto dto) {
        try {
            List<SystemInfo> systemInfoList = systemInfoService.list();
            SystemInfo systemInfo = systemInfoList.stream().findFirst().orElse(null);
            InputStream inputStream = image.getInputStream();
            byte[] imageByte = new byte[(int) image.getSize()];
            inputStream.read(imageByte);
            if (systemInfo == null) {
                SystemInfo system = new SystemInfo();
                system.setSystemName(dto.getSystemName());
                system.setImageInfo(imageByte);
                Boolean result = systemInfoService.save(system);
                if (result)
                    return Response.success(true);
                return Response.fail();
            } else {
                SystemInfo systemUpd = new SystemInfo();
                systemUpd.setId(systemInfo.getId());
                systemUpd.setSystemName(dto.getSystemName());
                systemUpd.setImageInfo(imageByte);
                Boolean result = systemInfoService.updateById(systemUpd);
                if (result)
                    return Response.success(true);
                return Response.fail();
            }
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("系统信息图片查询")
    @GetMapping("/queryImage")
    public Response queryImage() {
        try {
            List<SystemInfo> systemInfoList = systemInfoService.list();
            SystemInfo systemInfo = systemInfoList.stream().findFirst().orElse(null);
            return Response.success(systemInfo);
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("新增面积改变信息")
    @PostMapping("/insertHeatAreaChange")
    public Response insertHeatAreaChange(@RequestBody HeatAreaChangeDto dto) {
        HeatAreaChangeHistory heatAreaChangeHistory = heatAreaChangeHistoryService.getOne(Wrappers.<HeatAreaChangeHistory>lambdaQuery().eq(HeatAreaChangeHistory::getLevel, dto.getLevel()).eq(HeatAreaChangeHistory::getRelevanceId, dto.getRelevanceId()).orderByDesc(HeatAreaChangeHistory::getCreateTime), false);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createNow = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), 0, 0);
        if (heatAreaChangeHistory != null && heatAreaChangeHistory.getCreateTime().isEqual(createNow)) {
            heatAreaChangeHistory.setNewValue(dto.getNewValue());
            boolean result = heatAreaChangeHistoryService.updateById(heatAreaChangeHistory);
            if (result)
                return Response.success();
        } else {
            HeatAreaChangeHistory h = new HeatAreaChangeHistory();
            h.setRelevanceId(dto.getRelevanceId());
            h.setLevel(dto.getLevel());
            h.setCreateTime(createNow);
            h.setStatus(dto.getStatus());
            h.setNewValue(dto.getNewValue());
            if (heatAreaChangeHistory != null) {
                h.setOldValue(heatAreaChangeHistory.getNewValue());
            } else {
                h.setOldValue(dto.getNewValue());
            }
            boolean result = heatAreaChangeHistoryService.save(h);
            if (result)
                return Response.success();
        }
        return Response.fail();
    }

    @ApiOperation("查询区间折算面积信息")
    @PostMapping("/queryHeatAreaChange")
    public List<HeatAreaChangeResponse> queryHeatAreaChange(@RequestBody QueryAreaChangeDto dto) {
        try {
            QueryWrapper<HeatAreaChangeHistory> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("createTime", dto.getStartTime());
            queryWrapper.le("createTime", dto.getEndTime());
            queryWrapper.eq("level", dto.getLevel());
            queryWrapper.in("relevanceId", dto.getRelevanceIds());
            queryWrapper.orderByAsc("createTime");
            List<HeatAreaChangeHistory> heatAreaChangeHistoryList = heatAreaChangeHistoryService.list(queryWrapper);
            List<HeatAreaChangeResponse> heatAreaChangeResponseList = new ArrayList<>();
            for (Integer id : dto.getRelevanceIds()) {
                Map<LocalDateTime, BigDecimal> areaMap = new HashMap<>();
                List<HeatAreaChangeHistory> groupAreaChangeList = heatAreaChangeHistoryList.stream().filter(s -> s.getRelevanceId() == id).collect(Collectors.toList());
                HeatAreaChangeHistory history = groupAreaChangeList.stream().findFirst().orElse(null);
                if (history.getCreateTime().isEqual(dto.getStartTime())) {
                    areaMap.put(dto.getStartTime(), history.getNewValue());
                } else if (history.getCreateTime().isBefore(dto.getStartTime())) {
                    areaMap.put(dto.getStartTime(), history.getNewValue());
                } else {
                    areaMap.put(dto.getStartTime(), history.getOldValue());
                }
                for (HeatAreaChangeHistory h : groupAreaChangeList.stream().filter(s -> s.getId() != history.getId()).collect(Collectors.toList())) {
                    areaMap.put(h.getCreateTime(), h.getNewValue());
                }
                heatAreaChangeResponseList.add(new HeatAreaChangeResponse(){{setRelevanceId(id);setAreaValue( ConvertArea.calc(areaMap, dto.getStartTime(), dto.getEndTime()));}});
            }
            return heatAreaChangeResponseList;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
