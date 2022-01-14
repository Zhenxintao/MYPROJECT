package com.bmts.heating.storm.bolt.destination;

import com.bmts.heating.storm.bolt.process.DataBaseBolt;
import com.bmts.heating.storm.pojo.DataStream;
import com.bmts.heating.storm.pojo.Ibolt;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 历史库节点
 */
public class HistoryBolt extends BaseBasicBolt implements Ibolt {
    Logger LOGGER = LoggerFactory.getLogger(HistoryBolt.class);
    public final int boltId=233412;
    public final String boltName="history";

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        //启动spring容器
        //SpringBootStormApplication.run();
        super.prepare(stormConf, context);
    }
    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        DataStream c = (DataStream)tuple.getValueByField("store");
        LOGGER.info("historyBolt处理器获得数据：name={},type={}",c.getName(),c.getType());
        String process =c.getWashProcess();
        process+="historyBolt->";
        c.setWashProcess(process);
        LOGGER.info("{}清洗完成,进入history库,清洗流程:{}",c.getName(),c.getWashProcess());
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
