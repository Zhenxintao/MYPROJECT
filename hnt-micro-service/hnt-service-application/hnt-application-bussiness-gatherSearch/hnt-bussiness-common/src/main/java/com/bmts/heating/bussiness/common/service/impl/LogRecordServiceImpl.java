package com.bmts.heating.bussiness.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.bussiness.common.service.*;
import com.bmts.heating.commons.basement.model.cache.FirstNetBase;
import com.bmts.heating.commons.basement.model.db.entity.HeatSystem;
import com.bmts.heating.commons.basement.model.db.entity.LogOperationControl;
import com.bmts.heating.commons.db.service.HeatSystemService;
import com.bmts.heating.commons.db.service.LogOperationControlService;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.BPModeDto;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.CVModeDto;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.XPModeDto;
import com.bmts.heating.commons.entiy.gathersearch.request.issue.XSVModeDto;
import com.bmts.heating.commons.entiy.gathersearch.response.issue.BPModeResponse;
import com.bmts.heating.commons.entiy.gathersearch.response.issue.CVModeResponse;
import com.bmts.heating.commons.entiy.gathersearch.response.issue.XPModeResponse;
import com.bmts.heating.commons.entiy.gathersearch.response.issue.XSVModeResponse;
import com.bmts.heating.commons.utils.restful.Response;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName: LogRecordServiceImpl
 * @Description:
 * @Author: pxf
 * @Date: 2021/3/4 17:29
 * @Version: 1.0
 */
@Slf4j
@Service
public class LogRecordServiceImpl implements LogRecordService {

    @Autowired
    private BPModeService bpModeService;

    @Autowired
    private CVModeService cvModeService;

    @Autowired
    private XPModeService xpModeService;

    @Autowired
    private XSVModeService xsvModeService;

    @Autowired
    private LogOperationControlService logOperationControlService;

    @Autowired
    private HeatSystemService heatSystemService;


    @Override
    public Boolean recordBPMode(BPModeDto dto, int type) {
        switch (type) {
            // 二次补水变频工作方式 0:定值
            case 0:
                return bpModeZero(dto);
            // 二次补水变频工作方式 1:恒定二次回压
            case 1:
                return bpModeOne(dto);
            // 二次补水变频工作方式 2:高低限自动（电节点压力表方式）
            case 2:
                return bpModeTwo(dto);
            // 二次补水变频泵的启停
            case 3:
                return bpModeThree(dto);
        }
        return false;
    }

    @Override
    public Boolean recordCVMode(CVModeDto dto) {
        return cvModeRecord(dto);
    }

    @Override
    public Boolean recordXPMode(XPModeDto dto, int type) {
        switch (type) {
            // 二次循环变频工作方式   0:定值   1:恒定二次供回压差   2:恒定二次供压
            case 1:
                return xpModeOne(dto);
            // 二次循环变频泵的启停
            case 3:
                return xpModeThree(dto);
        }
        return false;
    }

    @Override
    public Boolean recordXSVMode(XSVModeDto dto) {
        return xsvModeRecord(dto);
    }


