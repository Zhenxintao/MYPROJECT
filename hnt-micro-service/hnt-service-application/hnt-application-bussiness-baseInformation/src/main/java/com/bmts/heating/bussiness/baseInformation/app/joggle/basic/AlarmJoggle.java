package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.bussiness.baseInformation.app.service.CommonServiceAsync;
import com.bmts.heating.commons.auth.entity.response.UserDataPerms;
import com.bmts.heating.commons.auth.service.AuthrityService;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.db.mapper.AlarmRealMapper;
import com.bmts.heating.commons.db.service.*;
import com.bmts.heating.commons.db.service.auth.SysUserService;
import com.bmts.heating.commons.entiy.baseInfo.request.alarm.*;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseUser;
import com.bmts.heating.commons.entiy.baseInfo.response.AlarmHistoryResponse;
import com.bmts.heating.commons.entiy.baseInfo.response.AlarmRealBarResponse;
import com.bmts.heating.commons.entiy.baseInfo.response.AlarmRealResponse;
import com.bmts.heating.commons.entiy.gathersearch.request.SingleAlarmDto;
import com.bmts.heating.commons.entiy.gathersearch.response.history.AlarmCountIndex;
import com.bmts.heating.commons.entiy.gathersearch.response.history.SingleAlarmCountResponse;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
@Api(tags = "报警")
@RequestMapping("/alarmReal")
public class AlarmJoggle {
    @Autowired
    AlarmRealService alarmRealService;
    @Autowired
    AlarmHistoryService alarmHistoryService;
    @Autowired
    AuthrityService authrityService;
    @Autowired
    AlarmHistoryService alarmHistoryServicel;
    @Autowired
    CommonHeatSeasonService commonHeatSeasonService;
    @Autowired
    HeatOrganizationService heatOrganizationService;
    @Autowired
    HeatTransferStationService heatTransferStationService;
    @Autowired
    AlarmRealMapper alarmRealMapper;

    @Autowired
    private CommonServiceAsync commonServiceAsync;

    @GetMapping("/real/{id}")
    public Response querySingle(@PathVariable int id) {
        // alarmRealService.getById(id)
        return Response.success(alarmRealService.queryId(id));
    }

    @GetMapping("/his/{id}")
    public Response querySingleHis(@PathVariable int id) {
        //alarmHistoryService.getById(id)
        return Response.success(alarmHistoryService.queryId(id));
    }

    @PostMapping("/queryHisAlarm")
    @ApiOperation("查询报警历史")
    public Response queryHisAlarm(@RequestBody AlarmRealDto alarmRealDto) {
        try {

            Page<AlarmHistory> page = new Page<>(alarmRealDto.getCurrentPage(), alarmRealDto.getPageCount());
            QueryWrapper<AlarmHistory> queryWrapper = new QueryWrapper<>();
            if (alarmRealDto.getStartTime() != null)
                queryWrapper.ge("ah.alarmTime", alarmRealDto.getStartTime());
            if (alarmRealDto.getEndTime() != null)
                queryWrapper.le("ah.alarmTime", alarmRealDto.getEndTime());
            /*if (alarmRealDto.getClassify() != null) {
                queryWrapper.eq("classify", alarmRealDto.getClassify());
            }*/
            if (alarmRealDto.getIsConfirm() != null && alarmRealDto.getIsConfirm() != 0) {
                if (alarmRealDto.getIsConfirm() == 2) {
                    queryWrapper.eq("ah.status", 1);
                } else {
                    queryWrapper.eq("ah.status", 2);
                }
            }
            if (!"".equals(alarmRealDto.getStationName())) {
                queryWrapper.eq("ah.relevanceName", alarmRealDto.getStationName());
            }
            if (alarmRealDto.getUserId() != -1) {
                UserDataPerms userDataPerms = authrityService.queryStations(alarmRealDto.getUserId());
                if (CollectionUtils.isEmpty(userDataPerms.getStations())) {
                    page.setRecords(null);
                    return Response.success(page);
                } else queryWrapper.in("ah.relevanceId", userDataPerms.getStations());
            }
            //alarmHistoryService.page(page, queryWrapper);
            IPage<AlarmHistoryResponse> alarmHistoryResponseIPage = alarmHistoryService.pageAlarmHis(page, queryWrapper);
            return Response.success(alarmHistoryResponseIPage);
        } catch (Exception e) {
            log.error("query real alarm history cause exception {}", e);
            return Response.fail();
        }

    }

