package com.bmts.heating.storm;

import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseBasicBolt;
import java.util.Map;

public class TopologyBuilderFactory {


    public static TopologyBuilder makeBolts(TopologyBuilder topologyBuilder, String fromStep, Map<String, BaseBasicBolt> boltMap){
        boltMap.keySet().forEach(
                Key->{
                    topologyBuilder.setBolt(Key,boltMap.get(Key),2)
                            .localOrShuffleGrouping(fromStep,Key);
                }
        );
        return topologyBuilder;
    }
}
