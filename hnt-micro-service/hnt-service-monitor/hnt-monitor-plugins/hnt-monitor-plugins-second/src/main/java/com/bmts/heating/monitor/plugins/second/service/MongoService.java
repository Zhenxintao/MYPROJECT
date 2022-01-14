package com.bmts.heating.monitor.plugins.second.service;

import com.alibaba.fastjson.JSONObject;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.PointInfo;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.SecondNetDataMinuteDto;
import com.bmts.heating.commons.entiy.second.request.device.PointSecondDto;
import com.bmts.heating.middleware.td.HistoryTdGrpcClient;
import com.bmts.heating.monitor.plugins.second.pojo.CommunityQueryDto;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: MongoService
 * @Description: mongnDB 处理方法封装
 * @Author: pxf
 * @Date: 2022/1/6 14:27
 * @Version: 1.0
 */
@Slf4j
@Service
public class MongoService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private HistoryTdGrpcClient historyTdGrpcClient;

    public void updatSert(PointSecondDto dto) {
        // 组装 查询的key
        String key = "devices." + dto.getLevel() + ".deviceCode";
        Query query = new Query();
        query.addCriteria(Criteria.where(key).is(dto.getDeviceCode()));
        // 设置参数
        Update update = new Update();
        update.set("devices." + dto.getLevel() + ".realData.timeStamp", dto.getTimestamp());
        // points  Map 的key 为点名称，map 为点对应的各种信息
        Map<String, Map<String, String>> points = dto.getPoints();
        points.forEach((k, v) -> {
            v.forEach((x, y) -> {
                update.set("devices." + dto.getLevel() + ".realData." + k + "." + x, y);
            });
        });
        UpdateResult state = mongoTemplate.upsert(query, update, dto.getTableName());
        long matchedCount = state.getMatchedCount();
    }

    // public void updatSert(PointSecondDto dto) {
    //        // 组装 查询的key
    //        String key = "devices." + dto.getLevel() + ".deviceCode";
    //        Query query = new Query();
    //        query.addCriteria(Criteria.where(key).is(dto.getDeviceCode()));
    //        // 更新数据
    //        JSONObject jsonObj = new JSONObject();
    //        // 设置参数
    //        Update update = new Update();
    //        update.set("devices." + dto.getLevel() + ".realdata.timeStamp", dto.getTimestamp());
    //
    //        jsonObj.put("timeStamp", dto.getTimestamp());
    //        // points  Map 的key 为点名称，map 为点对应的各种信息
    //        Map<String, Map<String, String>> points = dto.getPoints();
    //        points.forEach((k, v) -> {
    //            JSONObject pointDetail = new JSONObject();
    //            v.forEach((x, y) -> {
    //                pointDetail.put(x, y);
    //            });
    //            jsonObj.put(k, pointDetail);
    //        });
    //
    //
    //        UpdateResult state = mongoTemplate.upsert(query, update, dto.getTableName());
    //    }

    //二网插入历史分钟数据
    public Boolean insertTdHistory() {
        //定义历史数据插入Dto实现类
        List<SecondNetDataMinuteDto> historyMinuteDtoList = new ArrayList<>();
        try {
            //查询MongoDB实时数据
            List<CommunityQueryDto> allRealData = mongoTemplate.findAll(CommunityQueryDto.class, "floor");
            if (allRealData.size() <= 0) {
                return false;
            }
            for (CommunityQueryDto dto : allRealData) {
                //获取Community实时数据表中devices数据信息
                Map<String, Map> map = dto.getDevices();
                for (String key : map.keySet()) {
                    SecondNetDataMinuteDto minuteDto = new SecondNetDataMinuteDto();
                    Integer level = (Integer) map.get(key).get("level");
                    String deviceCode = map.get(key).get("deviceCode").toString();
                    if (deviceCode == null || deviceCode.length() == 0) {
                        continue;
                    }
//                    if (level == null || level <= 0) {
//                        continue;
//                    }
                    //插入历史点位信息数据
                    List<PointInfo> pointInfos = new ArrayList<>();
                    Map<String, Object> realDataMap = (Map) map.get(key).get("realData");
                    if (realDataMap != null) {
                        String timeStamp = realDataMap.get("timeStamp").toString();
                        minuteDto.setTs((Long.valueOf(timeStamp)));
                        realDataMap.remove("timeStamp");
                        for (String realDataKey : realDataMap.keySet()) {
                            PointInfo pointInfo = new PointInfo();
                            pointInfo.setPointName(realDataKey);
                            JSONObject dataMap = (JSONObject) JSONObject.toJSON(realDataMap.get(realDataKey));
                            pointInfo.setValue(dataMap.get("value").toString());
                            pointInfos.add(pointInfo);
                        }
                    } else {
                        continue;
                    }
                    //组装历史数据响应体
                    Integer treeId = (Integer) map.get(key).get("treeId");
                    List<PointInfo> tags = new ArrayList<>();
                    PointInfo deviceCodePoint = new PointInfo() {{
                        setPointName("deviceCode");
                        setValue(deviceCode);
                    }};
                    PointInfo treePoint = new PointInfo() {{
                        setPointName("treeId");
                        setValue(treeId.toString());
                    }};
                    PointInfo levelPoint = new PointInfo() {{
                        setPointName("level");
                        setValue(level.toString());
                    }};
                    tags.add(deviceCodePoint);
                    tags.add(levelPoint);
                    tags.add(treePoint);
                    minuteDto.setTags(tags);
                    minuteDto.setPoints(pointInfos);
                    minuteDto.setTableName(key + "_meta_" + deviceCode + "_" + level);
                    minuteDto.setStableName(key + "_meta");
                    historyMinuteDtoList.add(minuteDto);
                }
            }
            //调用历史服务客户端，插入历史分钟数据。
            return historyTdGrpcClient.insertSecondHistoryMinuteToTd(historyMinuteDtoList);
        } catch (Exception e) {
            log.error("二网分数历史数据插入失败！……………………{}", e);
            return false;
        }
    }
}
