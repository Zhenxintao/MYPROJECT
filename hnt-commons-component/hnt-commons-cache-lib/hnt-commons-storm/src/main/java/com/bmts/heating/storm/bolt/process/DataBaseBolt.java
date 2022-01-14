package com.bmts.heating.storm.bolt.process;

import com.bmts.heating.storm.pojo.DataStream;
import com.bmts.heating.storm.pojo.Ibolt;
import com.bmts.heating.storm.pojo.WashPolicy;
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

public class DataBaseBolt extends BaseBasicBolt implements Ibolt {
    Logger LOGGER = LoggerFactory.getLogger(DataBaseBolt.class);
    public final int boltId=43235235;
    public final String boltName="database";

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        //启动spring容器
        //SpringBootStormApplication.run();
        super.prepare(stormConf, context);
    }
    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        DataStream ds=(DataStream) tuple.getValueByField("isWash");
        LOGGER.info("databaseBolt处理器获得数据：name={},type={}",ds.getName(),ds.getType());
        Map<String, WashPolicy[]> washMap =ds.getWashMap(); //获取所有清洗策略
        //根据boltName判断清洗策略中是否有bolt该节点的清洗任务
        if(washMap.containsKey(boltName)){
            //有本bolt任务,遍历策略数组
            WashPolicy[] wps=washMap.get(boltName);
            //根据数据顺序执行各个策略的清洗任务
            String process=ds.getWashProcess()+boltName+":";
            for(int i=0;i<wps.length;i++){
                WashPolicy wp =wps[i];
                String washName=wp.getWpName();
                //模拟清洗过程
                process+="{清洗过程:"+washName+"}";
            }
            ds.setWashProcess(process+"->");
        }else{
            //没有本bolt任务,直接发射到下一个bolt
            LOGGER.info("bolt节点{}没有清洗任务,直接发射到下一个节点.....",boltName);
        }
        basicOutputCollector.emit(new Values(ds));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("transform"));
    }
}
