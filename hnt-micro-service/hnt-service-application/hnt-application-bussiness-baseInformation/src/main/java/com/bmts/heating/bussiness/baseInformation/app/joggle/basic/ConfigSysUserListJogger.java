package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.db.entity.ConfigSysUserList;
import com.bmts.heating.commons.basement.model.db.entity.HeatTransferStation;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.response.PointStandardResponse;
import com.bmts.heating.commons.db.service.ConfigSysUserListService;
import com.bmts.heating.commons.db.service.DefaultRealHeadersService;
import com.bmts.heating.commons.db.service.HeatTransferStationService;
import com.bmts.heating.commons.db.service.PointStandardService;
import com.bmts.heating.commons.entiy.baseInfo.request.ConfigSysUserListDto;
import com.bmts.heating.commons.entiy.baseInfo.response.ConfigSysUserListResponse;
import com.bmts.heating.commons.entiy.baseInfo.response.DefaultRealHeadersDto;
import com.bmts.heating.commons.entiy.common.TableHeaderType;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "自定义列表管理")
@RestController
@RequestMapping("/configSysUserList")
public class ConfigSysUserListJogger {

    @Autowired
    private ConfigSysUserListService configSysUserListService;

    @Autowired
    private PointStandardService pointStandardService;

    @Autowired
    private HeatTransferStationService heatTransferStationService;

    @Autowired
    private DefaultRealHeadersService defaultRealHeadersService;


    @ApiOperation("查询")
    @PostMapping("/query")
    public Response query(@RequestBody int userId) {
        List<ConfigSysUserListResponse> listResponse = new ArrayList<>();
        QueryWrapper<ConfigSysUserList> queryWrapper = new QueryWrapper<>();
        if (userId != 0) {
            queryWrapper.eq("userId", userId);
        } else {
            return Response.success(listResponse);
        }
        List<ConfigSysUserList> list = configSysUserListService.list(queryWrapper);
        // 组装数据
        setResponse(list, listResponse);
        return Response.success(listResponse);
    }

    private void setResponse(List<ConfigSysUserList> list, List<ConfigSysUserListResponse> listResponse) {
        // 组装数据
        for (ConfigSysUserList conSys : list) {
            ConfigSysUserListResponse response = getConfigSysUserListResponse(conSys);
            listResponse.add(response);
        }
    }

    // 组装站点数据 和 参量类型数据
    private ConfigSysUserListResponse getConfigSysUserListResponse(ConfigSysUserList conSys) {
        ConfigSysUserListResponse response = new ConfigSysUserListResponse();
        response.setId(conSys.getId());
        response.setName(conSys.getName());
        response.setUserId(conSys.getUserId());
        response.setHeatOrganizationIds(JSONArray.parseArray(conSys.getHeatOrganizationIds(), Integer.class));
        response.setIsDefault(conSys.getIsDefault());
        response.setTypeJson(conSys.getTypeJson());
        // 组装站点数据
        setHeatStation(conSys, response);
        // 组装参数数据
        setPointStandard(conSys, response);
        return response;
    }

    private void setHeatStation(ConfigSysUserList conSys, ConfigSysUserListResponse response) {
        // 组装站点数据
        List<ConfigSysUserListResponse.HeatStation> heatStations = new ArrayList<>();
        List<Integer> listId = JSONArray.parseArray(conSys.getStaionIds(), Integer.class);
        if (!CollectionUtils.isEmpty(listId)) {

            QueryWrapper<HeatTransferStation> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", listId);
            List<HeatTransferStation> listStation = heatTransferStationService.list(queryWrapper);
            for (HeatTransferStation station : listStation) {
                ConfigSysUserListResponse.HeatStation oneStation = new ConfigSysUserListResponse.HeatStation();
                oneStation.setId(station.getId());
                oneStation.setName(station.getName());
                oneStation.setPid(station.getHeatOrganizationId());
                oneStation.setProperties("station");
                heatStations.add(oneStation);
            }
        }
        response.setStaionIds(heatStations);
    }

    private void setPointStandard(ConfigSysUserList conSys, ConfigSysUserListResponse response) {
        // 处理参量列表的数据
        List<ConfigSysUserListResponse.PointStandard> pointStandardIds = new ArrayList<>();
        List<Integer> listId = JSONArray.parseArray(conSys.getPointStandardIds(), Integer.class);
        if (!CollectionUtils.isEmpty(listId)) {
            QueryWrapper<PointStandard> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("ps.id", listId);

            List<PointStandardResponse> listRespon = pointStandardService.listPointStandard(queryWrapper);
            for (PointStandardResponse pointStandard : listRespon) {
                ConfigSysUserListResponse.PointStandard onePointStandard = new ConfigSysUserListResponse.PointStandard();
                onePointStandard.setId(pointStandard.getId());
                onePointStandard.setName(pointStandard.getName());
                onePointStandard.setNetFlag(pointStandard.getNetFlag());
                onePointStandard.setColumnName(pointStandard.getColumnName());
                onePointStandard.setPointName(pointStandard.getPointName());
                onePointStandard.setUnit(pointStandard.getUnit());
                onePointStandard.setUnitDisable(pointStandard.getUnitDisable());
                pointStandardIds.add(onePointStandard);
            }
        }
        response.setPointStandardIds(pointStandardIds);
    }

