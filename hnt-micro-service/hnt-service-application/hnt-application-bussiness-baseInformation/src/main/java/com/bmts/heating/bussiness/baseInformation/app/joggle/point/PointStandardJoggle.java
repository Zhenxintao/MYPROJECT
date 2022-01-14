package com.bmts.heating.bussiness.baseInformation.app.joggle.point;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.bussiness.baseInformation.app.utils.ExcelUtils;
import com.bmts.heating.bussiness.baseInformation.app.utils.WrapperSortUtils;
import com.bmts.heating.commons.basement.model.db.entity.Dic;
import com.bmts.heating.commons.basement.model.db.entity.PointParameterType;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.entity.PointUnit;
import com.bmts.heating.commons.basement.model.db.request.PointStandardAddDto;
import com.bmts.heating.commons.basement.model.db.response.PointStandardImportResponse;
import com.bmts.heating.commons.basement.model.db.response.PointStandardResponse;
import com.bmts.heating.commons.basement.model.db.response.PointStandardSingleResponse;
import com.bmts.heating.commons.basement.model.enums.DataType;
import com.bmts.heating.commons.db.mapper.PointStandardMapper;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.entiy.baseInfo.enums.NetFlagType;
import com.bmts.heating.commons.entiy.baseInfo.enums.PointStandardLevel;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardDto;
import com.bmts.heating.commons.entiy.baseInfo.request.PointStandardImportDto;
import com.bmts.heating.commons.entiy.baseInfo.request.equipment.ColumnsDto;
import com.bmts.heating.commons.entiy.baseInfo.request.equipment.InsertEquipmentInfoDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.ComputeConfigAddDto;
import com.bmts.heating.commons.entiy.common.PointProperties;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.commons.utils.tdengine.TdTableIndex;
import com.bmts.heating.middleware.td.HistoryTdGrpcClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Api(tags = "标准点表管理")
@RestController
@RequestMapping("/pointStandard")
@Slf4j
public class PointStandardJoggle {
    @Autowired
    PointStandardService pointStandardService;
    @Autowired
    DicService dicService;
    @Autowired
    PointUnitService pointUnitService;
    @Autowired
    PointParameterTypeService pointParameterTypeService;
    @Autowired
    PointStandardMapper pointStandardMapper;

    @Autowired
    private PointConfigService pointConfigService;

    @GetMapping("/queryType/{typeName}/{level}")
    @ApiOperation("类别查询")
    public Response queryType(@PathVariable String typeName, @PathVariable Integer level) {
        QueryWrapper<PointStandardResponse> queryWrapper = new QueryWrapper<>();
        // 全部  -1  ， 其他  0
        if (level != null) {
            queryWrapper.eq("ps.level", level);
        }
        if (Objects.equals("-1", typeName)) {
            typeName = null;
        }
        if (StringUtils.isNotBlank(typeName) && !Objects.equals("0", typeName)) {
            queryWrapper.eq("pu.unitName", typeName).or().eq("pu.unitValue", typeName);
        }
        if (Objects.equals("0", typeName)) {
            queryWrapper.isNull("pu.unitName");
        }

        return Response.success(pointStandardMapper.listPointStandard(queryWrapper));
    }

    @Autowired
    private ComputeConfigJoggle computeConfigJoggle;

    @ApiOperation("新增")
    @PostMapping
    public Response insert(@RequestBody PointStandardAddDto info) {
        PointStandard pointStandard = info.getPointStandard();
        PointStandard one = pointStandardService.getOne(Wrappers.<PointStandard>lambdaQuery().eq(PointStandard::getColumnName, pointStandard.getColumnName()).eq(PointStandard::getLevel, pointStandard.getLevel()));
        if (one != null) {
            return Response.fail("参量名或标签名重复");
        }
        Response response = Response.fail();
        pointStandard.setCreateTime(LocalDateTime.now());
        if (pointStandardService.save(pointStandard)) {
            if (pointStandard.getPointConfig().equals(PointProperties.Compute.type()) && info.getPointStandardIds().length > 0) {
                return this.insert(pointStandard.getId(), info.getPointStandardIds());
            }
            return Response.success();
        }
        return response;
    }

