package com.bmts.heating.monitor.boot.joggle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.AlarmReal;
import com.bmts.heating.commons.basement.model.db.entity.AlarmStorage;
import com.bmts.heating.commons.basement.model.db.entity.WebPageConfig;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.db.service.AlarmRealService;
import com.bmts.heating.commons.db.service.AlarmStorageService;
import com.bmts.heating.commons.db.service.WebPageConfigService;
import com.bmts.heating.commons.entiy.converge.DegreeBalanceDto;
import com.bmts.heating.commons.entiy.converge.HeatMeteringDto;
import com.bmts.heating.commons.entiy.converge.ReportAlarmDto;
import com.bmts.heating.commons.redis.service.RedisPointService;
import com.bmts.heating.commons.redis.utils.RedisKeyConst;
import com.bmts.heating.commons.utils.auth.SHA1;
import com.bmts.heating.commons.utils.common.SnowIdUtil;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.middleware.cache.services.RedisCacheService;
import com.bmts.heating.monitor.dirver.common.MonitorTaskUtils;
import com.bmts.heating.monitor.dirver.common.ReloadComputation;
import com.bmts.heating.monitor.dirver.common.SimpleMailUtil;
import com.bmts.heating.monitor.dirver.config.MonitorMuster;
import com.bmts.heating.monitor.dirver.config.MonitorProtery;
import com.bmts.heating.monitor.plugins.pvss.constructors.PvssWorker;
import com.bmts.heating.monitor.plugins.pvss.constructors.pvssUtil.Computation;
import com.bmts.heating.monitor.plugins.pvss.constructors.service.PVSSCommonService;
import com.bmts.heating.monitor.plugins.pvss.constructors.service.PvssServiceDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * @ClassName: MonitorJoggle
 * @Description: ????????????
 * @Author: pxf
 * @Date: 2021/4/19 10:20
 * @Version: 1.0
 */

@Slf4j
@Api(tags = "????????????")
@RestController
@RequestMapping("/monitor")
public class MonitorJoggle {

    @Autowired
    private PvssWorker pvssWorker;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private ReloadComputation reloadComputation;

    @Autowired
    private MonitorTaskUtils monitorTaskUtils;

    @Autowired
    private WebPageConfigService webPageConfigService;

    @Autowired
    private SimpleMailUtil simpleMailUtil;

    @Autowired
    private AlarmRealService alarmRealService;

    @Autowired
    private Computation computation;

    @Autowired
    private RedisPointService redisPointService;

    @Autowired
    private PVSSCommonService pvssCommonService;


    @Autowired
    private AlarmStorageService alarmStorageService;


    @Value("${auth.appkey}")
    private String appkey;

    @PostMapping("/receive")
    @ApiOperation("????????????")
    public Response receiveData(@RequestBody String jsonStr) {

        PvssServiceDto pvssServiceDto = new PvssServiceDto();
        pvssServiceDto.setPvssWorker(pvssWorker);

        List<PointL> messageList = new ArrayList<>();
        // ??????????????????
        long lotNo = SnowIdUtil.uniqueLong();
        if (StringUtils.isBlank(jsonStr)) {
            return Response.fail("?????????????????????");
        }
        // ????????????
        JSONObject responseJson = JSONObject.parseObject(jsonStr);
        JSONArray piontList = responseJson.getJSONArray("pointList");
        List<PointL> listPvss = JSONArray.parseArray(piontList.toJSONString(), PointL.class);

//        PvssWorker pvssWorker = new PvssWorker();

        PvssWorker pvssWorkerDto = pvssServiceDto.getPvssWorker();

        pvssWorkerDto.handlePvss(messageList, lotNo, listPvss, jsonStr);
        if (!CollectionUtils.isEmpty(messageList)) {
            System.out.println("????????????????????? " + messageList.size() + " ??? ??????????????????????????????---  " + listPvss.size());
            // ?????? kafka ??????
            pvssWorkerDto.sendKafka(messageList);
        }

        return Response.success();
    }

    @GetMapping("/redis")
    @ApiOperation("??????????????????")
    public Response redisData(@RequestParam String deviceId) throws IOException, MicroException {
        long startTime = System.currentTimeMillis();
        List<PointL> pointList = redisCacheService.getPoints(deviceId);
        System.out.println("grpc----?????????" + (System.currentTimeMillis() - startTime));
        return Response.success(pointList);
    }

