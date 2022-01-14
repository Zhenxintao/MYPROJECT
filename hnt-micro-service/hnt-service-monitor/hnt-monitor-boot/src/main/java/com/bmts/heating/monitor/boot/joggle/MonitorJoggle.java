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
 * @Description: 采集测试
 * @Author: pxf
 * @Date: 2021/4/19 10:20
 * @Version: 1.0
 */

@Slf4j
@Api(tags = "采集管理")
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
    @ApiOperation("接收数据")
    public Response receiveData(@RequestBody String jsonStr) {

        PvssServiceDto pvssServiceDto = new PvssServiceDto();
        pvssServiceDto.setPvssWorker(pvssWorker);

        List<PointL> messageList = new ArrayList<>();
        // 生成统一批号
        long lotNo = SnowIdUtil.uniqueLong();
        if (StringUtils.isBlank(jsonStr)) {
            return Response.fail("接收数据为空！");
        }
        // 接收数据
        JSONObject responseJson = JSONObject.parseObject(jsonStr);
        JSONArray piontList = responseJson.getJSONArray("pointList");
        List<PointL> listPvss = JSONArray.parseArray(piontList.toJSONString(), PointL.class);

//        PvssWorker pvssWorker = new PvssWorker();

        PvssWorker pvssWorkerDto = pvssServiceDto.getPvssWorker();

        pvssWorkerDto.handlePvss(messageList, lotNo, listPvss, jsonStr);
        if (!CollectionUtils.isEmpty(messageList)) {
            System.out.println("推送的数据有： " + messageList.size() + " 条 数据，原始数据是有：---  " + listPvss.size());
            // 发送 kafka 消息
            pvssWorkerDto.sendKafka(messageList);
        }

        return Response.success();
    }

    @GetMapping("/redis")
    @ApiOperation("查询缓存数据")
    public Response redisData(@RequestParam String deviceId) throws IOException, MicroException {
        long startTime = System.currentTimeMillis();
        List<PointL> pointList = redisCacheService.getPoints(deviceId);
        System.out.println("grpc----耗时：" + (System.currentTimeMillis() - startTime));
        return Response.success(pointList);
    }

    @GetMapping("/reload")
    @ApiOperation("重新加载数据")
    public Response reloadThread() throws InterruptedException {
        //获取所有可执行的任务实例
        List<MonitorProtery> mplist = monitorTaskUtils.getRunMonitorProtery();
        // 只加载 处理 pvss
        for (MonitorProtery mp : mplist) {
            if (Objects.equals(mp.getPattern().getModel(), "PVSS")) {
                List<MonitorMuster.Plugin> pluginList = mp.getPluginList();
                pluginList.stream().forEach(e -> {
                    // 加载点表数据
                    List<List<PointL>> cachePoints = pvssCommonService.getCachePoints(e.getDevice_id());
                    if (cachePoints != null) {
                        reloadComputation.setMonitorMap(e.getDevice_id(), cachePoints);
                    }
                });
            }
        }

        String subject = "晋城采集任务重新加载-" + LocalDateTime.now();
        String text = "晋城采集服务---" + LocalDateTime.now() + "--采集任务重新加载！时间戳---" + LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        WebPageConfig mailToUser = webPageConfigService.getOne(Wrappers.<WebPageConfig>lambdaQuery().eq(WebPageConfig::getConfigKey, "mailToUser"));
        if (mailToUser != null && StringUtils.isNotBlank(mailToUser.getJsonConfig())) {
            JSONArray jsonArray = JSONArray.parseArray(mailToUser.getJsonConfig());
            simpleMailUtil.sendSimpleMail(subject, jsonArray.toArray(new String[0]), text);
        }
        log.warn("------采集服务重新加载成功！------{}", LocalDateTime.now());
        return Response.success();
    }


    @GetMapping("/loadAlarm")
    @ApiOperation("加载最新报警数据")
    public Response loadAlarm() {
        // 加载实时报警数据 进入Map
        List<AlarmReal> alarmRealList = alarmRealService.list();
        computation.setAlarmRealMap(alarmRealList);
        return Response.success();
    }


    @ApiOperation(value = "接收热计量实时数据")
    @PostMapping("/heat_metering")
    public Response heatMetering(@RequestBody List<HeatMeteringDto> list, HttpServletRequest request) {
        if (!validate(request)) {
            log.error("validate header cause execption");
            return Response.fail("没有访问权限！");
        }
        log.info("data---{}", list.toString());
        return Response.success();
    }

    @ApiOperation(value = "接收小区平衡度")
    @PostMapping("/degree_balance")
    public Response degreeBalance(@RequestBody List<DegreeBalanceDto> list, HttpServletRequest request) {

        if (!validate(request)) {
            log.error("validate header cause execption");
            return Response.fail("validate header cause execption");
        }
        log.info("data---{}", list.toString());

        // 写入缓存
        Map<String, Object> mapbalance = new HashMap();
        list.forEach(x -> {
            mapbalance.put(RedisKeyConst.DEGREE_BALANCE.concat(x.getBuildId()), x);
        });

        if (mapbalance != null) {
            redisPointService.setCacheByKey(mapbalance);
        }
        return Response.success();
    }

    @ApiOperation(value = "告警")
    @PostMapping("/reportAlarm")
    public Response reportAlarm(@RequestBody List<ReportAlarmDto> list, HttpServletRequest request) {

        if (!validate(request)) {
            log.error("validate header cause execption");
            return Response.fail("validate header cause execption");
        }
        log.info("data---{}", list.toString());
        try {
            //if (!CollectionUtils.isEmpty(list) && list.stream().count() > 50) throw new Exception("请减少消息体数量，上限50条");
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