    //插入计算量关联参量
    private Response insert(Integer compoteId, Integer[] pointStandardId) {
        ComputeConfigAddDto dto = new ComputeConfigAddDto();
        dto.setCompoteId(compoteId);
        dto.setPointStandardId(pointStandardId);
        switch (dto.verify()) {
            case 1:
                return Response.success();
            case 2:
                return computeConfigJoggle.add(dto);
            default:
                return Response.fail();
        }
    }

    @ApiOperation("修改")
    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    public Response update(@RequestBody PointStandardAddDto dto) {
        PointStandard info = dto.getPointStandard();
        Response response = Response.fail();
//        PointStandard pointStandard = pointStandardService.getById(info.getId());
//        if (pointStandard == null) {
//            return Response.fail();
//        }
        PointStandard one = pointStandardService.getOne(Wrappers.<PointStandard>lambdaQuery().eq(PointStandard::getColumnName, info.getColumnName()).eq(PointStandard::getLevel, info.getLevel()));
        if (one != null && !Objects.equals(one.getId(), info.getId())) {
            return Response.fail("参量名或标签名重复");
        }
//        String descriptionJson = pointStandard.getDescriptionJson();
        info.setUpdateTime(LocalDateTime.now());
        if (pointStandardService.updateById(info)) {
            // 批量更改点配置表的 descriptionJson 字段值
//            if (!Objects.equals(descriptionJson, info.getDescriptionJson())) {
//                pointConfigService.updateBatchStandard(info.getDescriptionJson(), info.getId());
//            }
            if (info.getPointConfig().equals(PointProperties.Compute.type())) {
                this.update(info.getId(), dto.getPointStandardIds());
            }
            return Response.success();
        }
        return response;
    }

    private void update(Integer computeId, Integer[] pointStandardId) {
        ComputeConfigAddDto dto = new ComputeConfigAddDto();
        dto.setCompoteId(computeId);
        dto.setPointStandardId(pointStandardId);
        switch (dto.verify()) {
            case 1:
                computeConfigJoggle.delete(computeId);
                break;
            case 2:
                computeConfigJoggle.delete(computeId);
                computeConfigJoggle.add(dto);
                break;
            default:
                throw new RuntimeException("参数错误");
        }
    }

    @ApiOperation("删除")
    @DeleteMapping
    public Response delete(@RequestParam int id) {
        Response response = Response.fail();
        if (pointStandardService.removeById(id)) {
            computeConfigJoggle.delete(id);
            return Response.success();
        }
        return response;
    }

    @ApiOperation("查询")
    @PostMapping("/query")
    public Response query(@RequestBody PointStandardDto dto) {
        Response response = Response.fail();
        try {
            Page<PointStandardResponse> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
            QueryWrapper<PointStandardResponse> queryWrapper = new QueryWrapper<>();
            WrapperSortUtils.sortWrapper(queryWrapper, dto);
            if (StringUtils.isNotBlank(dto.getKeyWord())) {
                queryWrapper.like("ps.name", dto.getKeyWord());
            }
            if (dto.getPointConfig() != null && dto.getPointConfig() != 0) {
                queryWrapper.eq("ps.pointConfig", dto.getPointConfig());
            }
            if (StringUtils.isNotBlank(String.valueOf(dto.getType())) && dto.getType() > 0) {
                queryWrapper.eq("ps.type", dto.getType());
            }
            if (dto.getNetFlag() != null) {
                queryWrapper.eq("ps.netFlag", dto.getNetFlag());
            }
            if (dto.getLevel() != null) {
                queryWrapper.eq("ps.level", dto.getLevel());
            }
            return Response.success(pointStandardService.queryPointStandard(page, queryWrapper));

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return response;
        }
    }

