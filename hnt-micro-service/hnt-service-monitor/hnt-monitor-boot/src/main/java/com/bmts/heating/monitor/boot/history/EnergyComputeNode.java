package com.bmts.heating.monitor.boot.history;

import com.bmts.heating.commons.basement.model.db.entity.EnergyCollectConfigChild;
import com.bmts.heating.commons.basement.model.db.response.EnergyNodeSource;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.utils.msmq.Message_Gather;
import com.bmts.heating.commons.utils.msmq.Message_Point_Gather;
import com.bmts.heating.commons.utils.msmq.PointL;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.nfunk.jep.JEP;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Slf4j
public class EnergyComputeNode {


    private JEP jep;

    /**
     * 入口
     */
    public void entry(Message_Gather message_gather) {
        if (EnergyRule.isContain(message_gather.getRelevanceId(), message_gather.getRelevanceType())) {
            this.step1(message_gather);
        }
    }


    /**
     * 判断是热力战还是热源
     */
    private void step1(Message_Gather messageGather) {
        if (messageGather.getRelevanceType() == TreeLevel.HeatStation.level()) {
            this.station(messageGather);
        } else {
            this.source(messageGather);
        }
    }

    /**
     * 热力站处理逻辑
     */
    private void station(Message_Gather messageGather) {
        //需要计算的点集合 key:pointId
        Map<Integer, List<EnergyNodeSource>> integerListMap = EnergyRule.stationMasterNode.get(messageGather.getRelevanceId());
        this.compute(messageGather, integerListMap);
    }

    //热源处理逻辑
    private void source(Message_Gather messageGather) {
        Map<Integer, List<EnergyNodeSource>> integerListMap = EnergyRule.sourceMasterNode.get(messageGather.getRelevanceId());
        this.compute(messageGather, integerListMap);
    }

    // 获取计算相关量
    private void compute(Message_Gather messageGather, Map<Integer, List<EnergyNodeSource>> integerListMap) {
        messageGather.getBatchPoints().forEach(e -> {
            List<EnergyNodeSource> rules = integerListMap.get(e.getRelevanceId());
            if (!CollectionUtils.isEmpty(rules)) {
                for (EnergyNodeSource rule : rules) {
                    try {
                        this.compute(rule, messageGather);
                        e.getPointLS().get(rule.getColumnName()).setValue(jep.getValue() + "");
                    } catch (Exception err) {
                        log.error("compute ERROR:{}", rule.getRelevanceId() + ":" + rule.getPointTargetName());
                        err.printStackTrace();
                    }
                }
            }
        });
    }

    private void compute(EnergyNodeSource rule, Message_Gather messageGather) {
        List<EnergyCollectConfigChild> children = EnergyRule.childNode.get(rule.getId());
        jep = new JEP();
        for (EnergyCollectConfigChild c : children) {
            double v;
            try {
                v = c.getStatus() ? c.getVal() : searchByPointIdVal(messageGather, c.getRelevanceId(), c.getColumnName());
                jep.addVariable(c.getSign(), v);
            } catch (Exception e) {
                log.info("Message_Gather 下不存在系统对应的点{}", rule.getId(), c.toString());
            }
        }
        jep.parseExpression(rule.getExpression().split("=")[1]);
    }

    /**
     * 获取message_gather中对应变量值
     */
    private double searchByPointIdVal(Message_Gather messageGather, Integer relevanceId, String columnName) {
        List<Message_Point_Gather> batchPoints = messageGather.getBatchPoints();
        for (Message_Point_Gather batchPoint : batchPoints) {
            //系统id
            if (batchPoint.getRelevanceId() == relevanceId) {
                Map<String, PointL> pointLMap = batchPoint.getPointLS();
                PointL pointL = pointLMap.get(columnName);
                if (pointL != null) {
                    String value = pointL.getValue();
                    if (StringUtils.isNotBlank(value)) {
                        return Double.parseDouble(value);
                    } else {
                        throw new RuntimeException(columnName + "{} --pointL.getValue() is null");
                    }
                } else {
                    log.info("pointL is null  ----- relevanceId:{}", relevanceId);
                }
                break;
            }
        }
        throw new RuntimeException("Message_Gather 未采集 " + relevanceId);
    }
}
