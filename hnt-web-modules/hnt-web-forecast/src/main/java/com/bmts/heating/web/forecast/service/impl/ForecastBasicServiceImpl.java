package com.bmts.heating.web.forecast.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.entiy.forecast.*;
import com.bmts.heating.commons.entiy.forecast.response.ForecastCoreResponse;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.forecast.service.ForecastBasicService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ForecastBasicServiceImpl extends SavantServices implements ForecastBasicService {
    @Autowired
    private CommonHeatSeasonService commonHeatSeasonService;
    @Autowired
    private ForecastSourceBasicService forecastSourceBasicService;
    @Autowired
    private ForecastSourceCoreService forecastSourceCoreService;
    @Autowired
    private ForecastSourceDetailService forecastSourceDetailService;
    @Autowired
    private ForecastSourceHeatSeasonService forecastSourceHeatSeasonService;
    @Autowired
    private ForecastSourceHistoryService forecastSourceHistoryService;

    // 查询后台当前供暖季信息
    @Override
    public Response queryCommonHeatSeason() {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            List<CommonHeatSeason> comList = commonHeatSeasonService.list(Wrappers.<CommonHeatSeason>lambdaQuery().lt(CommonHeatSeason::getHeatStartTime, df.format(new Date())).gt(CommonHeatSeason::getHeatEndTime, df.format(new Date())));
            CommonHeatSeason com = comList.stream().findFirst().orElse(null);
            if (com != null) {
                return Response.success(com);
            } else {
                return Response.warn("当前供暖季信息未设置");
            }
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @Override
    public Response queryNowSourceBasic() {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            List<ForecastSourceBasic> comList = forecastSourceBasicService.list(Wrappers.<ForecastSourceBasic>lambdaQuery().lt(ForecastSourceBasic::getStartTime, df.format(new Date())).gt(ForecastSourceBasic::getEndTime, df.format(new Date())));
            ForecastSourceBasic com = comList.stream().findFirst().orElse(null);
            if (com != null) {
                return Response.success(com);
            } else {
                return Response.warn("当前时间预测基础信息未设置");
            }
        } catch (Exception e) {
            return Response.fail();
        }
    }

    //查询负荷预测供暖季基础数据信息
    @Override
    public Response queryForecastSourceBasic(BaseDto dto) {
        try {
            IPage<ForecastSourceBasic> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
            QueryWrapper<ForecastSourceBasic> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("name", dto.getKeyWord());
            return Response.success(forecastSourceBasicService.page(page, queryWrapper));
        } catch (Exception e) {
            return Response.fail();
        }
    }

    //新增负荷预测供暖季基础数据信息
    @Override
    public Response insertForecastSourceBasic(ForecastSourceBasic dto) {
        try {
            Boolean result = forecastSourceBasicService.save(dto);
            return result ? Response.success() : Response.fail();
        } catch (Exception e) {
            return Response.fail();
        }
    }

    //修改负荷预测供暖季基础数据信息
    @Override
    public Response updForecastSourceBasic(ForecastSourceBasic dto) {
        try {
            Boolean result = forecastSourceBasicService.updateById(dto);
            if (result && LocalDateTime.now().isAfter(dto.getStartTime()) && LocalDateTime.now().isBefore(dto.getEndTime())) {
                UpdateWrapper<ForecastSourceCore> updateWrapper = new UpdateWrapper<>();
                updateWrapper.set("insideTemp", dto.getInsideDesignTemp());
                return forecastSourceCoreService.update(updateWrapper) ? Response.success() : Response.fail();
            } else {
                return result ? Response.success() : Response.fail();
            }
        } catch (Exception e) {
            return Response.fail();
        }
    }

    //删除负荷预测供暖季基础数据信息
    @Override
    public Response removeForecastSourceBasic(Integer id) {
        try {
            Boolean result = forecastSourceBasicService.removeById(id);
            return result ? Response.success() : Response.fail();
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @Override
    public Response insertForecastConfig(InsertForecastConfigDto dto) {
        try {
            return forecastdetailconfig(dto, 1);
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @Override
    public Response queryForecastConfig(BaseDto dto) {
        try {
            IPage<ForecastSourceCore> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
            IPage<ForecastSourceCore> data = forecastSourceCoreService.page(page);
            List<ForecastCoreResponse> coreResponseList = new ArrayList<>();
            for (ForecastSourceCore core : data.getRecords()) {
                ForecastCoreResponse coreResponse = new ForecastCoreResponse();
                BeanUtils.copyProperties(core, coreResponse);
                List<Compensation> compensationList = JSON.parseArray(core.getCompensation(), Compensation.class);
                List<Dispatch> dispatchList = JSON.parseArray(core.getDispatch(), Dispatch.class);
                List<TempForecast> tempForecasts = JSON.parseArray(core.getTempSetting(), TempForecast.class);
                coreResponse.setCompensationList(compensationList);
                coreResponse.setTempForecasts(tempForecasts);
                coreResponse.setDispatchList(dispatchList);
                List<ForecastSourceDetail> forecastSourceDetailList = forecastSourceDetailService.list(Wrappers.<ForecastSourceDetail>lambdaQuery().eq(ForecastSourceDetail::getForcastSourceCoreId, core.getId()));
                List<ForecastHeatSourceDto> forecastHeatSourceDtoList = new ArrayList<>();
                for (ForecastSourceDetail forecastSourceDetail : forecastSourceDetailList) {
                    ForecastHeatSourceDto forecastHeatSourceDto = new ForecastHeatSourceDto();
                    forecastHeatSourceDto.setHeatSourceName(forecastSourceDetail.getHeatName());
                    forecastHeatSourceDto.setHeatSourceId(forecastSourceDetail.getHeatSourceId());
                    forecastHeatSourceDtoList.add(forecastHeatSourceDto);
                }
                coreResponse.setForecastHeatSourceDtoList(forecastHeatSourceDtoList);
                List<ForecastSourceHeatSeason> forecastSourceHeatSeasonList = forecastSourceHeatSeasonService.list(Wrappers.<ForecastSourceHeatSeason>lambdaQuery().eq(ForecastSourceHeatSeason::getForcastSourceCoreId, core.getId()));
                List<ForecastSourceHeatSeasonDto> forecastSourceHeatSeasonDtoList = JSON.parseArray(JSON.toJSONString(forecastSourceHeatSeasonList), ForecastSourceHeatSeasonDto.class);
                coreResponse.setForecastSourceHeatSeasonDtoList(forecastSourceHeatSeasonDtoList);
                coreResponseList.add(coreResponse);
            }
            PageResponse pageResponse = new PageResponse();
            pageResponse.setTotal(data.getTotal());
            pageResponse.setRecords(coreResponseList);
            return Response.success(pageResponse);
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @Override
    public Response updForecastConfig(InsertForecastConfigDto dto) {
        try {
            return forecastdetailconfig(dto, 2);
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @Override
    public Response removeForecastConfig(Integer id) {
        try {
            if (forecastSourceCoreService.removeById(id)) {
                Boolean removeHistory = forecastSourceHistoryService.remove(Wrappers.<ForecastSourceHistory>lambdaQuery().eq(ForecastSourceHistory::getForecastSourceCoreId, id));
                if (removeHistory) {
                    Boolean removeStatus = forecastSourceDetailService.remove(Wrappers.<ForecastSourceDetail>lambdaQuery().eq(ForecastSourceDetail::getForcastSourceCoreId, id));
                    if (removeStatus) {
                        Boolean removeStatusSeason = forecastSourceHeatSeasonService.remove(Wrappers.<ForecastSourceHeatSeason>lambdaQuery().eq(ForecastSourceHeatSeason::getForcastSourceCoreId, id));
                        return removeStatusSeason ? Response.success() : Response.fail();
                    }
                    return Response.success();
                }
            }
            return Response.fail();
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @Override
    public Response forecastdetailconfig(InsertForecastConfigDto dto, Integer type) {
        try {
            ForecastSourceCore core = forecastSourceCore(dto);
            if (type == 1) {
                Boolean result = forecastSourceCoreService.save(core);
                if (result) {
                    getForecastSourceHeatSeason(core.getId(), dto);
                    List<ForecastSourceDetail> forecastSourceDetailList = forecastSourceDetailList(dto, core.getId());
                    return forecastSourceDetailService.saveBatch(forecastSourceDetailList) ? Response.success() : Response.fail();
                }
            } else {
                core.setId(dto.getId());
                Boolean result = forecastSourceCoreService.updateById(core);
                if (result) {
                    Boolean removeHeatSeason = forecastSourceHeatSeasonService.remove(Wrappers.<ForecastSourceHeatSeason>lambdaQuery().eq(ForecastSourceHeatSeason::getForcastSourceCoreId, core.getId()));
                    if (removeHeatSeason) {
                        getForecastSourceHeatSeason(core.getId(), dto);
                    }
                    Boolean removeStatus = forecastSourceDetailService.remove(Wrappers.<ForecastSourceDetail>lambdaQuery().eq(ForecastSourceDetail::getForcastSourceCoreId, core.getId()));
                    if (removeStatus) {
                        List<ForecastSourceDetail> forecastSourceDetailList = forecastSourceDetailList(dto, core.getId());
                        return forecastSourceDetailService.saveBatch(forecastSourceDetailList) ? Response.success() : Response.fail();
                    }
                    return Response.fail();
                }
            }
            return Response.fail();
        } catch (Exception e) {
            return Response.fail();
        }

    }

    public Boolean getForecastSourceHeatSeason(Integer coreId, InsertForecastConfigDto dto) {
        List<ForecastSourceHeatSeason> forecastSourceHeatSeasonList = new ArrayList<>();
        for (ForecastSourceHeatSeasonDto forecastSourceHeatSeasonDto : dto.getForecastSourceHeatSeasonDtoList()) {
            ForecastSourceHeatSeason f = new ForecastSourceHeatSeason();
            BeanUtils.copyProperties(forecastSourceHeatSeasonDto, f);
            f.setForcastSourceCoreId(coreId);
            forecastSourceHeatSeasonList.add(f);
        }
        return forecastSourceHeatSeasonService.saveBatch(forecastSourceHeatSeasonList);
    }

    public ForecastSourceCore forecastSourceCore(InsertForecastConfigDto dto) {
        ForecastSourceCore core = new ForecastSourceCore();
        core.setName(dto.getName());
        core.setAreaHeatingIndex(dto.getAreaHeatingIndex());
        core.setPredictHeatRate(dto.getPredictHeatRate());
        core.setActualHeatRate(dto.getActualHeatRate());
        core.setHeatArea(dto.getHeatArea());
        core.setInsideTemp(dto.getInsideTemp());
        core.setFirstNetTg(dto.getFirstNetTg());
        core.setComputeType(dto.getComputeType());
        core.setFirstNetTh(dto.getFirstNetTh());
        core.setFirstNetCycleFlow(dto.getFirstNetCycleFlow());
        core.setIsValid(dto.getIsValid());
        core.setType(dto.getType());
        core.setRadiatorTg(dto.getRadiatorTg());
        core.setRadiatorTh(dto.getRadiatorTh());
        core.setFloorTg(dto.getFloorTg());
        core.setFloorTh(dto.getFloorTh());
        core.setRadiatingSpace(dto.getRadiatingSpace());
        String compensation = JSON.toJSONString(dto.getCompensationList());
        String dispatch = JSON.toJSONString(dto.getDispatchList());
        core.setCompensation(compensation);
        core.setDispatch(dispatch);
        core.setDesiredValue(dto.getDesiredValue());
        if (dto.getTempForecasts() != null)
            core.setTempSetting(JSON.toJSONString(dto.getTempForecasts()));
        return core;
    }

    public List<ForecastSourceDetail> forecastSourceDetailList(InsertForecastConfigDto dto, Integer id) {
        List<ForecastSourceDetail> forecastSourceDetailList = new ArrayList<>();
        for (ForecastHeatSourceDto heatSource : dto.getForecastHeatSourceDtoList()) {
            ForecastSourceDetail forecastSourceDetail = new ForecastSourceDetail();
            forecastSourceDetail.setForcastSourceCoreId(id);
            forecastSourceDetail.setHeatSourceId(heatSource.getHeatSourceId());
            forecastSourceDetail.setHeatName(heatSource.getHeatSourceName());
            forecastSourceDetailList.add(forecastSourceDetail);
        }
        return forecastSourceDetailList;
    }

    @Override
    public Response updForecastHeatSeason(ForecastSourceHeatSeason dto) {
        try {
            Boolean result = forecastSourceHeatSeasonService.updateById(dto);
            return result ? Response.success() : Response.fail();
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @Override
    public Response insertForecastSourceHeatSeason(ForecastSourceHeatSeason dto) {
        try {
            Boolean result = forecastSourceHeatSeasonService.save(dto);
            return result ? Response.success() : Response.fail();
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @Override
    public Response removeForecastSourceHeatSeason(Integer id) {
        try {
            Boolean result = forecastSourceHeatSeasonService.removeById(id);
            return result ? Response.success() : Response.fail();
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @Override
    public Response forecastAreaIndex(ForecastAreaHotIndexDto dto) {
        try {
            ForecastSourceBasic com = forecastSourceBasic();
            LocalDate startTime = com.getStartTime().toLocalDate();
            LocalDate endTime = com.getEndTime().toLocalDate();
            Period period = Period.between(startTime, endTime);
            double hotAreaIndex = ((dto.getHotEnergy() * Math.pow(10, 6)) / (24 * period.getDays() * 3.6))
                    * ((dto.getTn() - com.getOutsideConfigTemp().floatValue()) / (dto.getTn() - com.getOutsideComputeTempAvg().floatValue()));
            return Response.success(hotAreaIndex);
        } catch (Exception e) {
            return Response.fail();
        }
    }

    @Override
    public Response forecastFirstNetFlow(Integer id, float circulationValue, float flowValue) {
        if (id == 0) {
            float firstNetFlow = flowValue / circulationValue;
            return Response.success(firstNetFlow);
        } else {
            ForecastSourceCore forecastSourceCore = forecastSourceCoreService.getById(id);
            float value = flowValue / forecastSourceCore.getFirstNetCycleFlow().floatValue();
            return Response.success(value);
        }
    }

    public ForecastSourceBasic forecastSourceBasic() {
        QueryWrapper<ForecastSourceBasic> queryCommonHeatSeason = new QueryWrapper<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        queryCommonHeatSeason.lt("startTime", df.format(new Date())).gt("endTime", df.format(new Date()));
        List<ForecastSourceBasic> comList = forecastSourceBasicService.list(queryCommonHeatSeason);
        ForecastSourceBasic com = comList.stream().findFirst().orElse(null);
        return com;
    }

}