    @ApiOperation("详情")
    @GetMapping
    public Response detail(@RequestParam int id) {
        PointStandardSingleResponse singleResponse = new PointStandardSingleResponse();
        PointStandard info = pointStandardService.getById(id);
        singleResponse.setPointStandard(info);
        if (info.getPointConfig().equals(PointProperties.Compute.type())) {
            singleResponse.setConfigResponses(computeConfigJoggle.query(id));
        }
        return Response.success(singleResponse);
    }

    @ApiOperation("标准点表导入")
    @PostMapping("/import")
    public Response importExcel(@RequestParam MultipartFile file, PointStandardImportDto info) {
        try {
            List<PointStandardImportResponse> pointStandardExcel = ExcelUtils.readExcel("", PointStandardImportResponse.class, file);
            QueryWrapper<Dic> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("code", "pointStandardType");
            Dic one = dicService.getOne(queryWrapper);
            if (one == null) {
                return Response.fail();
            }
            queryWrapper.clear();
            queryWrapper.eq("code", "fixTypeValue");
            Dic fixDic = dicService.getOne(queryWrapper);
            if (fixDic == null) {
                return Response.fail();
            }
            queryWrapper.clear();

            QueryWrapper<PointUnit> queryWrapperPU = new QueryWrapper<>();
            QueryWrapper<PointParameterType> queryWrapperPPT = new QueryWrapper<>();
            List<PointStandardImportResponse> listFail = new ArrayList<>();
            for (PointStandardImportResponse psi : pointStandardExcel) {
                if (setPointStandard(info, queryWrapper, one, fixDic, queryWrapperPU, queryWrapperPPT, psi)) {
                    listFail.add(psi);
                }
            }
            if (!CollectionUtils.isEmpty(listFail)) {
                return Response.success(listFail);
            }
            return Response.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail();
        }
    }


    @ApiOperation("查询")
    @PostMapping("/list")
    public Response list(@RequestBody PointStandardDto dto) {
        Response response = Response.fail();
        try {
            QueryWrapper<PointStandardResponse> queryWrapper = new QueryWrapper<>();
            WrapperSortUtils.sortWrapper(queryWrapper, dto);
            if (StringUtils.isNotBlank(dto.getKeyWord())) {
                queryWrapper.like("ps.name", dto.getKeyWord());
            }
            if (StringUtils.isNotBlank(String.valueOf(dto.getPointConfig())) && dto.getPointConfig() != 0) {
                queryWrapper.eq("ps.pointConfig", dto.getPointConfig());
            }
            if (StringUtils.isNotBlank(String.valueOf(dto.getType())) && dto.getType() > 0) {
                queryWrapper.eq("ps.type", dto.getType());
            }
            if (dto.getLevel() != null) {
                queryWrapper.eq("ps.level", dto.getLevel());
            }
            return Response.success(pointStandardService.listPointStandard(queryWrapper));

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return response;
        }
    }