    private Boolean bpModeZero(BPModeDto dto) {
        Response query = bpModeService.query(dto.getSystemId());
        Gson gson = new Gson();
        BPModeResponse bpModeResponse = gson.fromJson(gson.toJson(query.getData()), BPModeResponse.class);
        LogOperationControl logOperation = new LogOperationControl();
        // 操作时间
        logOperation.setOperationTime(LocalDateTime.now());
        logOperation.setIp(dto.getIp());
        logOperation.setUserName(dto.getUserName());
        logOperation.setDeviceName(" 二次补水变频泵");
        logOperation.setMode("二次补水变频工作方式 0:定值");
        // 操作类型 ：1、阀门动作   2、变频动作
        logOperation.setType(2);
        QueryWrapper<HeatSystem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hss.id", dto.getSystemId());
        queryWrapper.eq("hss.deleteFlag", 0);
        List<FirstNetBase> firstNetBases = heatSystemService.querySystem(queryWrapper);
        if (CollectionUtils.isEmpty(firstNetBases)) {
            log.error("控制下发日记录失败！没有查询到 {} 对应的机组信息！", dto.getSystemId());
            return false;
        }
        logOperation.setCabinetName(firstNetBases.get(0).getHeatCabinetName());
        logOperation.setCompany(firstNetBases.get(0).getHeatStationOrgName());
        logOperation.setStationName(firstNetBases.get(0).getHeatTransferStationName());
        logOperation.setSystemName(firstNetBases.get(0).getHeatSystemName());
        // 拼接原始数据
        JSONObject oldValue = new JSONObject(true);
        Integer options = bpModeResponse.getRead().getOptions();
        if (options == null) {
            return false;
        }
        if (options == 0) {
            oldValue.put("原来工作模式", bpModeResponse.getRead().getOptions() + " :定值");
        }
        if (options == 1) {
            oldValue.put("原来工作模式", bpModeResponse.getRead().getOptions() + " :恒定二次回压");
        }
        if (options == 2) {
            oldValue.put("原来工作模式", bpModeResponse.getRead().getOptions() + " :高低限自动");
        }
        oldValue.put("频率设定值", bpModeResponse.getRead().getBpMsP());
        oldValue.put("二次回压设定值", bpModeResponse.getRead().getBpPhSP());
        oldValue.put("自动高限设定值", bpModeResponse.getRead().getBpP2hH());
        oldValue.put("自动低限设定值", bpModeResponse.getRead().getBpP2hL());
        logOperation.setOldValue(oldValue.toJSONString().replaceAll("[{}]", ""));

        // 拼接修改的值
        JSONObject newValue = new JSONObject(true);
        newValue.put("工作模式调整为", dto.getOptions() + " :定值");
        newValue.put("频率设定值为", dto.getBpMsP());
        logOperation.setNewValue(newValue.toJSONString().replaceAll("[{}]", ""));
        logOperation.setCreateTime(LocalDateTime.now());
        return logOperationControlService.save(logOperation);
    }

    private Boolean bpModeOne(BPModeDto dto) {
        Response query = bpModeService.query(dto.getSystemId());
        Gson gson = new Gson();
        BPModeResponse bpModeResponse = gson.fromJson(gson.toJson(query.getData()), BPModeResponse.class);
        LogOperationControl logOperation = new LogOperationControl();
        // 操作时间
        logOperation.setOperationTime(LocalDateTime.now());
        logOperation.setIp(dto.getIp());
        logOperation.setUserName(dto.getUserName());
        logOperation.setDeviceName(" 二次补水变频泵");
        logOperation.setMode("二次补水变频工作方式 1:恒定二次回压");
        // 操作类型 ：1、阀门动作   2、变频动作
        logOperation.setType(2);
        QueryWrapper<HeatSystem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hss.id", dto.getSystemId());
        queryWrapper.eq("hss.deleteFlag", 0);
        List<FirstNetBase> firstNetBases = heatSystemService.querySystem(queryWrapper);
        if (CollectionUtils.isEmpty(firstNetBases)) {
            log.error("控制下发日记录失败！没有查询到 {} 对应的机组信息！", dto.getSystemId());
            return false;
        }
        logOperation.setCabinetName(firstNetBases.get(0).getHeatCabinetName());
        logOperation.setCompany(firstNetBases.get(0).getHeatStationOrgName());
        logOperation.setStationName(firstNetBases.get(0).getHeatTransferStationName());
        logOperation.setSystemName(firstNetBases.get(0).getHeatSystemName());
        // 拼接原始数据
        JSONObject oldValue = new JSONObject(true);
        Integer options = bpModeResponse.getRead().getOptions();
        if (options == null) {
            return false;
        }
        if (options == 0) {
            oldValue.put("原来工作模式", bpModeResponse.getRead().getOptions() + " :定值");
        }
        if (options == 1) {
            oldValue.put("原来工作模式", bpModeResponse.getRead().getOptions() + " :恒定二次回压");
        }
        if (options == 2) {
            oldValue.put("原来工作模式", bpModeResponse.getRead().getOptions() + " :高低限自动");
        }
        oldValue.put("频率设定值", bpModeResponse.getRead().getBpMsP());
        oldValue.put("二次回压设定值", bpModeResponse.getRead().getBpPhSP());
        oldValue.put("自动高限设定值", bpModeResponse.getRead().getBpP2hH());
        oldValue.put("自动低限设定值", bpModeResponse.getRead().getBpP2hL());
        logOperation.setOldValue(oldValue.toJSONString().replaceAll("[{}]", ""));

        // 拼接修改的值
        JSONObject newValue = new JSONObject(true);
        newValue.put("工作模式调整为", dto.getOptions() + " :恒定二次回压");
        newValue.put("二次回压设定值为", dto.getBpPhSP());
        logOperation.setNewValue(newValue.toJSONString().replaceAll("[{}]", ""));
        logOperation.setCreateTime(LocalDateTime.now());
        return logOperationControlService.save(logOperation);

    }

