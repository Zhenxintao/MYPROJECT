package com.bmts.heating.bussiness.baseInformation.app.joggle.station;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.bussiness.baseInformation.app.ann.ClearCache;
import com.bmts.heating.bussiness.baseInformation.app.handler.EsHandler;
import com.bmts.heating.bussiness.baseInformation.app.joggle.basic.AreaManagerJoggle;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.basement.model.db.response.CommonTree;
import com.bmts.heating.commons.basement.model.db.response.template.TemplatePointResponse;
import com.bmts.heating.commons.basement.utils.MapperUtils;
import com.bmts.heating.commons.basement.utils.PinYinUtils;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.db.mapper.TemplatePointMapper;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.entiy.baseInfo.request.station.BuildCollectPoint;
import com.bmts.heating.commons.entiy.baseInfo.request.station.BuildStationDto;
import com.bmts.heating.commons.entiy.common.PointProperties;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.entiy.energy.AreaManagerDto;
import com.bmts.heating.commons.redis.service.RedisPointService;
import com.bmts.heating.commons.redis.utils.RedisKeys;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName BuildStationJoggle
 * @Author naming
 * @Date 2020/11/21 11:04
 **/
@RestController
@Api(tags = "配置站点")
@Slf4j
@RequestMapping("/configStation")
public class BuildStationJoggle {

    @Autowired
    PointTemplateConfigService pointTemplateConfigService;
    @Autowired
    TemplatePointMapper templatePointMapper;
    @Autowired
    HeatTransferStationService heatTransferStationService;
    @Autowired
    HeatCabinetService heatCabinetService;
    @Autowired
    HeatSystemService heatSystemService;
    @Autowired
    PointConfigService pointConfigService;
    @Autowired
    RedisPointService redisPointService;
    @Autowired
    EsHandler esHandler;
    @Autowired
    private AreaManagerJoggle areaManagerJoggle;

    @Autowired
    private PointStandardService pointStandardService;

    @ApiOperation("配置站点")
    @PostMapping
    @ClearCache(key = RedisKeys.FIRST_BASE)
    public Response configStation(@RequestBody BuildStationDto station) {

        HeatTransferStation heatTransferStation = new HeatTransferStation();
        try {
            MapperUtils.copyProperties(station, heatTransferStation);
            //首字母简拼
            String JP = PinYinUtils.toFirstChar(heatTransferStation.getName());
            heatTransferStation.setLogogram(JP);
            heatTransferStation.setCreateTime(LocalDateTime.now());
            heatTransferStationService.save(heatTransferStation);

            AreaManagerDto dto = new AreaManagerDto();
            dto.setLevel(TreeLevel.HeatStation.level());
            dto.setRelevanceId(heatTransferStation.getId());
            dto.setArea(heatTransferStation.getHeatArea());
            areaManagerJoggle.update(dto);

            // 插入控制柜
//            station.getCabinets().forEach(x -> {
//                HeatCabinet heatCabinet = new HeatCabinet();
//                MapperUtils.copyProperties(x, heatCabinet);
//                heatCabinet.setHeatTransferStationId(heatTransferStation.getId());
//                heatCabinet.setCreateUser(station.getCreateUser());
//                heatCabinet.setUserId(station.getUserId());
//                heatCabinet.setCreateTime(LocalDateTime.now());
//                heatCabinetService.save(heatCabinet);
//                //插入机组
//                x.getBuildSystems().forEach(system -> {
//                    system.setHeatCabinetId(heatCabinet.getId());
//                    HeatSystem heatSystem = new HeatSystem();
//                    MapperUtils.copyProperties(system, heatSystem);
//                    heatSystem.setHeatCabinetId(heatCabinet.getId());
//                    heatSystem.setUserId(station.getUserId());
//                    heatSystem.setCreateUser(station.getCreateUser());
//                    heatSystem.setCreateTime(LocalDateTime.now());
//                    heatSystemService.save(heatSystem);
//                    //更新面积
//                    dto.setLevel(TreeLevel.HeatSystem.level());
//                    dto.setRelevanceId(heatSystem.getId());
//                    dto.setArea(heatSystem.getHeatArea());
//                    areaManagerJoggle.update(dto);
//
//                    //采集量
//                    List<BuildCollectPoint> buildCollectPoints = system.getNetFlagCollect().getFirst();
//                    buildCollectPoints.addAll(system.getNetFlagCollect().getTwo());
//                    buildCollectPoints.addAll(system.getNetFlagCollect().getCommon());
//                    List<PointConfig> pointCollectConfigs = new ArrayList<>(buildCollectPoints.size());
//                    buildCollectPoints.forEach(point -> {
//                        PointConfig info = new PointConfig();
//                        MapperUtils.copyProperties(point, info);
//                        info.setRelevanceId(heatSystem.getId());
//                        info.setCreateTime(LocalDateTime.now());
////                        info.setUserId(station.getUserId());
//                        info.setCreateUser(station.getCreateUser());
//                        info.setLevel(TreeLevel.HeatSystem.level());
//                        pointCollectConfigs.add(info);
//                    });
//                    pointConfigService.saveBatch(pointCollectConfigs);
//                    buildCollectPoints.forEach(config -> {
//                        PointStandard byId = pointStandardService.getById(config.getPointStandardId());
//                        if (byId == null) {
//                            esHandler.configColumn(config.getPointStandardId(), byId.getDataType());
//                        }
//                    });
//                    redisPointService.saveOrUpdateByrelevanceId(heatSystem.getId(), TreeLevel.HeatSystem.level());
//                });
//            });
            return Response.success();
        } catch (Exception e) {
            log.error("config station cause exception {}", e);
            return Response.fail();
        }


    }