    @ApiOperation("根据分类分页查询")
    @PostMapping("/pageNetFlag")
    public Response pageNetFlag(@RequestBody PointStandardDto dto) {
        Response response = Response.fail();
        try {
            // 查询分组
            QueryWrapper<PointStandard> queryGroup = new QueryWrapper<>();
            List<PointStandard> list = pointStandardService.list(queryGroup);
            Map<Integer, List<PointStandard>> collect = list.stream().collect(Collectors.groupingBy(PointStandard::getNetFlag));
            // 每个分组进行分页查询
            List<PointStandardResponse> listStandard = new ArrayList<>();
            Page<PointStandardResponse> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
            AtomicLong total = new AtomicLong();
            AtomicLong size = new AtomicLong();
            collect.forEach((k, v) -> {
                QueryWrapper<PointStandardResponse> queryWrapper = new QueryWrapper<>();
                WrapperSortUtils.sortWrapper(queryWrapper, dto);
                if (StringUtils.isNotBlank(dto.getKeyWord())) {
                    queryWrapper.like("ps.name", dto.getKeyWord());
                }
                if (dto.getPointConfig() != null && dto.getPointConfig() > 0) {
                    queryWrapper.eq("ps.pointConfig", dto.getPointConfig());
                }
                if (StringUtils.isNotBlank(String.valueOf(dto.getType())) && dto.getType() > 0) {
                    queryWrapper.eq("ps.type", dto.getType());
                }
                if (dto.getLevel() != null) {
                    queryWrapper.eq("ps.level", dto.getLevel());
                }
                queryWrapper.eq("ps.netFlag", k);
                Page<PointStandardResponse> pointStandardResponsePage = pointStandardService.queryPointStandard(page, queryWrapper);
                total.addAndGet(pointStandardResponsePage.getTotal());
                size.addAndGet(pointStandardResponsePage.getSize());
                listStandard.addAll(pointStandardResponsePage.getRecords());
            });
            page.setRecords(listStandard);
            page.setSize(size.get());
            page.setTotal(total.get());

            return Response.success(page);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return response;
        }
    }


    private boolean setPointStandard(PointStandardImportDto info, QueryWrapper<Dic> queryWrapper, Dic one, Dic fixDic, QueryWrapper<PointUnit> queryWrapperPU, QueryWrapper<PointParameterType> queryWrapperPPT, PointStandardImportResponse psi) {
        PointStandard psd = new PointStandard();
        if (StringUtils.isBlank(psi.getName()) || StringUtils.isBlank(psi.getColumnName())) {
            return true;
        }
        psd.setName(psi.getName());
        psd.setColumnName(psi.getColumnName());
        psd.setPointConfig(info.getPointConfig());
        psd.setCreateTime(LocalDateTime.now());
        if (info.getUserId() != 0) {
            psd.setUserId(info.getUserId());
        }
        if (StringUtils.isNotBlank(info.getUserName())) {
            psd.setCreateUser(info.getUserName());
        }
        /*关联字典表  设置 type 值*/
        if (StringUtils.isNotBlank(psi.getPointTypeName())) {
            queryWrapper.eq("pid", one.getId());
            queryWrapper.eq("name", psi.getPointTypeName());
            Dic one1 = dicService.getOne(queryWrapper);
            queryWrapper.clear();
            if (one1 == null) {
                return true;
            }
            psd.setType(one1.getId());
        } else {
            return true;
        }
        /**关联单位表  设置 pointUnitId */
        if (StringUtils.isNotBlank(psi.getUnit())) {
            queryWrapperPU.eq("unitValue", psi.getUnit());
            PointUnit one2 = pointUnitService.getOne(queryWrapperPU);
            queryWrapperPU.clear();
            if (one2 != null) {
                psd.setPointUnitId(one2.getId());
            }

        }
        /** 设置 数据类型 */
        if (StringUtils.isNotBlank(psi.getDataTypeName())) {
            String dataTypeName = psi.getDataTypeName().toLowerCase();
            Integer dataType = DataType.getValue(dataTypeName);
            if (dataType != null) {
                psd.setDataType(dataType);
            }
        } else {
            return true;
        }


        /** 设置 网测类型 0.公共 1.一次侧 2.二次侧*/
        if (StringUtils.isNotBlank(psi.getNetFlagName())) {
            Integer netFlag = NetFlagType.getNetFlag(psi.getNetFlagName());
            if (netFlag != null) {
                psd.setNetFlag(netFlag);
            }
        } else {
            return true;
        }
        /** 设置 是否参与运算 是  否 */
        if (StringUtils.isNotBlank(psi.getComputeMsg())) {
            if (Objects.equals(psi.getComputeMsg(), "是")) {
                psd.setIsComputePoint(true);
            }
            if (Objects.equals(psi.getComputeMsg(), "否")) {
                psd.setIsComputePoint(false);
            }
        }
        /**关联字典表 fix值类型 */
        if (StringUtils.isNotBlank(psi.getFixValueTypeName())) {
            queryWrapper.eq("pid", fixDic.getId());
            queryWrapper.eq("name", psi.getFixValueTypeName());
            Dic fixOne = dicService.getOne(queryWrapper);
            queryWrapper.clear();
            if (fixOne == null) {
                return true;
            }
            psd.setFixValueType(fixOne.getId());
        } else {
            return true;
        }

        /**关联参量分类表*/
        if (StringUtils.isNotBlank(psi.getPointName())) {
            queryWrapperPPT.eq("name", psi.getPointName());
            PointParameterType one3 = pointParameterTypeService.getOne(queryWrapperPPT);
            queryWrapperPPT.clear();
            if (one3 == null) {
                PointParameterType pointParameterType = new PointParameterType();
                pointParameterType.setName(psi.getPointName());
                pointParameterType.setSort(1);
                pointParameterType.setCreateTime(LocalDateTime.now());
                pointParameterType.setCreateUser("超级管理员");
                pointParameterType.setUserId(-1);
                pointParameterType.setType(1);
                if (pointParameterTypeService.save(pointParameterType)) {
                    psd.setPointParameterTypeId(pointParameterType.getId());
                }
            } else {
                psd.setPointParameterTypeId(one3.getId());
            }
        } else {
            return true;
        }
        // 处理所属类型
        if (StringUtils.isNotBlank(psi.getLevelName())) {
            Integer standardLevel = PointStandardLevel.getStandardLevel(psi.getLevelName());
            if (standardLevel != null) {
                psd.setLevel(standardLevel);
            }
        } else {
            return true;
        }

        // 处理重名
        PointStandard onePointStandard = pointStandardService.getOne(Wrappers.<PointStandard>lambdaQuery()
                .eq(PointStandard::getColumnName, psd.getColumnName())
                .eq(PointStandard::getLevel, psd.getLevel()));
        //.or().eq(PointStandard::getName, psd.getName()));
        if (onePointStandard == null) {
            pointStandardService.save(psd);
        }
        return false;
    }