    private Boolean bpModeTwo(BPModeDto dto) {
        Response query = bpModeService.query(dto.getSystemId());
        Gson gson = new Gson();
        BPModeResponse bpModeResponse = gson.fromJson(gson.toJson(query.getData()), BPModeResponse.class);
        LogOperationControl logOperation = new LogOperationControl();
        // 操作时间
        logOperation.setOperationTime(LocalDateTime.now());
        logOperation.setIp(dto.getIp());
        logOperation.setUserName(dto.getUserName());
        logOperation.setDeviceName(" 二次补水变频泵");
        logOperation.setMode("二次补水变频工作方式 2:高低限自动");
        // 操作类型 ：1、阀门动作   2、变频动作
        logOperation.setType(2);
        QueryWrapper<HeatSystem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hss.id", dto.getSystemId());
        queryWrapper.eq("hss.deleteFlag", 0);
        List<FirstNetBase> firstNetBases = heatSystemService.querySystem(queryWrapper);
        if (CollectionUtils.isEmpty(firstNetBases)) {
            log.error("控制下发日记录失败！没有查询到 {} 对应的机组信息！", dto.getSystemId());
            return false;
        }
        logOperation.setCabinetName(firstNetBases.get(0).getHeatCabinetName());
        logOperation.setCompany(firstNetBases.get(0).getHeatStationOrgName());
        logOperation.setStationName(firstNetBases.get(0).getHeatTransferStationName());
        logOperation.setSystemName(firstNetBases.get(0).getHeatSystemName());
        // 拼接原始数据
        JSONObject oldValue = new JSONObject(true);
        Integer options = bpModeResponse.getRead().getOptions();
        if (options == null) {
            return false;
        }
        if (options == 0) {
            oldValue.put("原来工作模式", bpModeResponse.getRead().getOptions() + " :定值");
        }
        if (options == 1) {
            oldValue.put("原来工作模式", bpModeResponse.getRead().getOptions() + " :恒定二次回压");
        }
        if (options == 2) {
            oldValue.put("原来工作模式", bpModeResponse.getRead().getOptions() + " :高低限自动");
        }
        oldValue.put("频率设定值", bpModeResponse.getRead().getBpMsP());
        oldValue.put("二次回压设定值", bpModeResponse.getRead().getBpPhSP());
        oldValue.put("自动高限设定值", bpModeResponse.getRead().getBpP2hH());
        oldValue.put("自动低限设定值", bpModeResponse.getRead().getBpP2hL());
        logOperation.setOldValue(oldValue.toJSONString().replaceAll("[{}]", ""));

        // 拼接修改的值
        JSONObject newValue = new JSONObject(true);
        newValue.put("工作模式调整为", dto.getOptions() + " :高低限自动");
        newValue.put("自动高限设定值为", dto.getBpP2hH());
        newValue.put("自动低限设定值为", dto.getBpP2hL());
        logOperation.setNewValue(newValue.toJSONString().replaceAll("[{}]", ""));
        logOperation.setCreateTime(LocalDateTime.now());
        return logOperationControlService.save(logOperation);

    }

