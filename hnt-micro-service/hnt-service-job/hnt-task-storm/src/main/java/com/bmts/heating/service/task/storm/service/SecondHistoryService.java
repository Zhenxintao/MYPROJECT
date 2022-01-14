package com.bmts.heating.service.task.storm.service;

import com.alibaba.fastjson.JSONObject;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.PointInfo;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.SecondNetDataMinuteDto;
import com.bmts.heating.middleware.td.HistoryTdGrpcClient;
import com.bmts.heating.service.task.storm.pojo.SecondMongoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: SecondHistoryService
 * @Description: 二网历史处理业务
 * @Author: pxf
 * @Date: 2020/8/13 15:39
 * @Version: 1.0
 */

@Slf4j
@Service
public class SecondHistoryService {

    @Autowired
    private HistoryTdGrpcClient historyTdGrpcClient;

    //二网插入历史分钟数据
    public Boolean insertTdHistory(List<SecondMongoDto> listReal) {
        //定义历史数据插入Dto实现类
        List<SecondNetDataMinuteDto> historyMinuteDtoList = new ArrayList<>();
        try {
            for (SecondMongoDto dto : listReal) {
                //获取Community实时数据表中devices数据信息
                Map<String, Map> map = dto.getDevices();
                for (String key : map.keySet()) {
                    SecondNetDataMinuteDto minuteDto = new SecondNetDataMinuteDto();
                    Integer level = (Integer) map.get(key).get("level");
                    String deviceCode = map.get(key).get("deviceCode").toString();
                    Integer treeId = (Integer) map.get(key).get("treeId");
                    if (deviceCode == null || deviceCode.length() == 0 || treeId == null) {
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