    @GetMapping("/reload")
    @ApiOperation("??????????????????")
    public Response reloadThread() throws InterruptedException {
        //????????????????????????????????????
        List<MonitorProtery> mplist = monitorTaskUtils.getRunMonitorProtery();
        // ????????? ?????? pvss
        for (MonitorProtery mp : mplist) {
            if (Objects.equals(mp.getPattern().getModel(), "PVSS")) {
                List<MonitorMuster.Plugin> pluginList = mp.getPluginList();
                pluginList.stream().forEach(e -> {
                    // ??????????????????
                    List<List<PointL>> cachePoints = pvssCommonService.getCachePoints(e.getDevice_id());
                    if (cachePoints != null) {
                        reloadComputation.setMonitorMap(e.getDevice_id(), cachePoints);
                    }
                });
            }
        }

        String subject = "??????????????????????????????-" + LocalDateTime.now();
        String text = "??????????????????---" + LocalDateTime.now() + "--????????????????????????????????????---" + LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        WebPageConfig mailToUser = webPageConfigService.getOne(Wrappers.<WebPageConfig>lambdaQuery().eq(WebPageConfig::getConfigKey, "mailToUser"));
        if (mailToUser != null && StringUtils.isNotBlank(mailToUser.getJsonConfig())) {
            JSONArray jsonArray = JSONArray.parseArray(mailToUser.getJsonConfig());
            simpleMailUtil.sendSimpleMail(subject, jsonArray.toArray(new String[0]), text);
        }
        log.warn("------?????????????????????????????????------{}", LocalDateTime.now());
        return Response.success();
    }


    @GetMapping("/loadAlarm")
    @ApiOperation("????????????????????????")
    public Response loadAlarm() {
        // ???????????????????????? ??????Map
        List<AlarmReal> alarmRealList = alarmRealService.list();
        computation.setAlarmRealMap(alarmRealList);
        return Response.success();
    }


    @ApiOperation(value = "???????????????????????????")
    @PostMapping("/heat_metering")
    public Response heatMetering(@RequestBody List<HeatMeteringDto> list, HttpServletRequest request) {
        if (!validate(request)) {
            log.error("validate header cause execption");
            return Response.fail("?????????????????????");
        }
        log.info("data---{}", list.toString());
        return Response.success();
    }

    @ApiOperation(value = "?????????????????????")
    @PostMapping("/degree_balance")
    public Response degreeBalance(@RequestBody List<DegreeBalanceDto> list, HttpServletRequest request) {

        if (!validate(request)) {
            log.error("validate header cause execption");
            return Response.fail("validate header cause execption");
        }
        log.info("data---{}", list.toString());

        // ????????????
        Map<String, Object> mapbalance = new HashMap();
        list.forEach(x -> {
            mapbalance.put(RedisKeyConst.DEGREE_BALANCE.concat(x.getBuildId()), x);
        });

        if (mapbalance != null) {
            redisPointService.setCacheByKey(mapbalance);
        }
        return Response.success();
    }

    @ApiOperation(value = "??????")
    @PostMapping("/reportAlarm")
    public Response reportAlarm(@RequestBody List<ReportAlarmDto> list, HttpServletRequest request) {

        if (!validate(request)) {
            log.error("validate header cause execption");
            return Response.fail("validate header cause execption");
        }
        log.info("data---{}", list.toString());
        try {
            //if (!CollectionUtils.isEmpty(list) && list.stream().count() > 50) throw new Exception("?????????????????????????????????50???");
            List<AlarmStorage> alarmStorages = new ArrayList<>();
            for (ReportAlarmDto dto : list) {
                AlarmStorage alarm = new AlarmStorage();
                alarm.setSysId(dto.getId());
                alarm.setDescription(dto.getDescription());
                alarm.setValue(dto.getValue());
                alarm.setType(dto.getType());
                alarmStorages.add(alarm);
            }
            if (!CollectionUtils.isEmpty(alarmStorages)) alarmStorageService.saveBatch(alarmStorages);
            return Response.success();
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }

    }


    private boolean validate(HttpServletRequest request) {
        try {
            String nonce = request.getHeader("nonce");
            String nonce1 = request.getHeader("nonce1");
            String appKey = appkey;
            String timestamp = request.getHeader("timestamp");
            log.info("nonce---{},nonce1---{},appKey---{},timestamp---{}", nonce, nonce1, appKey, timestamp);
            if (StringUtils.isBlank(nonce) || StringUtils.isBlank(nonce1) || StringUtils.isBlank(appKey) || StringUtils.isBlank(timestamp)) {
                return false;
            }
            String checkSumHeader = request.getHeader("checkSum");
            log.info("checkSumHeader---{}", checkSumHeader);
            if (SHA1.encode(nonce.concat(nonce1).concat(appKey).concat(timestamp)).toLowerCase().equals(checkSumHeader)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("validate header cause exception {}", e);
            return false;
        }

    }

}
