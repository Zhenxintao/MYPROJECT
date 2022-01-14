package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.db.entity.DeviceConfig;
import com.bmts.heating.commons.basement.model.db.entity.PointConfig;
import com.bmts.heating.commons.basement.model.db.entity.TemplatePoint;
import com.bmts.heating.commons.basement.model.db.response.CommonTree;
import com.bmts.heating.commons.basement.model.enums.DeviceType;
import com.bmts.heating.commons.db.service.DeviceConfigService;
import com.bmts.heating.commons.db.service.PointConfigService;
import com.bmts.heating.commons.db.service.TemplatePointService;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.middleware.cache.services.RedisCacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "设备配置管理")
@RestController
@RequestMapping("/deviceConfig")
public class DeviceConfigJogger {

    @Autowired
    private DeviceConfigService deviceConfigService;

    @Autowired
    private PointConfigService pointConfigService;

    @Autowired
    private TemplatePointService templatePointService;

    @ApiOperation("新增")
    @PostMapping
    public Response insert(@RequestBody DeviceConfig dto) {
        Response response = Response.fail();
        if (deviceConfigService.save(dto)) {
            return Response.success();
        }
        return response;
    }

    @ApiOperation("删除")
    @DeleteMapping
    public Response delete(@RequestParam int id) {
        Response response = Response.fail();
        QueryWrapper<DeviceConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        // 删除之前先判断有没有关联的采集量点数据
        QueryWrapper<PointConfig> pointCollectWrapper = new QueryWrapper<>();
        pointCollectWrapper.eq("deviceConfigId", id);
        List<PointConfig> listPointCollect = pointConfigService.list(pointCollectWrapper);
        QueryWrapper<TemplatePoint> templateCollectWrapper = new QueryWrapper<>();
        templateCollectWrapper.eq("deviceConfigId", id);
        List<TemplatePoint> listTemplateCollect = templatePointService.list(templateCollectWrapper);
        if (!CollectionUtils.isEmpty(listPointCollect) || !CollectionUtils.isEmpty(listTemplateCollect)) {
            response.setMsg("该设备配置id 有关联的采集量点数据，暂时无法删除！请先删除关联的采集量点数据！");
            return response;
        }
        if (deviceConfigService.removeById(id)) {
            return Response.success();
        }
        return response;
    }

    @ApiOperation("修改")
    @PutMapping
    public Response update(@RequestBody DeviceConfig updateInfo) {
        Response response = Response.fail();
        DeviceConfig deviceConfigById = deviceConfigService.getById(updateInfo.getId());
        if (deviceConfigById == null) {
            return response;
        }
        // 修改前先判断nodeCode 是否会重复
        if (!Objects.equals(deviceConfigById.getNodeCode(), updateInfo.getNodeCode())) {
            QueryWrapper<DeviceConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("nodeCode", deviceConfigById.getNodeCode());
            DeviceConfig one = deviceConfigService.getOne(queryWrapper);
            if (one != null) {
                response.setMsg("该节点名称已经存在！修改时请确保所有节点名称唯一！");
                return response;
            }
        }
        if (deviceConfigService.updateById(updateInfo)) {
            return Response.success();
        }
        return response;
    }

    @ApiOperation("详情")
    @GetMapping
    public Response detail(@RequestParam int id) {
        DeviceConfig byId = deviceConfigService.getById(id);
        return Response.success(byId);
    }

    @ApiOperation("查询")
    @GetMapping("/query")
    public Response query() {
        List<DeviceConfig> list = deviceConfigService.list();
        List<CommonTree> listTree = new ArrayList<>();
        Map<Integer, List<DeviceConfig>> collect = list.stream().collect(Collectors.groupingBy(e -> e.getType()));
        for (Integer type : collect.keySet()) {
            String pid = String.valueOf(type);
            CommonTree commonTree = new CommonTree();
            commonTree.setId(pid);
            commonTree.setLevel(0);
            if (type == DeviceType.DEVICE_PVSS.getType()) {
                commonTree.setName(DeviceType.DEVICE_PVSS.getName());
            }
            if (type == DeviceType.DEVICE_JK.getType()) {
                commonTree.setName(DeviceType.DEVICE_JK.getName());
            }
            listTree.add(commonTree);
            List<DeviceConfig> deviceConfigs = collect.get(type);
            for (DeviceConfig device : deviceConfigs) {
                CommonTree sonTree = new CommonTree();
                sonTree.setId(String.valueOf(device.getId()));
                sonTree.setPid(pid);
                sonTree.setName(device.getNodeCode());
                sonTree.setLevel(1);
                listTree.add(sonTree);
            }
        }
        return Response.success(listTree);
    }

    @Autowired
    RedisCacheService redisCacheService;

//    @ApiOperation("test")
//    @PostMapping("/test")
//    public Response test() throws MicroException {
//        return Response.success(redisCacheService.queryComputePoints());
//    }
}
