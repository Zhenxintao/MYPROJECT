package com.bmts.heating.bussiness.baseInformation.app.joggle.equipment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.basement.model.db.request.InsertEquipmentDto;
import com.bmts.heating.commons.basement.model.db.response.EquipmentInfoResponse;
import com.bmts.heating.commons.basement.model.enums.DataType;
import com.bmts.heating.commons.db.mapper.EquipmentInfoMapper;
import com.bmts.heating.commons.db.service.EquipmentInfoPointStandardService;
import com.bmts.heating.commons.db.service.EquipmentInfoService;
import com.bmts.heating.commons.db.service.PointStandardService;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.entiy.baseInfo.request.equipment.ColumnsDto;
import com.bmts.heating.commons.entiy.baseInfo.request.equipment.InsertEquipmentInfoDto;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryTdDto;
import com.bmts.heating.commons.grpc.lib.services.tdengine.QueryPointsOuterClass;
import com.bmts.heating.commons.redis.service.RedisEquipmentService;
import com.bmts.heating.commons.utils.es.LocalTimeUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.commons.utils.tdengine.TdTableIndex;
import com.bmts.heating.middleware.td.HistoryTdGrpcClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Api(tags = "设备信息管理")
@RestController
@RequestMapping("/equipmentInfo")
@Slf4j
public class EquipmentInfoJoggle {

    @Autowired
    private EquipmentInfoService equipmentInfoService;
    @Autowired
    private EquipmentInfoPointStandardService equipmentInfoPointStandardService;
    @Autowired
    private EquipmentInfoMapper equipmentInfoMapper;
    @Autowired
    private PointStandardService pointStandardService;
    @Autowired
    private HistoryTdGrpcClient historyTdGrpcClient;
    @Autowired
    private RedisEquipmentService redisEquipmentService;


