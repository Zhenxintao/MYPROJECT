package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.basement.model.db.response.HeatNetResponse;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.db.mapper.HeatAreaChangeHistoryMapper;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatAreaChangeDto;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.entiy.energy.AreaChangeHistoryDto;
import com.bmts.heating.commons.entiy.energy.AreaManagerDto;
import com.bmts.heating.commons.utils.calcu.BigDecimalStaramUtil;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@Slf4j
@Api(tags = "面积管理")
@RequestMapping("/areaManager/")
public class AreaManagerJoggle {

    @Autowired
    private HeatNetService netService;

    @Autowired
    private HeatSourceService sourceService;

    @Autowired
    private HeatTransferStationService stationService;

    @Autowired
    private StationFirstNetBaseViewService stationViewService;

    @Autowired
    private SourceFirstNetBaseViewService sourceViewService;

    @Autowired
    private HeatSystemService systemService;
    @Autowired
    private SystemConfigJoggle joggle;

    @Autowired
    private HeatAreaChangeHistoryService heatAreaChangeHistoryService;

//    @Autowired
//    private HeatAreaChangeHistoryMapper heatAreaChangeHistoryMapper;

    //系统:1  控制柜:2  换热站:3  热源:4  热网:5  系统分支:6

    @ApiOperation("修改面积")
    @PostMapping("update")
    @Transactional
    public Response update(@RequestBody AreaManagerDto dto) {
        final Integer level = dto.getLevel();
        if (level == TreeLevel.HeatSystem.level()) {
            HeatSystem heatSystem = new HeatSystem();
            heatSystem.setId(dto.getRelevanceId());
            heatSystem.setHeatArea(dto.getArea());
            if (systemService.updateById(heatSystem)) {
                HeatAreaChangeDto areaChangeDto = new HeatAreaChangeDto();
                areaChangeDto.setLevel(TreeLevel.HeatSystem.level());
                areaChangeDto.setRelevanceId(dto.getRelevanceId());
                areaChangeDto.setNewValue(dto.getArea());
                this.updateHistory(areaChangeDto);
                //更新换热站面积
                this.convergeStationArea(dto.getRelevanceId());
                return Response.success();
            } else {
                throw new RuntimeException("更新失败");
            }
        } else if (level == TreeLevel.HeatStation.level()) {
            HeatTransferStation station = new HeatTransferStation();
            station.setId(dto.getRelevanceId());
            station.setHeatArea(dto.getArea());
            if (stationService.updateById(station)) {
                HeatAreaChangeDto areaChangeDto = new HeatAreaChangeDto();
                areaChangeDto.setLevel(TreeLevel.HeatStation.level());
                areaChangeDto.setRelevanceId(dto.getRelevanceId());
                areaChangeDto.setNewValue(dto.getArea());
                this.updateHistory(areaChangeDto);
                return Response.success();
            } else {
                throw new RuntimeException("更新失败");
            }
        } else if (level == TreeLevel.HeatSource.level()) {
            HeatSource source = new HeatSource();
            source.setId(dto.getRelevanceId());
            source.setHeatArea(dto.getArea());
            if (sourceService.updateById(source)) {
                HeatAreaChangeDto areaChangeDto = new HeatAreaChangeDto();
                areaChangeDto.setLevel(TreeLevel.HeatSource.level());
                areaChangeDto.setRelevanceId(dto.getRelevanceId());
                areaChangeDto.setNewValue(dto.getArea());
                this.updateHistory(areaChangeDto);
                return Response.success();
            } else {
                throw new RuntimeException("更新失败");
            }
        } else if (level == TreeLevel.HeatNet.level()) {
            HeatNet net = new HeatNet();
            net.setId(dto.getRelevanceId());
            net.setHeatArea(dto.getArea());
            if (netService.updateById(net)) {
                HeatAreaChangeDto areaChangeDto = new HeatAreaChangeDto();
                areaChangeDto.setLevel(TreeLevel.HeatSource.level());
                areaChangeDto.setRelevanceId(dto.getRelevanceId());
                areaChangeDto.setNewValue(dto.getArea());
                this.updateHistory(areaChangeDto);
                return Response.success();
            } else {
                throw new RuntimeException("更新失败");
            }
        }
        return Response.paramError();
    }