    @PostMapping("/queryAlarmHistoryCount")
    @ApiOperation("查询历史报警数据总条数信息")
    public Response queryAlarmHistoryIndex(@RequestBody AlarmHistoryDto alarmHistoryDto) {
        try {
            QueryWrapper<AlarmHistory> queryAlarmHistorWrapper = new QueryWrapper<>();
            queryAlarmHistorWrapper.between("alarmTime", alarmHistoryDto.getStartTime(), alarmHistoryDto.getEndTime()).func(i -> {
                /*if (alarmHistoryDto.getAlarmType() != null && alarmHistoryDto.getAlarmType() != 0) {
                    i.eq("classify", alarmHistoryDto.getAlarmType());
                }*/
                if (alarmHistoryDto.getStationId() != null && alarmHistoryDto.getStationId() != 0) {
                    i.eq("relevanceId", alarmHistoryDto.getStationId());
                }
                if (alarmHistoryDto.getIsConfirm() != null && alarmHistoryDto.getIsConfirm() != 0) {
                    if (alarmHistoryDto.getIsConfirm() == 2) {
                        i.eq("status", 1);
                    } else {
                        i.eq("status", 2);
                    }
                }
            });
            Integer alarmCount = alarmHistoryServicel.count(queryAlarmHistorWrapper);
            return Response.success(alarmCount);
        } catch (Exception e) {
            log.error("query real alarm history cause exception {}", e);
            return Response.fail();
        }

    }

    @PostMapping("/queryAlarmRealCount")
    @ApiOperation("查询实时报警数据总条数信息")
    public Response queryAlarmRealIndex(@RequestBody AlarmRealCountDto alarmRealCountDto) {
        try {
            QueryWrapper<AlarmReal> queryAlarmRealWrapper = new QueryWrapper<>();
            queryAlarmRealWrapper.func(i -> {
                /*if (alarmRealCountDto.getAlarmType() != null && alarmRealCountDto.getAlarmType() != 0) {
                    i.eq("classify", alarmRealCountDto.getAlarmType());
                }*/
                if (alarmRealCountDto.getStationId() != null && alarmRealCountDto.getStationId() != 0) {
                    i.eq("relevanceId", alarmRealCountDto.getStationId());
                }
                if (alarmRealCountDto.getIsConfirm() != null && alarmRealCountDto.getIsConfirm() != 0) {
                    if (alarmRealCountDto.getIsConfirm() == 2) {
                        i.eq("status", 1);
                    } else {
                        i.eq("status", 2);
                    }
                }
            });
            Integer alarmCount = alarmRealService.count(queryAlarmRealWrapper);
            return Response.success(alarmCount);
        } catch (Exception e) {
            log.error("query real alarm history cause exception {}", e);
            return Response.fail();
        }

    }

    @GetMapping("/queryAlarmRealStatistics")
    @ApiOperation("报警实时总数量")
    public Response queryAlarmRealStatistics(Integer id) {
        try {
            AlarmRealCountDto alarmRealCountDto = new AlarmRealCountDto();
            alarmRealCountDto.setAlarmType(0);
            alarmRealCountDto.setIsConfirm(0);
            alarmRealCountDto.setStationId(id);
            Response response = queryAlarmRealIndex(alarmRealCountDto);
            return response;
        } catch (Exception e) {
            log.error("报警实时总数量接口错误", e);
            return Response.fail();
        }
    }

