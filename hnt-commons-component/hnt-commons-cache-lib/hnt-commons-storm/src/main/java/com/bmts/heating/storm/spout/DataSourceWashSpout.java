package com.bmts.heating.storm.spout;

import com.bmts.heating.commons.utils.container.SpringBeanFactory;
import com.bmts.heating.storm.SpringBootStormApplication;
import com.bmts.heating.storm.pojo.ContainerServer;
import com.bmts.heating.storm.pojo.DataStream;
import com.bmts.heating.storm.pojo.WashPolicy;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataSourceWashSpout extends BaseRichSpout {
    private Logger LOGGER= LoggerFactory.getLogger(DataSourceWashSpout.class);

    private SpoutOutputCollector collector;

    private Queue<String> wordQueue = new ConcurrentLinkedQueue<String>();
    private Queue<DataStream> pojoQueue = new ConcurrentLinkedQueue<DataStream>();

    /**
     ** 发送失败集合，用于重发
     */
    private Map<String, Object> failMap = new ConcurrentHashMap<String, Object>();


    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {

        System.out.println(conf.get("myconfParam"));
        wordQueue.add("Hello");
        wordQueue.add("World");
        wordQueue.add("Drew");
        wordQueue.add("World");

        WashPolicy wp_oracle= new WashPolicy(1,"oracle","database");
        WashPolicy wp_mysql= new WashPolicy(2,"mysql","database");
        WashPolicy wp_db= new WashPolicy(4,"db","database");
        WashPolicy wp_char= new WashPolicy(5,"char","transform");
        WashPolicy wp_project= new WashPolicy(6,"project","transform");

        Map<String,WashPolicy[]> washMap1 = new HashMap<>();
        washMap1.put("database",new WashPolicy[]{wp_oracle, wp_mysql,wp_db});
        washMap1.put("transform",new WashPolicy[]{wp_char});

        Map<String,WashPolicy[]> washMap2 = new HashMap<>();
        washMap2.put("database",new WashPolicy[]{wp_oracle,wp_db});
        washMap2.put("transform",new WashPolicy[]{wp_char,wp_project});

        Map<String,WashPolicy[]> washMap3 = new HashMap<>();
        washMap3.put("transform",new WashPolicy[]{wp_project});

        Map<String,WashPolicy[]> washMap4 = new HashMap<>();
        washMap4.put("database",new WashPolicy[]{wp_db});

        DataStream ds1= new DataStream(1,"gaoxiang","java","history",washMap1,"清洗过程->");
        DataStream ds2= new DataStream(2,"guai","java","actual",washMap2,"清洗过程->");
        DataStream ds3= new DataStream(3,"ya",".net","history",washMap3,"清洗过程->");
        DataStream ds4= new DataStream(4,"huang",".net","actual",washMap4,"清洗过程->");
        DataStream ds5= new DataStream(5,"peng",".net","history","清洗过程->");

        pojoQueue.add(ds1);
        pojoQueue.add(ds2);
        pojoQueue.add(ds3);
        pojoQueue.add(ds4);
        pojoQueue.add(ds5);
//        pojoQueue.add(ds6);
//        pojoQueue.add(ds7);
//        pojoQueue.add(ds8);
//        pojoQueue.add(ds9);
//        pojoQueue.add(ds10);
//        pojoQueue.add(ds11);
//        pojoQueue.add(ds12);
        //启动spring容器
        SpringBootStormApplication.run();
        ContainerServer cs=SpringBeanFactory.getBean(ContainerServer.class);
        cs.getContainer("spout");
        this.collector = collector;

    }

    @Override
    public void nextTuple() {
        while (!pojoQueue.isEmpty()) {
            //String world = wordQueue.poll();
            DataStream ds= pojoQueue.poll();
            if (Optional.ofNullable(ds).isPresent()) {
                //collector.emit(new Values(world));//不传msgId
                //传递消息时加上msgId，用于定位消息
                String key = UUID.randomUUID().toString().replace("-", "");
                //记录消息，方便失败重发
                failMap.put(key, ds);
                collector.emit(new Values(ds), key);
            }

//            List<String> policys_left=ds.getWashPolicy_left();
//            if(policys_left.size()>0){
//                String streamType=policys_left.remove(0);
//                collector.emit(streamType,new Values(ds));
//            }

        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
//        Arrays.asList(washPolicy).forEach(
//                policy->outputFieldsDeclarer.declareStream(policy,new Fields("policy"))
//        );
        outputFieldsDeclarer.declare(new Fields("pointL"));
    }

    /**
     ** 当一个Tuple处理成功时，会调用这个方法 param obj emit方法中的msgId
     */
    @Override
    public void ack(Object obj) {
        //清除消息
        failMap.remove(obj);
        System.out.println("成功：" + obj);
    }

    /**
     ** 当Topology停止时，会调用这个方法
     */
    @Override
    public void close() {
        System.out.println("关闭...");
    }

    /**
     ** 当一个Tuple处理失败时，会调用这个方法
     */
    @Override
    public void fail(Object obj) {
        System.out.println("失败：" + obj);
        String world = (String)failMap.get(obj);
        //清除消息，只重发一次
        failMap.remove(obj);
        if (Optional.ofNullable(world).isPresent()) {
            collector.emit(new Values(world));
        }
    }
}