    /**
     * 异步
     * 更据系统面积求和得出0号机组面积，并更新
     *
     * @param systemId 系统Id
     */
    @Async
    public void convergeStationArea(Integer systemId) {
        List<StationFirstNetBaseView> stationViews = stationViewService.list();
        Wrapper<StationFirstNetBaseView> wrapper = Wrappers.<StationFirstNetBaseView>lambdaQuery()
                .eq(StationFirstNetBaseView::getHeatSystemId, systemId);
        StationFirstNetBaseView one = stationViewService.getOne(wrapper);
        if (one == null || one.getHeatCabinetId() == null) {
            //热源
            List<SourceFirstNetBaseView> sourceView = sourceViewService.list();
            Wrapper<SourceFirstNetBaseView> sourceWrapper = Wrappers.<SourceFirstNetBaseView>lambdaQuery()
                    .eq(SourceFirstNetBaseView::getHeatSystemId, systemId);
            SourceFirstNetBaseView sourceOne = sourceViewService.getOne(sourceWrapper);
            Integer sourceId = sourceOne.getHeatSourceId();

            BigDecimal sourceArea = sourceView
                    .stream()
                    .filter(e -> e.getHeatSourceId().equals(sourceId))
                    .collect(BigDecimalStaramUtil.summingBigDecimal(SourceFirstNetBaseView::getHeatSystemArea));
            AreaManagerDto dto = new AreaManagerDto();
            dto.setArea(sourceArea);
            dto.setRelevanceId(sourceId);
            dto.setLevel(TreeLevel.HeatSource.level());
            this.update(dto);
        } else {
            //热力站
            Integer stationId = one.getHeatTransferStationId();
            if ("0".equals(one.getNumber())) {
                //0号机组
                BigDecimal stationArea = stationViews
                        .stream()
                        .filter(e -> e.getHeatTransferStationId().equals(stationId) && "0".equals(e.getNumber()))
                        .collect(BigDecimalStaramUtil.summingBigDecimal(StationFirstNetBaseView::getHeatSystemArea));
                AreaManagerDto dto = new AreaManagerDto();
                dto.setArea(stationArea);
                dto.setRelevanceId(stationId);
                dto.setLevel(TreeLevel.HeatStation.level());
                this.update(dto);
            } else {
                //当前控制柜下系统面积之和（除0系统）
                Integer cabinetId = one.getHeatCabinetId();
                BigDecimal cabinetArea = stationViews
                        .stream()
                        .filter(e -> e.getHeatCabinetId().equals(cabinetId) && !"0".equals(e.getNumber()))
                        .collect(BigDecimalStaramUtil.summingBigDecimal(StationFirstNetBaseView::getHeatSystemArea));
                //检索0系统Id
                Integer zeroSystemId = stationViews
                        .stream()
                        .filter(e -> e.getHeatCabinetId().equals(cabinetId) && "0".equals(e.getNumber()))
                        .map(StationFirstNetBaseView::getHeatSystemId)
                        .findFirst().orElse(-1);
                if (zeroSystemId == -1) {
                    return;
                }
                AreaManagerDto dto = new AreaManagerDto();
                dto.setArea(cabinetArea);
                dto.setRelevanceId(zeroSystemId);
                dto.setLevel(TreeLevel.HeatSystem.level());
                this.update(dto);
            }
        }
    }

    /**
     * 异步更新面积管理表
     *
     * @param dto HeatAreaChangeDto.class
     */
    @Async
    public void updateHistory(HeatAreaChangeDto dto) {
        joggle.insertHeatAreaChange(dto);
    }

    @ApiOperation("站-柜-系统列表查询")
    @GetMapping("{id}")
    public Response list(@PathVariable Integer id) {
        Wrapper<StationFirstNetBaseView> wrapper = Wrappers.<StationFirstNetBaseView>lambdaQuery().eq(StationFirstNetBaseView::getHeatTransferStationId, id);
        return Response.success(stationViewService.list(wrapper));
    }

    @ApiOperation("面积修改历史记录")
    @PostMapping("heatAreaChangeHistory")
    public Response getAreaChangeHistoryList(@RequestBody AreaChangeHistoryDto areaChangeHistoryDto)  {

        try {
            QueryWrapper<AreaChangeHistoryDto> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("relevanceId", areaChangeHistoryDto.getRelevanceId());
            return Response.success(heatAreaChangeHistoryService.queryHeatAreaChangeHistory(new Page<>(areaChangeHistoryDto.getCurrentPage(), areaChangeHistoryDto.getPageCount()), queryWrapper,areaChangeHistoryDto.getType()));
        } catch (Exception e) {
            return Response.fail("查询面积修改历史" + e);
        }

    }


}