    private Boolean bpModeThree(BPModeDto dto) {
        Response query = bpModeService.query(dto.getSystemId());
        Gson gson = new Gson();
        BPModeResponse bpModeResponse = gson.fromJson(gson.toJson(query.getData()), BPModeResponse.class);
        LogOperationControl logOperation = new LogOperationControl();
        // 操作时间
        logOperation.setOperationTime(LocalDateTime.now());
        logOperation.setIp(dto.getIp());
        logOperation.setUserName(dto.getUserName());
        logOperation.setDeviceName(" 二次补水变频泵");
        logOperation.setMode("泵启停操作");
        // 操作类型 ：1、阀门动作   2、变频动作
        logOperation.setType(2);
        QueryWrapper<HeatSystem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hss.id", dto.getSystemId());
        queryWrapper.eq("hss.deleteFlag", 0);
        List<FirstNetBase> firstNetBases = heatSystemService.querySystem(queryWrapper);
        if (CollectionUtils.isEmpty(firstNetBases)) {
            log.error("控制下发日记录失败！没有查询到 {} 对应的机组信息！", dto.getSystemId());
            return false;
        }
        logOperation.setCabinetName(firstNetBases.get(0).getHeatCabinetName());
        logOperation.setCompany(firstNetBases.get(0).getHeatStationOrgName());
        logOperation.setStationName(firstNetBases.get(0).getHeatTransferStationName());
        logOperation.setSystemName(firstNetBases.get(0).getHeatSystemName());
        // 拼接原始数据
        JSONObject oldValue = new JSONObject(true);
        Integer runStatus = bpModeResponse.getRead().getRunStatus();
        oldValue.put("泵原来状态", runStatus == 0 ? runStatus + " :停止" : runStatus + " :运行");
        oldValue.put("补水启动令", bpModeResponse.getRead().getStart());
        oldValue.put("补水停止令", bpModeResponse.getRead().getStop());
        logOperation.setOldValue(oldValue.toJSONString().replaceAll("[{}]", ""));

        // 拼接修改的值
        JSONObject newValue = new JSONObject(true);
        Integer start = dto.getStart();
        Integer stop = dto.getStop();
        Integer status = null;
        if (start == 1 && stop == 0) {
            status = 1;
        }
        if (start == 0 && stop == 1) {
            status = 0;
        }

        newValue.put("泵状态改为", status == null ? " 无效" : (status == 1 ? status + " :启动" : status + " :停止"));
        newValue.put("补水启动令为", start);
        newValue.put("补水停止为", stop);
        logOperation.setNewValue(newValue.toJSONString().replaceAll("[{}]", ""));
        logOperation.setCreateTime(LocalDateTime.now());
        return logOperationControlService.save(logOperation);
    }


