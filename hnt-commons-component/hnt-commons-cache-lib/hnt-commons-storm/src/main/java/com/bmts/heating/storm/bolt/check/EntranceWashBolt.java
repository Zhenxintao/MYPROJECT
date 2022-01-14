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
 * 清洗入口节点
 */
public class EntranceWashBolt extends BaseBasicBolt implements Ibolt {

    Logger LOGGER = LoggerFactory.getLogger(EntranceWashBolt.class);

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        //启动spring容器
        //SpringBootStormApplication.run();
        super.prepare(stormConf, context);
    }
    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        DataStream c = (DataStream)tuple.getValueByField("pointL");
        //判断清洗策略是否为空
        String streamType;
        if(c.getWashMap()==null){
            streamType="false";
            LOGGER.info("当前EntranceWashBolt节点线程{}收到不需要清洗传输对象name:{},type:{}",Thread.currentThread().getName(),c.getName(),c.getType());
        }else{
            streamType="true";
            LOGGER.info("当前EntranceWashBolt节点线程{}收到需要清洗传输对象name:{},type:{}",Thread.currentThread().getName(),c.getName(),c.getType());
        }
        String proccess=c.getWashProcess()+"EntranceWashBolt->";
        c.setWashProcess(proccess);
        basicOutputCollector.emit(streamType,new Values(c));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declareStream("true",new Fields("isWash"));
        outputFieldsDeclarer.declareStream("false",new Fields("export"));
    }
}
