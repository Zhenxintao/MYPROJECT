package com.bmts.heating.storm.bolt.check;

import com.bmts.heating.storm.pojo.DataStream;
import com.bmts.heating.storm.pojo.Ibolt;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 清洗出口节点
 */
public class ExportWashBolt extends BaseBasicBolt implements Ibolt {
    Logger LOGGER = LoggerFactory.getLogger(ExportWashBolt.class);

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        //启动spring容器
        //SpringBootStormApplication.run();
        super.prepare(stormConf, context);
    }
    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        DataStream c = (DataStream)tuple.getValueByField("export");
        LOGGER.info("当前ExportWashBolt节点线程{}收到结束清洗对象name:{},type:{}",Thread.currentThread().getName(),c.getName(),c.getType());
        String process =c.getWashProcess();
        process+="ExportWashBolt->";
        c.setWashProcess(process);
        String streamType = null;
        if(c.getStoreType().equals("actual")){
            streamType="actual";
        }else if(c.getStoreType().equals("history")){
            streamType="history";
        }
        basicOutputCollector.emit(streamType,new Values(c));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declareStream("history",new Fields("store"));
        outputFieldsDeclarer.declareStream("actual",new Fields("store"));
    }
}