    private Boolean cvModeRecord(CVModeDto dto) {
        Response query = cvModeService.query(dto.getSystemId());
        Gson gson = new Gson();
        CVModeResponse cvModeResponse = gson.fromJson(gson.toJson(query.getData()), CVModeResponse.class);
        LogOperationControl logOperation = new LogOperationControl();
        // 操作时间
        logOperation.setOperationTime(LocalDateTime.now());
        logOperation.setIp(dto.getIp());
        logOperation.setUserName(dto.getUserName());
        logOperation.setDeviceName(" 一次调节阀");
        // 操作类型 ：1、阀门动作   2、变频动作
        logOperation.setType(1);
        QueryWrapper<HeatSystem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hss.id", dto.getSystemId());
        queryWrapper.eq("hss.deleteFlag", 0);
        List<FirstNetBase> firstNetBases = heatSystemService.querySystem(queryWrapper);
        if (CollectionUtils.isEmpty(firstNetBases)) {
            log.error("控制下发日记录失败！没有查询到 {} 对应的机组信息！", dto.getSystemId());
            return false;
        }
        logOperation.setCabinetName(firstNetBases.get(0).getHeatCabinetName());
        logOperation.setCompany(firstNetBases.get(0).getHeatStationOrgName());
        logOperation.setStationName(firstNetBases.get(0).getHeatTransferStationName());
        logOperation.setSystemName(firstNetBases.get(0).getHeatSystemName());
        // 拼接原始数据
        JSONObject oldValue = new JSONObject(true);
        // 原来的工作模式
        Integer options = cvModeResponse.getOptions();
        if (options == null) {
            return false;
        }
        if (options == 0) {
            // 一次调节阀工作方式 0:定值
            oldValue.put("原来工作模式", cvModeResponse.getOptions() + " :定值");
            oldValue.put("一次阀门给定开度值", cvModeResponse.getCvSV());
            oldValue.put("一次阀门实际开度", cvModeResponse.getCvU());
        }
        if (options == 1) {
            // 一次调节阀工作方式 1:二次供温恒定
            oldValue.put("原来工作模式", cvModeResponse.getOptions() + " :二次供温恒定");
            oldValue.put("恒定二次供温设定值", cvModeResponse.getCvTg().getCvTgSP());
            oldValue.put("实时供温", cvModeResponse.getCvTg().getT2g());
            oldValue.put("二次供温0时段偏差设定值", cvModeResponse.getCvTg().getTgSPTime0());
            oldValue.put("二次供温2时段偏差设定值", cvModeResponse.getCvTg().getTgSPTime1());
            oldValue.put("二次供温4时段偏差设定值", cvModeResponse.getCvTg().getTgSPTime2());
            oldValue.put("二次供温6时段偏差设定值", cvModeResponse.getCvTg().getTgSPTime3());
            oldValue.put("二次供温8时段偏差设定值", cvModeResponse.getCvTg().getTgSPTime4());
            oldValue.put("二次供温10时段偏差设定值", cvModeResponse.getCvTg().getTgSPTime5());
            oldValue.put("二次供温12时段偏差设定值", cvModeResponse.getCvTg().getTgSPTime6());
            oldValue.put("二次供温14时段偏差设定值", cvModeResponse.getCvTg().getTgSPTime7());
            oldValue.put("二次供温16时段偏差设定值", cvModeResponse.getCvTg().getTgSPTime8());
            oldValue.put("二次供温18时段偏差设定值", cvModeResponse.getCvTg().getTgSPTime9());
            oldValue.put("二次供温20时段偏差设定值", cvModeResponse.getCvTg().getTgSPTime10());
            oldValue.put("二次供温22时段偏差设定值", cvModeResponse.getCvTg().getTgSPTime11());
            oldValue.put("一次调节阀设置步进值", cvModeResponse.getCvStep());
            oldValue.put("一次调节阀最小开度值", cvModeResponse.getCvMin());
            oldValue.put("一次调节阀最大开度值", cvModeResponse.getCvMax());
        }
        if (options == 4) {
            // 一次调节阀工作方式 4:恒流
            oldValue.put("原来工作模式", cvModeResponse.getOptions() + " :恒流");
            oldValue.put("恒定流量设定值", cvModeResponse.getCvFSP());
            oldValue.put("一次调节阀设置步进值", cvModeResponse.getCvStep());
            oldValue.put("一次调节阀最小开度值", cvModeResponse.getCvMin());
            oldValue.put("一次调节阀最大开度值", cvModeResponse.getCvMax());
        }
        logOperation.setOldValue(oldValue.toJSONString().replaceAll("[{}]", ""));

        // 拼接修改的值
        JSONObject newValue = new JSONObject(true);
        // 下发调整的工作模式
        Integer newOptions = dto.getOptions();
        if (newOptions == 0) {
            // 一次调节阀工作方式 0:定值
            logOperation.setMode("一次调节阀工作方式 0:定值");
            newValue.put("工作模式调整为", dto.getOptions() + " :定值");
            newValue.put("定值开度设定值为", dto.getCvMsp());
        }
        if (newOptions == 1) {
            // 一次调节阀工作方式 1:二次供温恒定
            logOperation.setMode("一次调节阀工作方式 1:二次供温恒定");
            newValue.put("工作模式调整为", dto.getOptions() + " :二次供温恒定");
            newValue.put("恒定二次供温设定值为", dto.getCvTg().getCvTgSP());
            newValue.put("二次供温0时段偏差设定值为", dto.getCvTg().getTgSPTime0());
            newValue.put("二次供温2时段偏差设定值为", dto.getCvTg().getTgSPTime1());
            newValue.put("二次供温4时段偏差设定值为", dto.getCvTg().getTgSPTime2());
            newValue.put("二次供温6时段偏差设定值为", dto.getCvTg().getTgSPTime3());
            newValue.put("二次供温8时段偏差设定值为", dto.getCvTg().getTgSPTime4());
            newValue.put("二次供温10时段偏差设定值为", dto.getCvTg().getTgSPTime5());
            newValue.put("二次供温12时段偏差设定值为", dto.getCvTg().getTgSPTime6());
            newValue.put("二次供温14时段偏差设定值为", dto.getCvTg().getTgSPTime7());
            newValue.put("二次供温16时段偏差设定值为", dto.getCvTg().getTgSPTime8());
            newValue.put("二次供温18时段偏差设定值为", dto.getCvTg().getTgSPTime9());
            newValue.put("二次供温20时段偏差设定值为", dto.getCvTg().getTgSPTime10());
            newValue.put("二次供温22时段偏差设定值为", dto.getCvTg().getTgSPTime11());
            if (dto.getCvTg().getCvStep() != null) {
                newValue.put("一次调节阀设置步进值为", dto.getCvTg().getCvStep());
            }
            if (dto.getCvTg().getCvMin() != null) {
                newValue.put("一次调节阀最小开度值为", dto.getCvTg().getCvMin());
            }
            if (dto.getCvTg().getCvMax() != null) {
                newValue.put("一次调节阀最大开度值为", dto.getCvTg().getCvMax());
            }
        }
        if (newOptions == 4) {
            // 一次调节阀工作方式 4:恒流
            logOperation.setMode("一次调节阀工作方式 4:恒流");
            newValue.put("工作模式调整为", dto.getOptions() + " :恒流");
            newValue.put("恒定流量设定值为", dto.getCvFs().getCvFSP());
            if (dto.getCvFs().getCvStep() != null) {
                newValue.put("一次调节阀设置步进值为", dto.getCvFs().getCvStep());
            }
            if (dto.getCvFs().getCvMin() != null) {
                newValue.put("一次调节阀最小开度值为", dto.getCvFs().getCvMin());
            }
            if (dto.getCvFs().getCvMax() != null) {
                newValue.put("一次调节阀最大开度值为", dto.getCvFs().getCvMax());
            }
        }
        logOperation.setNewValue(newValue.toJSONString().replaceAll("[{}]", ""));
        logOperation.setCreateTime(LocalDateTime.now());
        return logOperationControlService.save(logOperation);
    }