    @ApiOperation("新增")
    @PostMapping("/add")
    public Response insert(@RequestBody ConfigSysUserListDto dto) {
        Response response = Response.fail();
        ConfigSysUserList addConfig = new ConfigSysUserList();
        addConfig.setUserId(dto.getUserId());
        if (StringUtils.isNotBlank(dto.getName())) {
            // 处理重名
            QueryWrapper<ConfigSysUserList> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", dto.getName());
            queryWrapper.eq("userId", dto.getUserId());
            ConfigSysUserList one = configSysUserListService.getOne(queryWrapper);
            if (one != null) {
                response.setMsg("该名称已经存在！请更改名称后再保存！");
                return response;
            }
            addConfig.setName(dto.getName());
        }
        if (dto.getIsDefault() != null) {
            addConfig.setIsDefault(dto.getIsDefault());
            if (dto.getIsDefault() == true) {
                updateIsDefault(dto);
            }
        }
        if (!CollectionUtils.isEmpty(dto.getStaionIds())) {
            addConfig.setStaionIds(JSON.toJSONString(dto.getStaionIds()));
        }
        if (!CollectionUtils.isEmpty(dto.getHeatOrganizationIds())) {
            addConfig.setHeatOrganizationIds(JSON.toJSONString(dto.getHeatOrganizationIds()));
        }
        if (CollectionUtils.isEmpty(dto.getPointStandardIds())) {
            QueryWrapper<DefaultRealHeadersDto> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("drh.type", TableHeaderType.RealValue.type());
            List<DefaultRealHeadersDto> listHeaders = defaultRealHeadersService.listHeaders(queryWrapper);
            List<Integer> collect = listHeaders.stream().map(DefaultRealHeadersDto::getPointStandardId).collect(Collectors.toList());
            addConfig.setPointStandardIds(JSONArray.toJSONString(collect));
        } else {
            addConfig.setPointStandardIds(JSON.toJSONString(dto.getPointStandardIds()));
        }
        if (StringUtils.isNotBlank(dto.getTypeJson())) {
            addConfig.setTypeJson(dto.getTypeJson());
        }
        if (configSysUserListService.save(addConfig)) {
            return Response.success();
        }
        return response;
    }

    private void updateIsDefault(ConfigSysUserListDto dto) {
        QueryWrapper<ConfigSysUserList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isDefault", dto.getIsDefault());
        ConfigSysUserList one = configSysUserListService.getOne(queryWrapper);
        if (one != null) {
            one.setIsDefault(false);
            configSysUserListService.updateById(one);
        }
    }

    @ApiOperation("删除")
    @PostMapping("/del")
    public Response delete(@RequestBody int id) {
        Response response = Response.fail();
        if (configSysUserListService.removeById(id)) {
            return Response.success();
        }
        return response;
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public Response update(@RequestBody ConfigSysUserListDto updateInfo) {
        Response response = Response.fail();
        ConfigSysUserList configSysById = configSysUserListService.getById(updateInfo.getId());
        if (configSysById == null) {
            return response;
        }
        if (StringUtils.isNotBlank(updateInfo.getName())) {
            // 处理重名
            QueryWrapper<ConfigSysUserList> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", updateInfo.getName());
            queryWrapper.eq("userId", updateInfo.getUserId());
            ConfigSysUserList one = configSysUserListService.getOne(queryWrapper);
            if (one != null && !Objects.equals(one.getId(), updateInfo.getId())) {
                response.setMsg("该名称已经存在！请更改名称后再保存！");
                return response;
            }
            configSysById.setName(updateInfo.getName());
        }
        if (updateInfo.getIsDefault() != null) {
            configSysById.setIsDefault(updateInfo.getIsDefault());
            if (updateInfo.getIsDefault() == true) {
                updateIsDefault(updateInfo);
            }
        }
        configSysById.setStaionIds(JSON.toJSONString(updateInfo.getStaionIds()));
        if (!CollectionUtils.isEmpty(updateInfo.getHeatOrganizationIds())) {
            configSysById.setHeatOrganizationIds(JSON.toJSONString(updateInfo.getHeatOrganizationIds()));
        }
        if (!CollectionUtils.isEmpty(updateInfo.getPointStandardIds())) {
            configSysById.setPointStandardIds(JSON.toJSONString(updateInfo.getPointStandardIds()));
        }
        if (StringUtils.isNotBlank(updateInfo.getTypeJson())) {
            configSysById.setTypeJson(updateInfo.getTypeJson());
        }

        if (configSysUserListService.updateById(configSysById)) {
            return Response.success();
        }
        return response;
    }

    @ApiOperation("详情")
    @GetMapping
    public Response detail(@RequestParam int id) {
        ConfigSysUserList byId = configSysUserListService.getById(id);
        if (byId != null) {
            ConfigSysUserListResponse response = getConfigSysUserListResponse(byId);
            return Response.success(response);
        }
        return Response.success(null);
    }

}