    @GetMapping("/queryAlarmSumCount")
    @ApiOperation("供暖季历史报警总数量")
    public Response queryAlarmSumCount(Integer id) {
        try {
            AlarmHistoryDto alarmHistoryDto = new AlarmHistoryDto();
            QueryWrapper<CommonHeatSeason> queryCommonHeatSeason = new QueryWrapper<>();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            queryCommonHeatSeason.lt("heatStartTime", df.format(new Date())).gt("heatEndTime", df.format(new Date()));
//            CommonHeatSeason com = commonHeatSeasonService.getOne(queryCommonHeatSeason);
            List<CommonHeatSeason> comList = commonHeatSeasonService.list(queryCommonHeatSeason);
            CommonHeatSeason com = comList.stream().findFirst().orElse(null);
//            Date now = df.parse(df.format(new Date()));//当前时间
//            Date date = df.parse(df.format(com.getHeatStartTime()));//过去
//            long l = now.getTime() - date.getTime();
//            long day = l / (24 * 60 * 60 * 1000);
//            System.out.println("out123123123ssss" + count);
            alarmHistoryDto.setStartTime(com.getHeatStartTime());
            alarmHistoryDto.setEndTime(com.getHeatEndTime());
            alarmHistoryDto.setAlarmType(0);
            alarmHistoryDto.setStationId(id);
            alarmHistoryDto.setIsConfirm(0);
            Response response = queryAlarmHistoryIndex(alarmHistoryDto);
            return response;
        } catch (Exception e) {
            log.error("供暖季历史报警总数量信息接口错误", e);
            return Response.fail();
        }
    }

    @GetMapping("/queryAlarmSumDataCountY")
    @ApiOperation("数据报警已确定总数量")
    public Response queryAlarmSumDataCountY() {
        try {
            AlarmHistoryDto alarmHistoryDto = new AlarmHistoryDto();
            QueryWrapper<CommonHeatSeason> queryCommonHeatSeason = new QueryWrapper<>();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            queryCommonHeatSeason.lt("heatStartTime", df.format(new Date())).gt("heatEndTime", df.format(new Date()));
            CommonHeatSeason com = commonHeatSeasonService.getOne(queryCommonHeatSeason);
            alarmHistoryDto.setStartTime(com.getHeatStartTime());
            alarmHistoryDto.setEndTime(com.getHeatEndTime());
            alarmHistoryDto.setAlarmType(1);
            alarmHistoryDto.setStationId(0);
            alarmHistoryDto.setIsConfirm(1);
            Response response = queryAlarmHistoryIndex(alarmHistoryDto);
            return response;
        } catch (Exception e) {
            log.error("供暖季历史报警总数量信息接口错误", e);
            return Response.fail();
        }
    }

