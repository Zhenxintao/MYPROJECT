package com.bmts.heating.storm;

import com.bmts.heating.storm.bolt.check.EntranceWashBolt;
import com.bmts.heating.storm.bolt.check.ExportWashBolt;
import com.bmts.heating.storm.bolt.destination.ActualBolt;
import com.bmts.heating.storm.bolt.destination.HistoryBolt;
import com.bmts.heating.storm.bolt.process.DataBaseBolt;
import com.bmts.heating.storm.bolt.process.TransFormBolt;
import com.bmts.heating.storm.spout.DataSourceWashSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

public class WashStreamTopology {

    public static void main(String[] args) throws Exception {
        TopologyBuilder topologyBuilder = new TopologyBuilder();

        topologyBuilder.setSpout("dataSourceWashSpout", new DataSourceWashSpout()).setNumTasks(1);
        topologyBuilder.setBolt("entranceWashBolt",new EntranceWashBolt(),2).setNumTasks(2)
                    .shuffleGrouping("dataSourceWashSpout");
        topologyBuilder.setBolt("dataBaseBolt", new DataBaseBolt(),2)
                    .localOrShuffleGrouping("entranceWashBolt","true");
        topologyBuilder.setBolt("exportWashBolt",new ExportWashBolt(),2)
                    .localOrShuffleGrouping("entranceWashBolt","false")
                    .shuffleGrouping("transFormBolt");
        topologyBuilder.setBolt("transFormBolt",new TransFormBolt(),2)
                    .shuffleGrouping("dataBaseBolt");
        topologyBuilder.setBolt("historyBolt",new HistoryBolt(),2)
                    .localOrShuffleGrouping("exportWashBolt","history");
        topologyBuilder.setBolt("actualBolt",new ActualBolt(),2)
                    .localOrShuffleGrouping("exportWashBolt","actual");

        Config conf = new Config();
        //关闭ack
        //conf.setNumAckers(0);
        conf.put("myconfParam", "test");
        //本地模式
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("myTopology", conf, topologyBuilder.createTopology());
        //  关闭本地集群
        Thread.sleep(10000);
        cluster.shutdown();

        //集群模式
        // StormSubmitter.submitTopology("myTopology", conf, topologyBuilder.createTopology());
    }
}