    @ApiOperation("查询模板 tree")
    @PostMapping("/queryTeplateTree")
    public Response queryTemplateTree() throws IOException, MicroException {

        QueryWrapper<PointTemplateConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PointTemplateConfig::getDeleteFlag, false).and(x -> x.like(PointTemplateConfig::getTemplateKey, "Station")).eq(PointTemplateConfig::getStatus, true);
        List<PointTemplateConfig> list = pointTemplateConfigService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Response.success("");
        }

        List<TemplatePointResponse> templateCollectResponses = templatePointMapper.queryPointTemplate(new QueryWrapper());
        List<CommonTree> result = new ArrayList<>();
        for (PointTemplateConfig pointTemplateConfig : list) {
            String id = "templateId_".concat(pointTemplateConfig.getId().toString());
            CommonTree template =
                    new CommonTree(id, "0", 1, "", pointTemplateConfig.getTemplateValue());

            result.add(template);
            //采集量层
            String collectId = "collect_".concat(pointTemplateConfig.getId().toString());
            CommonTree collectTemplate = new CommonTree(collectId, id, 2, "", "采集量");
            result.add(collectTemplate);

            //控制量层
            String controlId = "control_".concat(pointTemplateConfig.getId().toString());
            CommonTree controlTemplate = new CommonTree(controlId, id, 2, "", "控制量");
            result.add(controlTemplate);

            //添加采集量子节点
            List<TemplatePointResponse> collectPoints =
                    templateCollectResponses.stream().filter(x -> x.getPointTemplateConfigId().equals(pointTemplateConfig.getId()) && x.getPointConfig() == PointProperties.ReadOnly.type()).collect(Collectors.toList());
            if (collectPoints.size() > 0) {
                Map<Integer, List<TemplatePointResponse>> collectGroups =
                        collectPoints.stream().collect(Collectors.groupingBy(x -> x.getNetFlag()));
                collectGroups.forEach((k, v) -> {
                    String collectType = collectId.concat("_" + k);
                    result.add(new CommonTree(collectType, collectId, 3, "", mapPointType(k)));
                    for (TemplatePointResponse templateCollectResponse : v) {
                        result.add(new CommonTree(collectType.concat("_") + templateCollectResponse.getId(), collectType, 4, JSONObject.toJSONString(templateCollectResponse), templateCollectResponse.getPointStandardName()));
                    }
                });
            }
            //添加控制量子节点
            List<TemplatePointResponse> controlPoints =
                    templateCollectResponses.stream().filter(x -> x.getPointTemplateConfigId().equals(pointTemplateConfig.getId()) && x.getPointConfig() == PointProperties.ReadAndControl.type()).collect(Collectors.toList());
            if (controlPoints.size() > 0) {
                Map<Integer, List<TemplatePointResponse>> controlGroups =
                        controlPoints.stream().filter(x -> StringUtils.isNotBlank(x.getPointStandardName())).collect(Collectors.groupingBy(x -> x.getNetFlag()));

                controlGroups.forEach((k, v) -> {
                    String controlType = controlId.concat("_" + k);
                    result.add(new CommonTree(controlType, controlId, 3, "", mapPointType(k)));
                    for (TemplatePointResponse templateCollectResponse : v) {
                        result.add(new CommonTree
                                (controlType.concat("_") + templateCollectResponse.getId(), controlType, 4, JSONObject.toJSONString(templateCollectResponse), templateCollectResponse.getPointStandardName()));
                    }
                });
            }
        }

        return Response.success(result);
    }

    private String mapPointType(int type) {
        switch (type) {
            case 0:
                return "公用";
            case 1:
                return "一次测";
            case 2:
                return "二次测";
            default:
                return "";
        }
    }
}





