    @ApiOperation("添加设备信息")
    @PostMapping("/insertEquipmentInfo")
    public Response insertEquipmentInfo(@RequestBody List<EquipmentInfo> dto) {
        try {
            Boolean result = equipmentInfoService.saveBatch(dto);
            if (result)
                return Response.success();
            return Response.fail();
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("修改设备信息")
    @PutMapping("/updateEquipmentInfo")
    public Response updateEquipmentInfo(@RequestBody EquipmentInfo dto) {
        try {
            Boolean result = equipmentInfoService.updateById(dto);
            if (result)
                return Response.success();
            return Response.fail();
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("删除设备信息")
    @DeleteMapping("/deleteEquipmentInfo")
    public Response deleteEquipmentInfo(@RequestParam Integer id) {
        try {
            Boolean result = equipmentInfoService.removeById(id);
            if (result) {
                Boolean pointResult = equipmentInfoPointStandardService.remove(Wrappers.<EquipmentInfoPointStandard>lambdaQuery().eq(EquipmentInfoPointStandard::getEquipmentInfoId, id));
                if (pointResult)
                    return Response.success();
                return Response.fail();
            }
            return Response.fail();
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("查询设备信息")
    @PostMapping("/queryEquipmentInfo")
    public Response queryEquipmentInfo(@RequestBody BaseDto dto) {
        try {
            QueryWrapper<EquipmentInfo> queryWrapper = new QueryWrapper<>();
            Page<EquipmentInfo> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
            if (StringUtils.isNotBlank(dto.getKeyWord())) {
                queryWrapper.like("equipmentName", dto.getKeyWord());
            }
            return Response.success(equipmentInfoService.page(page, queryWrapper));
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("根据设备Id查询已绑定点位信息")
    @GetMapping("/queryEquipmentInfoPointStandard")
    public Response queryEquipmentInfoPointStandard(@RequestParam Integer id) {
        try {
            QueryWrapper<EquipmentInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("e.id", id);
            return Response.success(equipmentInfoMapper.EquipmentInfoResponse(queryWrapper));
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("根据设备Id查询未绑定点位信息")
    @GetMapping("/queryEquipmentInfoPointStandardNot")
    public Response queryEquipmentInfoPointStandardNot(@RequestParam Integer id) {
        try {

            QueryWrapper<PointStandard> queryWrapper = new QueryWrapper<>();
//            queryWrapper.notInSql("id", "SELECT pointStandardId FROM equipmentInfoPointStandard WHERE equipmentInfoId=" + id);
            queryWrapper.notInSql("id", "SELECT pointStandardId FROM equipmentInfoPointStandard");
            List<PointStandard> list = pointStandardService.list(queryWrapper);
            return Response.success(list);
        } catch (Exception e) {
            return Response.fail();
        }
    }


    @ApiOperation("添加设备关联点位信息")
    @PostMapping("/insertEquipmentInfoPointStandard")
    public Response insertEquipmentInfoPointStandard(@RequestBody List<EquipmentInfoPointStandard> dto) {
        try {
            Boolean result = equipmentInfoPointStandardService.saveBatch(dto);
            if (result)
                return Response.success();
            return Response.fail();
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("删除设备关联点位信息")
    @PostMapping("/deleteEquipmentInfoPointStandard")
    public Response deleteEquipmentInfoPointStandard(@RequestBody List<Integer> ids) {
        try {
            Boolean result = equipmentInfoPointStandardService.removeByIds(ids);
            if (result)
                return Response.success();
            return Response.fail();
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("同步添加设备及点位信息")
    @PostMapping("/insertSyncEquipmentInfo")
    public Response insertSyncEquipmentInfo(@RequestBody InsertEquipmentDto dto) {
        try {
            EquipmentInfo em = new EquipmentInfo();
            em.setEquipmentCode(dto.getEquipmentCode());
            em.setEquipmentName(dto.getEquipmentName());
            Boolean result = equipmentInfoService.save(em);
            if (result) {
                List<EquipmentInfoPointStandard> list = new ArrayList<>();
                if (dto.getPointStandardIds().stream().count() > 0) {
                    for (Integer id : dto.getPointStandardIds()) {
                        EquipmentInfoPointStandard equipmentInfoPointStandard = new EquipmentInfoPointStandard();
                        equipmentInfoPointStandard.setEquipmentInfoId(em.getId());
                        equipmentInfoPointStandard.setPointStandardId(id);
                        list.add(equipmentInfoPointStandard);
                    }
                    Boolean equipmentPointResult = equipmentInfoPointStandardService.saveBatch(list);
                    if (equipmentPointResult) {
                        InsertEquipmentInfoDto infoDto = new InsertEquipmentInfoDto();
                        infoDto.setEquipmentName(dto.getEquipmentCode());
                        QueryWrapper<PointStandard> queryWrapper = new QueryWrapper<>();
                        queryWrapper.in("id", dto.getPointStandardIds());
                        List<PointStandard> pointStandardList = pointStandardService.list(queryWrapper);
                        List<ColumnsDto> columnsDtoList = new ArrayList<>();
                        for (PointStandard pointStandard : pointStandardList) {
                            ColumnsDto columnsDto = new ColumnsDto();
                            columnsDto.setColumnName(pointStandard.getColumnName());
                            columnsDto.setDataType(setDataTypeInfo(pointStandard.getDataType()));
                            columnsDtoList.add(columnsDto);
                        }
                        infoDto.setPoint(columnsDtoList);
                        if (!historyTdGrpcClient.insertEquipmentPointInfo(infoDto)) {
                            log.error("新增设备及点位信息同步Td数据库gRPC错误！---设备名称及code码为:" + dto.getEquipmentName() + "——" + dto.getEquipmentCode(), columnsDtoList);
                            return Response.fail();
                        }
                        // 同步设备编号
                        redisEquipmentService.syncPointStandardIds(dto.getPointStandardIds());

                        return Response.success();
                    }
                }
            }
            return Response.fail();
        } catch (Exception e) {
            return Response.fail();
        }
    }

    public String setDataTypeInfo(Integer dataType) {
        String dataTypeName = "";
        if (dataType != null) {
            dataTypeName = DataType.getValue(dataType);
        }
        return dataTypeName;
    }

    @ApiOperation("核实设备code码是否存在")
    @GetMapping("/queryEquipmentCodeStatus")
    public Response queryEquipmentCodeStatus(@RequestParam String code) {
        try {
            QueryWrapper<EquipmentInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("equipmentCode", code);
            EquipmentInfo equipmentInfo = equipmentInfoService.getOne(queryWrapper);
            if (equipmentInfo != null)
                return Response.success(true);
            return Response.success(false);
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("初始化设备信息同步Td")
    @GetMapping("/initializeEquipmentToTd")
    public Response initializeEquipmentToTd() {
        try {
            QueryWrapper<EquipmentInfo> queryWrapper = new QueryWrapper<>();
            List<EquipmentInfoResponse> equipmentInfoResponseList = equipmentInfoMapper.EquipmentInfoResponse(queryWrapper);
            if (equipmentInfoResponseList != null) {
                Map<String, List<EquipmentInfoResponse>> listMap = equipmentInfoResponseList.stream().collect(Collectors.groupingBy(EquipmentInfoResponse::getEquipmentCode));
                listMap.forEach((key, value) -> {
                    InsertEquipmentInfoDto infoDto = new InsertEquipmentInfoDto();
                    infoDto.setEquipmentName(key);
                    List<ColumnsDto> columnsDtoList = new ArrayList<>();
                    value.forEach(s -> {
                        ColumnsDto columnsDto = new ColumnsDto();
                        columnsDto.setColumnName(s.getColumnName());
                        columnsDto.setDataType(setDataTypeInfo(s.getDataType()));
                        columnsDtoList.add(columnsDto);
                    });
                    infoDto.setPoint(columnsDtoList);
                    if (!historyTdGrpcClient.insertEquipmentPointInfo(infoDto)) {
                        log.error("新增设备及点位信息同步Td数据库gRPC错误！---设备code码为:" + key, columnsDtoList);
                    }
                });
                // 同步设备编号
                redisEquipmentService.syncPointStandardIds(equipmentInfoResponseList.stream().map(s -> s.getPointStandardId()).collect(Collectors.toList()));
                return Response.success();
            }
            return Response.fail("数据库中未录入设备及点位信息");
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }
    }


}