    private Boolean xpModeOne(XPModeDto dto) {
        Response query = xpModeService.query(dto.getSystemId());
        Gson gson = new Gson();
        XPModeResponse xpModeResponse = gson.fromJson(gson.toJson(query.getData()), XPModeResponse.class);
        LogOperationControl logOperation = new LogOperationControl();
        // 操作时间
        logOperation.setOperationTime(LocalDateTime.now());
        logOperation.setIp(dto.getIp());
        logOperation.setUserName(dto.getUserName());
        logOperation.setDeviceName(" 二次循环变频泵");
        // 操作类型 ：1、阀门动作   2、变频动作
        logOperation.setType(2);
        QueryWrapper<HeatSystem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hss.id", dto.getSystemId());
        queryWrapper.eq("hss.deleteFlag", 0);
        List<FirstNetBase> firstNetBases = heatSystemService.querySystem(queryWrapper);
        if (CollectionUtils.isEmpty(firstNetBases)) {
            log.error("控制下发日记录失败！没有查询到 {} 对应的机组信息！", dto.getSystemId());
            return false;
        }
        logOperation.setCabinetName(firstNetBases.get(0).getHeatCabinetName());
        logOperation.setCompany(firstNetBases.get(0).getHeatStationOrgName());
        logOperation.setStationName(firstNetBases.get(0).getHeatTransferStationName());
        logOperation.setSystemName(firstNetBases.get(0).getHeatSystemName());
        // 拼接原始数据
        JSONObject oldValue = new JSONObject(true);
        Integer oldOptions = xpModeResponse.getRead().getOptions();
        if (oldOptions == null) {
            return false;
        }
        if (oldOptions == 0) {
            oldValue.put("原来工作模式", xpModeResponse.getRead().getOptions() + " :定值");
        }
        if (oldOptions == 1) {
            oldValue.put("原来工作模式", xpModeResponse.getRead().getOptions() + " :恒定二次供回压差");
        }
        if (oldOptions == 2) {
            oldValue.put("原来工作模式", xpModeResponse.getRead().getOptions() + " :恒定二次供压");
        }
        oldValue.put("频率设定值", xpModeResponse.getRead().getXpMSP());
        oldValue.put("二次供回压差设定值", xpModeResponse.getRead().getXpPdSP());
        oldValue.put("二次供压设定值", xpModeResponse.getRead().getXpPgSP());
        oldValue.put("二次供回压差当前值", xpModeResponse.getRead().getP2d());
        oldValue.put("二次供压当前值", xpModeResponse.getRead().getP2g());
        logOperation.setOldValue(oldValue.toJSONString().replaceAll("[{}]", ""));
        // 拼接修改的值
        JSONObject newValue = new JSONObject(true);
        Integer options = dto.getOptions();
        // 二次循环变频工作方式   0:定值   1:恒定二次供回压差   2:恒定二次供压
        if (options == 0) {
            logOperation.setMode("二次循环变频工作方式   0:定值");
            newValue.put("工作模式调整为", dto.getOptions() + " :定值");
            newValue.put("频率设定值为", dto.getXpMSP());
        }
        if (options == 1) {
            logOperation.setMode("二次循环变频工作方式   1:恒定二次供回压差");
            newValue.put("工作模式调整为", dto.getOptions() + " :恒定二次供回压差");
            newValue.put("二次供回压差设定值为", dto.getXpPdSP());
        }
        if (options == 2) {
            logOperation.setMode("二次循环变频工作方式   2:恒定二次供压");
            newValue.put("工作模式调整为", dto.getOptions() + " :恒定二次供压");
            newValue.put("二次供压设定值为", dto.getXpPgSP());
        }
        logOperation.setNewValue(newValue.toJSONString().replaceAll("[{}]", ""));
        logOperation.setCreateTime(LocalDateTime.now());
        return logOperationControlService.save(logOperation);
    }


