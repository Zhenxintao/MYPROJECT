package com.bmts.heating.bussiness.baseInformation.app.joggle.point;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.bussiness.baseInformation.app.utils.ExcelUtils;
import com.bmts.heating.bussiness.baseInformation.app.utils.WrapperSortUtils;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.basement.model.db.response.PointStandardImportResponse;
import com.bmts.heating.commons.basement.model.db.response.PointStandardResponse;
import com.bmts.heating.commons.basement.model.enums.DataType;
import com.bmts.heating.commons.db.mapper.PointStandardFullMapper;
import com.bmts.heating.commons.db.mapper.PointStandardMapper;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardChangeDto;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardDto;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardImportDto;
import com.bmts.heating.commons.entiy.baseInfo.response.PointStandardFullResponse;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Api(tags = "标准点表管理")
@RestController
@RequestMapping("/pointStandardFull")
@Slf4j
public class PointStandardFullJoggle {
    @Autowired
    PointStandardFullMapper pointStandardFullMapper;
    @Autowired
    PointStandardFullService pointStandardFullService;
    @Autowired
    PointStandardService pointStandardService;

    @ApiOperation("查询全类点表数据")
    @GetMapping("/pointStandardFullList")
    public Response pointStandardFullList() {
        try {
            List<PointStandardFullResponse> pointStandardFullList = pointStandardFullMapper.pointStandardFullList();
            return Response.success(pointStandardFullList);
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("查询配置点表数据")
    @GetMapping("/pointStandardList")
    public Response pointStandardList() {
        try {
            List<PointStandardFullResponse> pointStandardList = pointStandardFullMapper.pointStandardList();
            return Response.success(pointStandardList);
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @ApiOperation("添加项目使用点表信息")
    @PostMapping("/insertPointStandard")
    public Response insertPointStandard(@RequestBody PointStandardChangeDto pointStandardChangeDto) {
        List<Integer> ids = Arrays.stream(pointStandardChangeDto.getPointStandardIds()).collect(Collectors.toList());
        List<PointStandardFull> pointStandardFullList=   pointStandardFullService.listByIds(ids);
//        QueryWrapper<PointStandardFull> pointStandardFullQueryWrapper = new QueryWrapper<>();
//        pointStandardFullQueryWrapper.in("id", pointStandardChangeDto.getPointStandardIds());
//        List<PointStandardFull> pointStandardFullList = pointStandardFullService.list(pointStandardFullQueryWrapper);
        List<String> columns = pointStandardFullList.stream().map(s -> s.getColumnName()).collect(Collectors.toList());
        QueryWrapper<PointStandard> pointStandardQueryWrapper = new QueryWrapper<>();
        pointStandardQueryWrapper.in("columnName", columns);
        List<PointStandard> pointStandardList = pointStandardService.list(pointStandardQueryWrapper);
        List<PointStandard> pointStandardInserts = new ArrayList<>();
        if (pointStandardList.stream().count() == 0) {
            pointStandardInserts = pointStandardFullList.stream().map(s -> {
                PointStandard p = new PointStandard();
                p.setName(s.getName());
                p.setType(s.getType());
                p.setDataType(s.getDataType());
                p.setColumnName(s.getColumnName());
                p.setNetFlag(s.getNetFlag());
                p.setIsComputePoint(s.getIsComputePoint());
                p.setPointParameterTypeId(s.getPointParameterTypeId());
                p.setPointUnitId(s.getPointUnitId());
                p.setFixValueType(s.getFixValueType());
                p.setCreateUser(s.getCreateUser());
                p.setCreateTime(s.getCreateTime());
                p.setUpdateUser(s.getUpdateUser());
                p.setUpdateTime(s.getUpdateTime());
                p.setDescription(s.getDescription());
                p.setDeleteUser(s.getDeleteUser());
                p.setDeleteTime(s.getDeleteTime());
                p.setUserId(s.getUserId());
                p.setPointConfig(s.getPointConfig());
                p.setDescriptionJson(s.getDescriptionJson());
                return p;
            }).collect(Collectors.toList());
        } else {
            for (PointStandardFull s : pointStandardFullList) {
                PointStandard result = pointStandardList.stream().filter(f -> f.getColumnName().equals(s.getColumnName())).findFirst().orElse(null);
                if (result == null) {
                    PointStandard p = new PointStandard();
                    p.setName(s.getName());
                    p.setType(s.getType());
                    p.setDataType(s.getDataType());
                    p.setColumnName(s.getColumnName());
                    p.setNetFlag(s.getNetFlag());
                    p.setIsComputePoint(s.getIsComputePoint());
                    p.setPointParameterTypeId(s.getPointParameterTypeId());
                    p.setPointUnitId(s.getPointUnitId());
                    p.setFixValueType(s.getFixValueType());
                    p.setCreateUser(s.getCreateUser());
                    p.setCreateTime(s.getCreateTime());
                    p.setUpdateUser(s.getUpdateUser());
                    p.setUpdateTime(s.getUpdateTime());
                    p.setDescription(s.getDescription());
                    p.setDeleteUser(s.getDeleteUser());
                    p.setDeleteTime(s.getDeleteTime());
                    p.setUserId(s.getUserId());
                    p.setPointConfig(s.getPointConfig());
                    p.setDescriptionJson(s.getDescriptionJson());
                    pointStandardInserts.add(p);
                }
            }
        }
        if (pointStandardInserts.stream().count()==0)
        {
            return Response.success();
        }
        else {
            Boolean insertPoint = pointStandardService.saveBatch(pointStandardInserts);
            if (insertPoint) {
                return Response.success();
            } else {
                return Response.fail();
            }
        }
    }

    @ApiOperation("删除项目使用点表信息")
    @PostMapping("/deletePointStandard")
    public Response deletePointStandard(@RequestBody PointStandardChangeDto pointStandardChangeDto) {
        List<Integer> ids = Arrays.stream(pointStandardChangeDto.getPointStandardIds()).collect(Collectors.toList());
        Boolean result = pointStandardService.removeByIds(ids);
        if (result) {
            return Response.success();
        } else {
            return Response.fail();
        }
    }
}
