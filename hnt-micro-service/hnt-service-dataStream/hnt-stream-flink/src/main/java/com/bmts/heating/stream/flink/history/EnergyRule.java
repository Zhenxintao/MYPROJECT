package com.bmts.heating.stream.flink.history;

import com.bmts.heating.commons.basement.model.db.entity.EnergyCollectConfigChild;
import com.bmts.heating.commons.basement.model.db.response.EnergyNodeSource;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//计算策略
@Slf4j
public class EnergyRule {

    //需要计算的站
    /**
     * {key(站id):{key(pointId):rulePojo}}
     */
    public static Map<Integer, Map<Integer, List<EnergyNodeSource>>> stationMasterNode;
    //需要计算的源
    /**
     * {key(源id):{key(pointId):rulePojo}}
     */
    public static Map<Integer, Map<Integer, List<EnergyNodeSource>>> sourceMasterNode;
    /**
     * 所有参数
     * key:系统id
     */
    public static Map<Integer, List<EnergyCollectConfigChild>> childNode;

//	static {
//		stationMasterNode = new HashMap<>();
//		sourceMasterNode = new HashMap<>();
//		childNode = new HashMap<>();
//	}

    /**
     * 初始化数据
     */
    public void initMaster(List<EnergyNodeSource> energyNodeSources, Integer level) {
        if (TreeLevel.HeatStation.level() == level) {
            this.initStation(energyNodeSources);
        } else if (TreeLevel.HeatSource.level() == level) {
            this.initSource(energyNodeSources);
        }
        log.info("数据不存在");
    }

    public void initChild(List<EnergyCollectConfigChild> energyCollectConfigChildren) {
        childNode = new HashMap<>(16);
        childNode = energyCollectConfigChildren
                .stream()
                .collect(Collectors.groupingBy(EnergyCollectConfigChild::getTargetId));

    }

    private void initStation(List<EnergyNodeSource> energyNodeSources) {
        Map<Integer, List<EnergyNodeSource>> collect = energyNodeSources
                .stream()
                .collect(Collectors.groupingBy(EnergyNodeSource::getMasterId));
        stationMasterNode = new HashMap<>(16);
        collect
                .forEach((k, v) -> stationMasterNode
                        .put(k, v.stream()
                                .collect(Collectors.groupingBy(EnergyNodeSource::getRelevanceId))));
    }

    private void initSource(List<EnergyNodeSource> energyNodeSources) {
        sourceMasterNode = new HashMap<>(16);
        energyNodeSources
                .stream()
                .collect(Collectors.groupingBy(EnergyNodeSource::getMasterId))
                .forEach((k, v) -> sourceMasterNode
                        .put(k, v.stream()
                                .collect(Collectors
                                        .groupingBy(EnergyNodeSource::getRelevanceId))));
    }

    /**
     * 验证当前站或者热源是否需要计算
     */
    public static boolean isContain(Integer id, int type) {
        if (type == TreeLevel.HeatStation.level()) {
            return stationMasterNode.get(id) != null;
        } else if (type == TreeLevel.HeatSource.level()) {
            return sourceMasterNode.get(id) != null;
        }
        throw new RuntimeException("找不到匹配的type类型");
    }

}