    private Boolean xpModeThree(XPModeDto dto) {
        Response query = xpModeService.query(dto.getSystemId());
        Gson gson = new Gson();
        XPModeResponse xpModeResponse = gson.fromJson(gson.toJson(query.getData()), XPModeResponse.class);
        LogOperationControl logOperation = new LogOperationControl();
        // 操作时间
        logOperation.setOperationTime(LocalDateTime.now());
        logOperation.setIp(dto.getIp());
        logOperation.setUserName(dto.getUserName());
        logOperation.setDeviceName(" 二次循环变频泵");
        logOperation.setMode("泵启停操作");
        // 操作类型 ：1、阀门动作   2、变频动作
        logOperation.setType(2);
        QueryWrapper<HeatSystem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hss.id", dto.getSystemId());
        queryWrapper.eq("hss.deleteFlag", 0);
        List<FirstNetBase> firstNetBases = heatSystemService.querySystem(queryWrapper);
        if (CollectionUtils.isEmpty(firstNetBases)) {
            log.error("控制下发日记录失败！没有查询到 {} 对应的机组信息！", dto.getSystemId());
            return false;
        }
        logOperation.setCabinetName(firstNetBases.get(0).getHeatCabinetName());
        logOperation.setCompany(firstNetBases.get(0).getHeatStationOrgName());
        logOperation.setStationName(firstNetBases.get(0).getHeatTransferStationName());
        logOperation.setSystemName(firstNetBases.get(0).getHeatSystemName());
        // 拼接原始数据
        JSONObject oldValue = new JSONObject(true);
        Integer runStatus = xpModeResponse.getRead().getRunStatus();
        oldValue.put("泵原来状态", runStatus == 0 ? runStatus + " :停止" : runStatus + " :运行");
        oldValue.put("补水启动令", xpModeResponse.getRead().getStart());
        oldValue.put("补水停止令", xpModeResponse.getRead().getStop());
        logOperation.setOldValue(oldValue.toJSONString().replaceAll("[{}]", ""));


        // 拼接修改的值
        JSONObject newValue = new JSONObject(true);
        Integer start = dto.getStart();
        Integer stop = dto.getStop();
        Integer status = null;
        if (start == 1 && stop == 0) {
            status = 1;
        }
        if (start == 0 && stop == 1) {
            status = 0;
        }
        newValue.put("泵状态改为", status == null ? " 无效" : (status == 1 ? status + " :启动" : status + " :停止"));
        newValue.put("补水启动令为", start);
        newValue.put("补水停止为", stop);

        logOperation.setNewValue(newValue.toJSONString().replaceAll("[{}]", ""));
        logOperation.setCreateTime(LocalDateTime.now());
        return logOperationControlService.save(logOperation);
    }