    @Autowired
    private HistoryTdGrpcClient client;

    @ApiOperation("修改当前标准点是否同步到td库状态")
    @GetMapping("/updateById")
    @Transactional(rollbackFor = Exception.class)
    public Response updatePointStandardTd(@RequestParam int id) {
        String flag = null;
        PointStandard one = pointStandardService.getById(id);
        if (one != null) {
            one.setTdColumn(!one.getTdColumn());
            boolean b = pointStandardService.updateById(one);
            if (b) {
                flag = this.insertOneToTd(one);
                if (flag == null){
                    return Response.success();
                }
            }
        }
        return Response.fail(flag);
    }

    private String insertOneToTd(PointStandard pointStandard) {
        ColumnsDto columnsDto = new ColumnsDto();
        columnsDto.setColumnName(pointStandard.getColumnName());
        columnsDto.setDataType(this.dataType(pointStandard.getDataType()));
        String tableName = null;
        Boolean flag = false;
        String str = null;
        if (pointStandard.getTdColumn()){
            //添加操作
            if (pointStandard.getLevel()==1){
                tableName = TdTableIndex.STATION_MINUTE_INITIALIZE.getIndex();
            }else{
                tableName = TdTableIndex.SOURCE_MINUTE_INITIALIZE.getIndex();
            }
            flag = client.addColumn(columnsDto, tableName);
        }else{
            //删除操作
            if (pointStandard.getLevel()==1){
                QueryWrapper<PointStandard> queryWrapper = new QueryWrapper<PointStandard>().eq("tdColumn",true).eq("level",pointStandard.getLevel());
                List<PointStandard> stations = pointStandardService.list(queryWrapper);
                if (stations.size() >= 2){
                    tableName = TdTableIndex.STATION_MINUTE_INITIALIZE.getIndex();
                    flag = client.delColumn(columnsDto,tableName);
                }else{
                    str = "站/源最少要有一个";
                }
            }else{
                QueryWrapper<PointStandard> queryWrapper = new QueryWrapper<PointStandard>().eq("tdColumn",true).eq("level",pointStandard.getLevel());
                List<PointStandard> sources = pointStandardService.list(queryWrapper);
                if (sources.size() >= 2){
                    tableName = TdTableIndex.SOURCE_MINUTE_INITIALIZE.getIndex();
                    flag = client.delColumn(columnsDto,tableName);
                }else{
                    str = "站/源最少要有一个";
                }
            }
        }
        if (!flag){
            log.error("插入td失败{}",pointStandard);
            str = "站/源没初始化";
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return str;
    }


    @ApiOperation("站/源 初始化/删除 历史库")
    @GetMapping("/initialTd")
    public Response initialTdPointStandard(@RequestParam int level) {
        Boolean flag = false;
        if (level == 1){
            //站
            QueryWrapper<PointStandard> queryWrapper = new QueryWrapper<PointStandard>().eq("tdColumn",true).eq("level",level);
            List<PointStandard> stations = pointStandardService.list(queryWrapper);
            if (!CollectionUtils.isEmpty(stations) && stations.size() >= 1){
                flag = insertToTd(stations);
                if (flag){
                    return Response.success();
                }
            }
        }else{
            QueryWrapper<PointStandard> queryWrapper = new QueryWrapper<PointStandard>().eq("tdColumn",true).eq("level",level);
            List<PointStandard> sources = pointStandardService.list(queryWrapper);
            if (!CollectionUtils.isEmpty(sources) && sources.size() >= 1){
                flag = insertToTd(sources);
                if (flag){
                    return Response.success();
                }
            }
        }

        return Response.fail("初始化时 站/源 最少要有一个点");
    }

    //初始化入历史库
    private boolean insertToTd(List<PointStandard> list) {
        InsertEquipmentInfoDto dto = new InsertEquipmentInfoDto();
        List<ColumnsDto> columnsDtoList = new ArrayList<>();
        for (PointStandard standard : list) {
            if (standard.getLevel() == 1) {
                dto.setEquipmentName(TdTableIndex.STATION_MINUTE_INITIALIZE.getIndex());
                ColumnsDto columnsDto = new ColumnsDto();
                columnsDto.setColumnName(standard.getColumnName());
                columnsDto.setDataType(this.dataType(standard.getDataType()));
                columnsDtoList.add(columnsDto);
            } else {
                dto.setEquipmentName(TdTableIndex.SOURCE_MINUTE_INITIALIZE.getIndex());
                ColumnsDto columnsDto = new ColumnsDto();
                columnsDto.setColumnName(standard.getColumnName());
                columnsDto.setDataType(this.dataType(standard.getDataType()));
                columnsDtoList.add(columnsDto);
            }
        }
        if (!CollectionUtils.isEmpty(columnsDtoList)) {
            dto.setPoint(columnsDtoList);
            if (!client.insertEquipmentPointInfo(dto)) {
                log.error("插入td标准点失败,失败的热力站数据{}", dto);
                return false;
            }
        }
        return true;
    }

    private String dataType(Integer number) {
        // 1-Bool 2-Int 3-Uint 4-Long 5-ULong 6-Float 7-Double',
        String dataType = null;
        switch (number) {
            case 1:
                dataType = "boolean";
                break;
            case 2:
                dataType = "Integer";
                break;
            case 3:
                dataType = "UInteger";
                break;
            case 4:
                dataType = "Long";
                break;
            case 5:
                dataType = "ULong";
                break;
            case 6:
                dataType = "Float";
                break;
            case 7:
                dataType = "Double";
                break;
        }
        return dataType;
    }
}