    @GetMapping("/queryAlarmDataMonthY")
    @ApiOperation("数据报警当月已确定总数")
    public Response queryAlarmDataMonthY() {
        try {
            AlarmHistoryDto alarmHistoryDto = new AlarmHistoryDto();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            Calendar cale = null;
            cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            alarmHistoryDto.setStartTime(cale.getTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
            alarmHistoryDto.setEndTime(new Date().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
            alarmHistoryDto.setAlarmType(1);
            alarmHistoryDto.setStationId(0);
            alarmHistoryDto.setIsConfirm(1);
            Response response = queryAlarmHistoryIndex(alarmHistoryDto);
            return response;
        } catch (Exception e) {
            log.error("数据报警当月已确定总数", e);
            return Response.fail();
        }
    }

    @GetMapping("/queryAlarmSumDataCountW")
    @ApiOperation("数据报警未确定总数")
    public Response queryAlarmSumDataCountW() {
        try {
            AlarmHistoryDto alarmHistoryDto = new AlarmHistoryDto();
            QueryWrapper<CommonHeatSeason> queryCommonHeatSeason = new QueryWrapper<>();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            queryCommonHeatSeason.lt("heatStartTime", df.format(new Date())).gt("heatEndTime", df.format(new Date()));
            CommonHeatSeason com = commonHeatSeasonService.getOne(queryCommonHeatSeason);
            alarmHistoryDto.setStartTime(com.getHeatStartTime());
            alarmHistoryDto.setEndTime(com.getHeatEndTime());
            alarmHistoryDto.setAlarmType(1);
            alarmHistoryDto.setStationId(0);
            alarmHistoryDto.setIsConfirm(2);
            Response response = queryAlarmHistoryIndex(alarmHistoryDto);
            return response;
        } catch (Exception e) {
            log.error("供暖季数据报警未确定总数信息接口错误", e);
            return Response.fail();
        }
    }

    @GetMapping("/queryAlarmDataMonthW")
    @ApiOperation("数据报警当月未确定总数")
    public Response queryAlarmDataMonthW() {
        try {
            AlarmHistoryDto alarmHistoryDto = new AlarmHistoryDto();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            Calendar cale = null;
            cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            alarmHistoryDto.setStartTime(cale.getTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
            alarmHistoryDto.setEndTime(new Date().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
            alarmHistoryDto.setAlarmType(1);
            alarmHistoryDto.setStationId(0);
            alarmHistoryDto.setIsConfirm(2);
            Response response = queryAlarmHistoryIndex(alarmHistoryDto);
            return response;
        } catch (Exception e) {
            log.error("数据报警当月未确定总数", e);
            return Response.fail();
        }
    }

    @GetMapping("/queryAlarmFacilityCountY")
    @ApiOperation("设备报警已确定总数")
    public Response queryAlarmFacilityCountY() {
        try {
            AlarmHistoryDto alarmHistoryDto = new AlarmHistoryDto();
            QueryWrapper<CommonHeatSeason> queryCommonHeatSeason = new QueryWrapper<>();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            queryCommonHeatSeason.lt("heatStartTime", df.format(new Date())).gt("heatEndTime", df.format(new Date()));
            CommonHeatSeason com = commonHeatSeasonService.getOne(queryCommonHeatSeason);
            alarmHistoryDto.setStartTime(com.getHeatStartTime());
            alarmHistoryDto.setEndTime(com.getHeatEndTime());
            alarmHistoryDto.setAlarmType(2);
            alarmHistoryDto.setStationId(0);
            alarmHistoryDto.setIsConfirm(1);
            Response response = queryAlarmHistoryIndex(alarmHistoryDto);
            return response;
        } catch (Exception e) {
            log.error("供暖季设备报警已确定总数信息接口错误", e);
            return Response.fail();
        }
    }

    @GetMapping("/queryAlarmFacilityMonthY")
    @ApiOperation("设备报警当月已确定总数")
    public Response queryAlarmFacilityMonthY() {
        try {
            AlarmHistoryDto alarmHistoryDto = new AlarmHistoryDto();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            Calendar cale = null;
            cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            alarmHistoryDto.setStartTime(cale.getTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
            alarmHistoryDto.setEndTime(new Date().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
            alarmHistoryDto.setAlarmType(2);
            alarmHistoryDto.setStationId(0);
            alarmHistoryDto.setIsConfirm(1);
            Response response = queryAlarmHistoryIndex(alarmHistoryDto);
            return response;
        } catch (Exception e) {
            log.error("设备报警当月已确定总数", e);
            return Response.fail();
        }
    }

    @GetMapping("/queryAlarmFacilityCountW")
    @ApiOperation("设备报警未确定总数")
    public Response queryAlarmFacilityCountW() {
        try {
            AlarmHistoryDto alarmHistoryDto = new AlarmHistoryDto();
            QueryWrapper<CommonHeatSeason> queryCommonHeatSeason = new QueryWrapper<>();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            queryCommonHeatSeason.lt("heatStartTime", df.format(new Date())).gt("heatEndTime", df.format(new Date()));
            CommonHeatSeason com = commonHeatSeasonService.getOne(queryCommonHeatSeason);
            alarmHistoryDto.setStartTime(com.getHeatStartTime());
            alarmHistoryDto.setEndTime(com.getHeatEndTime());
            alarmHistoryDto.setAlarmType(2);
            alarmHistoryDto.setStationId(0);
            alarmHistoryDto.setIsConfirm(2);
            Response response = queryAlarmHistoryIndex(alarmHistoryDto);
            return response;
        } catch (Exception e) {
            log.error("供暖季设备报警未确定总数信息接口错误", e);
            return Response.fail();
        }
    }

    @GetMapping("/queryAlarmFacilityMonthW")
    @ApiOperation("设备报警当月未确定总数")
    public Response queryAlarmFacilityMonthW() {
        try {
            AlarmHistoryDto alarmHistoryDto = new AlarmHistoryDto();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            Calendar cale = null;
            cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            alarmHistoryDto.setStartTime(cale.getTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
            alarmHistoryDto.setEndTime(new Date().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
            alarmHistoryDto.setAlarmType(2);
            alarmHistoryDto.setStationId(0);
            alarmHistoryDto.setIsConfirm(2);
            Response response = queryAlarmHistoryIndex(alarmHistoryDto);
            return response;
        } catch (Exception e) {
            log.error("设备报警当月未确定总数", e);
            return Response.fail();
        }
    }

    @GetMapping("/queryAlarmIndexCount")
    @ApiOperation("报警模块数量统计")
    public Response queryAlarmIndexCount() {
        try {
            Integer id = 0;
            AlarmCountIndex alarmCountIndex = new AlarmCountIndex();
            //报警实时统计
            alarmCountIndex.setAlarmStatistics(Integer.parseInt(queryAlarmRealStatistics(id).getData().toString()));
            //报警统计总数
            alarmCountIndex.setAlarmSumCount(Integer.parseInt(queryAlarmSumCount(id).getData().toString()));
            //数据报警已确定总数
            alarmCountIndex.setAlarmSumDataCountY(Integer.parseInt(queryAlarmSumDataCountY().getData().toString()));
            //数据报警当月已确定总数
            alarmCountIndex.setAlarmDataMonthY(Integer.parseInt(queryAlarmDataMonthY().getData().toString()));
            //数据报警未确定总数
            alarmCountIndex.setAlarmSumDataCountW(Integer.parseInt(queryAlarmSumDataCountW().getData().toString()));
            //数据报警当月未确定总数
            alarmCountIndex.setAlarmDataMonthW(Integer.parseInt(queryAlarmDataMonthW().getData().toString()));
            //设备报警已确定总数
            alarmCountIndex.setAlarmFacilityCountY(Integer.parseInt(queryAlarmFacilityCountY().getData().toString()));
            //设备报警当月已确定总数
            alarmCountIndex.setAlarmFacilityMonthY(Integer.parseInt(queryAlarmFacilityMonthY().getData().toString()));
            //设备报警未确定总数
            alarmCountIndex.setAlarmFacilityCountW(Integer.parseInt(queryAlarmFacilityCountW().getData().toString()));
            //设备报警当月未确定总数
            alarmCountIndex.setAlarmFacilityMonthW(Integer.parseInt(queryAlarmFacilityMonthW().getData().toString()));
            //换热站数量
            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("status", true);
            List<HeatTransferStation> heatTransferStationList = heatTransferStationService.listByMap(columnMap);
            alarmCountIndex.setStationCount((int) heatTransferStationList.stream().count());
            return Response.success(alarmCountIndex);
        } catch (Exception e) {
            log.error("报警模块数量统计错误", e);
            return Response.fail();
        }


    }

    @PostMapping("/queryAlarmRealBar")
    @ApiOperation("查询换热站实时报警信息")
    public Response queryAlarmRealBar(@RequestBody AlarmRealBarDto alarmRealBarDto) {
        try {
            QueryWrapper<AlarmReal> queryWrapper = new QueryWrapper<>();
            queryWrapper.func(i -> {
                i.eq("s.status", true);
                /*if (alarmRealBarDto.getAlarmType() != null && alarmRealBarDto.getAlarmType() != 0) {
                    i.eq("a.classify", alarmRealBarDto.getAlarmType());
                }*/
                if (alarmRealBarDto.getHeatorganId() != null && alarmRealBarDto.getHeatorganId() != 0) {
                    QueryWrapper<HeatOrganization> warpperOrganCode = new QueryWrapper<>();
                    warpperOrganCode
                            .eq("id", alarmRealBarDto.getHeatorganId())
                            .select("code");
                    HeatOrganization orgObjCode = heatOrganizationService.getOne(warpperOrganCode);
                    QueryWrapper<HeatOrganization> warpperOrgan = new QueryWrapper<>();
                    warpperOrgan.ne("isEnd", 0)
                            .likeRight("code", orgObjCode.getCode());
                    HeatOrganization orgObj = heatOrganizationService.getOne(warpperOrgan);
                    List<Integer> ids = new ArrayList<>();
                    for (String value : orgObj.getCode().split(":")) {
                        if (!value.equals("root")) {
                            ids.add(Integer.parseInt(value));
                        }
                    }
                    i.in("s.heatOrganizationId", ids);
                }
                if (alarmRealBarDto.getHeatSouceId() != null && alarmRealBarDto.getHeatSouceId() != 0) {
                    i.eq("s.heatSourceId", alarmRealBarDto.getHeatSouceId());
                }
                i.groupBy("a.stationId");
                if (alarmRealBarDto.getSortType() != null) {
                    if (alarmRealBarDto.getSortType() == "ASC") {
                        i.orderByAsc("stationAlarmCount");
                    } else {
                        i.orderByDesc("stationAlarmCount");
                    }
                }
                if (alarmRealBarDto.getBarChartCount() != null && alarmRealBarDto.getBarChartCount() != 0) {
                    i.last(StringUtils.join("limit", " ", alarmRealBarDto.getBarChartCount()));
                }
            });
            List<AlarmRealBarResponse> alarmRealBarResponses = alarmRealService.alarmRealBarResponsesList(queryWrapper);
            return Response.success(alarmRealBarResponses);
        } catch (Exception e) {
            log.error("换热站实时报警信息接口错误", e);
            return Response.fail();
        }

    }

    @PostMapping
    @ApiOperation("查询")
    public Response queryList(@RequestBody AlarmRealDto alarmRealDto) {
        try {
            Page<AlarmReal> page = new Page<>(alarmRealDto.getCurrentPage(), alarmRealDto.getPageCount());
            QueryWrapper<AlarmReal> queryWrapper = new QueryWrapper<>();
            if (alarmRealDto.getStartTime() != null)
                queryWrapper.ge("ar.alarmTime", alarmRealDto.getStartTime());
            if (alarmRealDto.getEndTime() != null) {
                queryWrapper.le("ar.alarmTime", alarmRealDto.getEndTime());
            }
            if (alarmRealDto.getIsConfirm() != null) {
                queryWrapper.eq("ar.status", alarmRealDto.getIsConfirm());
            }
            if (!"".equals(alarmRealDto.getStationName())) {
                queryWrapper.eq("ar.relevanceName", alarmRealDto.getStationName());
            }
            if (alarmRealDto.getUserId() != -1) {
                UserDataPerms userDataPerms = authrityService.queryStations(alarmRealDto.getUserId());
                if (CollectionUtils.isEmpty(userDataPerms.getStations())) {
                    page.setRecords(null);
                    return Response.success(page);
                } else {
                    queryWrapper.in("ar.relevanceId", userDataPerms.getStations());
                }
            }
            //alarmRealService.page(page,queryWrapper);
            IPage<AlarmRealResponse> alarmRealResponseIPage = alarmRealService.pageAlarmReal(page, queryWrapper);
            return Response.success(alarmRealResponseIPage);
        } catch (Exception e) {
            log.error("query real alarm cause exception {}", e);
            return Response.fail();
        }
    }

    @Autowired
    private SysUserService sysUserService;

    @PutMapping("/{id}")
    @ApiOperation("确认")
    public Response confirm(@RequestBody BaseUser baseUser, @PathVariable Integer id) {
        try {
            AlarmReal info = alarmRealService.getById(id);
            if (info != null) {
                info.setStatus(2);
                info.setConfirmTime(LocalDateTime.now());
                info.setConfirmUser(baseUser.getUserName());
                if (alarmRealService.updateById(info)) {
                    commonServiceAsync.loadAlarm();
                }
                if (baseUser.getUserId() == -1) {
                    info.setConfirmUser("tsadmin");
                } else {
                    info.setConfirmUser(baseUser.getUserName());
                }
                if (alarmRealService.updateById(info)) {
                    commonServiceAsync.loadAlarm();
                }
                return Response.success();
            } else {
                return Response.fail("数据不存在");
            }
        } catch (Exception e) {
            log.error("confirm alarm real cause execption {}", e);
            return Response.fail();
        }
    }

    @PostMapping("/confirmAll")
    @ApiOperation("批量确认")
    public Response confirmAll(@RequestBody AlarmConfirmAllDto dto) {
        try {
            List<Integer> ids = Arrays.stream(dto.getIds()).collect(Collectors.toList());
            List<AlarmReal> infoList = alarmRealService.listByIds(ids);
            if (infoList.stream().count() != 0) {
                List<AlarmHistory> alarmHistoryList = new ArrayList<>();
                for (AlarmReal alarmReal : infoList) {
                    alarmReal.setStatus(2);
                    if (dto.getUserId() == -1) {
                        alarmReal.setConfirmUser("tsadmin");
                    } else {
                        alarmReal.setConfirmUser(dto.getUserName());
                    }
                    alarmReal.setConfirmTime(LocalDateTime.now());
                }
                if (alarmRealService.updateBatchById(infoList)) {
                    commonServiceAsync.loadAlarm();
                }
                return Response.success();
            } else {
                return Response.fail("数据不存在");
            }
        } catch (Exception e) {
            log.error("confirm alarm real cause execption {}", e);
            return Response.fail();
        }
    }

    @ApiOperation("单站监测报警个数")
    @PostMapping("/singleStationAlarmCount")
    public Response singleStationAlarmCount(@RequestBody SingleAlarmDto dto) {
        try {
            SingleAlarmCountResponse single = new SingleAlarmCountResponse();
            AlarmHistoryDto determiendDto = new AlarmHistoryDto();
            determiendDto.setStartTime(dto.getStartTime());
            determiendDto.setEndTime(dto.getEndTime());
            determiendDto.setStationId(dto.getStationId());
            determiendDto.setAlarmType(dto.getAlarmType());
            determiendDto.setIsConfirm(1);
            single.setAlarmDetermined(Integer.parseInt(queryAlarmHistoryIndex(determiendDto).getData().toString()));
            AlarmHistoryDto notDetermiendDto = determiendDto;
            notDetermiendDto.setIsConfirm(2);
            single.setAlarmNotDetermined(Integer.parseInt(queryAlarmHistoryIndex(notDetermiendDto).getData().toString()));
            single.setAlarmSumCount(Integer.parseInt(queryAlarmSumCount(dto.getStationId()).getData().toString()));
            single.setAlarmRealCount(Integer.parseInt(queryAlarmRealStatistics(dto.getStationId()).getData().toString()));
            return Response.success(single);
        } catch (Exception e) {
            return Response.fail();
        }

    }

    @ApiOperation("报警时间区间个数")
    @PostMapping("/alarmTimeZonesCount")
    public Response alarmTimeZonesCount(@RequestBody SingleAlarmDto dto) {
        try {
            QueryWrapper<AlarmHistory> alarmHistoryQueryWrapper = new QueryWrapper<>();
            if (dto.getStartTime() != null)
                alarmHistoryQueryWrapper.ge("alarmTime", dto.getStartTime());
            if (dto.getEndTime() != null)
                alarmHistoryQueryWrapper.le("alarmTime", dto.getEndTime());
            /*if (dto.getAlarmType() != null) {
                alarmHistoryQueryWrapper.eq("classify", dto.getAlarmType());
            }*/
            if (dto.getIsConfirm() != null && dto.getIsConfirm() != 0) {
                if (dto.getIsConfirm() == 2) {
                    alarmHistoryQueryWrapper.eq("status", 1);
                } else {
                    alarmHistoryQueryWrapper.eq("status", 2);
                }
            }
            List<AlarmHistory> alarmHistoryList = alarmHistoryService.list(alarmHistoryQueryWrapper);
            List<AlarmHistory> alarmHistoryListResponse = new ArrayList<>();
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder().append(df)
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter();
            for (AlarmHistory alarm : alarmHistoryList) {
                AlarmHistory alarmHistory = alarm;
                String date = df.format(alarm.getAlarmTime());
                LocalDateTime time = LocalDateTime.parse(date, dateTimeFormatter);
                alarmHistory.setAlarmTime(time);
                alarmHistoryListResponse.add(alarmHistory);
            }
            return Response.success(alarmHistoryListResponse);
        } catch (Exception e) {
            return Response.fail();
        }

    }

    @GetMapping
    @ApiOperation("仅供实体响应类说明使用")
    public Response document(@RequestBody AlarmReal real, @RequestBody AlarmHistory history) {
        return Response.success();
    }
}