    private Boolean xsvModeRecord(XSVModeDto dto) {
        Response query = xsvModeService.query(dto.getSystemId());
        Gson gson = new Gson();
        XSVModeResponse xsvModeResponse = gson.fromJson(gson.toJson(query.getData()), XSVModeResponse.class);
        LogOperationControl logOperation = new LogOperationControl();
        // 操作时间
        logOperation.setOperationTime(LocalDateTime.now());
        logOperation.setIp(dto.getIp());
        logOperation.setUserName(dto.getUserName());
        logOperation.setDeviceName(" 一次调节阀");
        // 操作类型 ：1、阀门动作   2、变频动作
        logOperation.setType(1);
        QueryWrapper<HeatSystem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("hss.id", dto.getSystemId());
        queryWrapper.eq("hss.deleteFlag", 0);
        List<FirstNetBase> firstNetBases = heatSystemService.querySystem(queryWrapper);
        if (CollectionUtils.isEmpty(firstNetBases)) {
            log.error("控制下发日记录失败！没有查询到 {} 对应的机组信息！", dto.getSystemId());
            return false;
        }
        logOperation.setCabinetName(firstNetBases.get(0).getHeatCabinetName());
        logOperation.setCompany(firstNetBases.get(0).getHeatStationOrgName());
        logOperation.setStationName(firstNetBases.get(0).getHeatTransferStationName());
        logOperation.setSystemName(firstNetBases.get(0).getHeatSystemName());
        // 拼接原始数据
        JSONObject oldValue = new JSONObject(true);
        Integer options = xsvModeResponse.getRead().getOptions();
        if (options == null) {
            return false;
        }
        if (options == 0) {
            oldValue.put("原来工作模式", xsvModeResponse.getRead().getOptions() + " :手动");
        }
        if (options == 1) {
            oldValue.put("原来工作模式", xsvModeResponse.getRead().getOptions() + " :自动");
        }

        Integer status = xsvModeResponse.getRead().getStatus();
        oldValue.put("泄压阀状态", status == 0 ? status + " :关闭" : status + " :打开");
        Integer handStatus = xsvModeResponse.getRead().getHandStatus();
        oldValue.put("泄压电磁阀手动启停状态", handStatus == 0 ? handStatus + " :关闭" : handStatus + " :打开");
        oldValue.put("自动高限设定值", xsvModeResponse.getRead().getXsvP2hH());
        oldValue.put("自动低限设定值", xsvModeResponse.getRead().getXsvP2hL());
        oldValue.put("回水压力值", xsvModeResponse.getRead().getP2h());
        logOperation.setOldValue(oldValue.toJSONString().replaceAll("[{}]", ""));

        // 拼接修改的值
        JSONObject newValue = new JSONObject(true);
        // 下发调整的工作模式
        Integer newOptions = dto.getOptions();
        if (newOptions == 0) {
            // 泄压电磁阀工作方式 0:手动
            logOperation.setMode("泄压电磁阀工作方式 0:手动");
            newValue.put("工作模式调整为", dto.getOptions() + " :手动");
            newValue.put("泄压电磁阀手动启停值为", dto.getXsvSS() == 0 ? dto.getXsvSS() + " :关闭" : dto.getXsvSS() + " :打开");
        }
        if (newOptions == 1) {
            // 泄压电磁阀工作方式 1:自动
            logOperation.setMode("泄压电磁阀工作方式 1:自动");
            newValue.put("工作模式调整为", dto.getOptions() + " :自动");
            newValue.put("自动高限设定值为", dto.getXsvP2hH());
            newValue.put("自动低限设定值为", dto.getXsvP2hL());
        }
        logOperation.setNewValue(newValue.toJSONString().replaceAll("[{}]", ""));
        logOperation.setCreateTime(LocalDateTime.now());
        return logOperationControlService.save(logOperation);
    }

}
