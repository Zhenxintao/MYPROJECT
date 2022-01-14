package com.bmts.heating.monitor.plugins.jk.constructors;

import com.bmts.heating.commons.utils.container.SpringBeanFactory;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.monitor.dirver.config.MonitorMuster;
import com.bmts.heating.monitor.dirver.handler.IWorkEntrance;
import com.bmts.heating.monitor.plugins.jk.constructors.jkUtil.JKValueTwo;
import com.bmts.heating.monitor.plugins.jk.constructors.service.JKCommonService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@Component("JkWorker")
@Scope("prototype")
public class JkWorker implements IWorkEntrance {

    private Logger LOGGER = LoggerFactory.getLogger(JkWorker.class);
    private Map<String, String> currentUrl = new HashMap<String, String>();
    private CountDownLatch countDownLatch;

    private static Socket client;
    private static OutputStream ous;
    private static InputStream ins;

    @Getter
    @Setter
    private MonitorMuster.Plugin plugin;
    @Getter
    @Setter
    private List<MonitorMuster.Plugin> work_plugins;
    @Getter
    @Setter
    private int result; //计数器


    @Autowired
    private JKCommonService jkCommonService;

    @Override
    public void config(Object... objs) {
        countDownLatch = (CountDownLatch) objs[0];
        this.setResult((Integer) objs[1]);
        this.setPlugin((MonitorMuster.Plugin) objs[2]);
        this.setWork_plugins((List<MonitorMuster.Plugin>) objs[3]);
        //设置url
    }

    @Override
    public int getGather_count() {
        return this.getResult();
    }


    /**
     * 执行任务子线程
     */
    @SneakyThrows
    @Override
    public void invoke(List<PointL> taskArray, int flag) {
        ////判断当前任务是采集还是下发
        /**
         *采集和下发
         * flag-->0:采集，1下发
         * pointL.getType()-->1-Bool 2-Int 3-Uint 4-Long 5-ULong 6-Float 7-Double
         */
        synchronized (this) {
            if (0 == flag) {
                client = new Socket(plugin.getModel_host(), plugin.getModel_port());
                System.out.println("连接地址：----->" + plugin.getModel_host() + ":" + plugin.getModel_port());
                ous = client.getOutputStream();
                ins = client.getInputStream();

                List<PointL> list = new ArrayList<>();
                for (PointL pointL : taskArray) {
                    pointL.setPointlsSign(plugin.getPointls_sign());
                    pointL.setApplicationName(SpringBeanFactory.getBean("application_name").toString());
//                    String addre = pointL.getPointAddress();
//                    int type = pointL.getType();
//                    if (1 == type) {//bool类型
//                        byte[] BoolValue = GetValues.getBool(addre);
//                        ous.write(BoolValue);//请求数据包
//                        //接受应答数据包
//                        String ret = "";
//                        byte[] bytes = new byte[1];
//                        while (ins.read(bytes) != -1) {
//                            ret += Conver.byteArrToHex(bytes) + " ";
//                            if (ins.available() == 0) {
//                                System.out.println("远程服务" + client.getRemoteSocketAddress() + "响应：" + ret);
//                                String bools = GetValues.getBoolValue(ret);
//                                System.out.println(addre + ":----->" + bools);
//                                pointL.setOldValue(bools);
//                                pointL.setTimeStrap(System.currentTimeMillis());
//                                break;
//                            }
//                        }
//                        list.add(pointL);
//                    }
//                    if (2 == type) {//int整数类型
//                        byte[] IntValue = GetValues.getInt(addre);
//                        ous.write(IntValue);//请求数据包
//                        String ret = "";
//                        byte[] bytes = new byte[1];
//                        while (ins.read(bytes) != -1) {//接受应答数据包
//                            ret += Conver.byteArrToHex(bytes) + " ";
//                            if (ins.available() == 0) {
//                                System.out.println("远程服务" + client.getRemoteSocketAddress() + "响应：" + ret);
//                                String UInts = FloatValueUtil.indexStr(ret.replaceAll(" ", ""), 12, 16);
//                                System.out.println(addre + ":----->" + UInts);
//                                pointL.setOldValue(UInts);
//                                pointL.setTimeStrap(System.currentTimeMillis());
//                                break;
//                            }
//                        }
//                        list.add(pointL);
//                    }
//                    if (6 == type) {//float类型
//                        byte[] FloatValue = GetValues.getFloat(addre);
//                        ous.write(FloatValue);//请求数据包
//                        String ret = "";
//                        byte[] bytes = new byte[1];
//                        while (ins.read(bytes) != -1) {//接受应答数据包
//                            ret += Conver.byteArrToHex(bytes) + " ";
//                            if (ins.available() == 0) {
//                                System.out.println("远程服务" + client.getRemoteSocketAddress() + "响应：" + ret);
//                                String floats = FloatValueUtil.indexStr(ret.replaceAll(" ", ""), 12, 20);
//                                System.out.println(addre + ":----->" + floats);
//                                pointL.setOldValue(floats);
//                                pointL.setTimeStrap(System.currentTimeMillis());
//                                break;
//                            }
//                        }
//                        list.add(pointL);
//                    }
                    

                }

                // 进行 kafka 消息发送处理
                if (!CollectionUtils.isEmpty(list)) {
                    Map<Integer, List<PointL>> groupMap = list.stream().collect(Collectors.groupingBy(e -> e.getDataType() + e.getLevel() + e.getRelevanceId()));
                    Map<String, PointL> messageMap = new HashMap<>();
                    for (Integer key : groupMap.keySet()) {
                        List<PointL> pList = groupMap.get(key);
                        int listSize = pList.size();
                        if (listSize > 999) {
                            int num = 1;
                            for (PointL ent : pList) {
                                messageMap.put(String.valueOf(ent.getPointId()), ent);
                                if ((num % 1000) == 0) {
                                    if (messageMap != null) {
                                        jkCommonService.setMessageProduce(messageMap, ent);
                                    }
                                }
                                if (listSize % 1000 != 0 && num == listSize) {
                                    if (messageMap != null) {
                                        jkCommonService.setMessageProduce(messageMap, ent);
                                    }
                                }

                                num++;
                            }
                        } else if (listSize > 0 && listSize < 1000) {
                            for (PointL ent : pList) {
                                messageMap.put(String.valueOf(ent.getPointId()), ent);
                            }
                            if (messageMap != null) {
                                jkCommonService.setMessageProduce(messageMap, pList.get(0));
                            }
                        }


                    }

                }

                //关闭流
                ous.close();
                ins.close();
                client.close();
            }
        }
        LOGGER.info("{}线程执行jk任务:{}完毕.........", Thread.currentThread().getName(), plugin.getModel_host());
        countDownLatch.countDown();
    }


    /**
     * 执行任务子线程
     */
//    @SneakyThrows
//    @Override
//    public void invoke(List<PointL> taskArray, int flag) {
//        ////判断当前任务是采集还是下发
//
//        /**
//         *采集和下发
//         * flag-->0:采集，1下发
//         * pointL.getType()-->1-Bool 2-Int 3-Uint 4-Long 5-ULong 6-Float 7-Double
//         */
//        synchronized (this) {
//            client = new Socket(plugin.getModel_host(), plugin.getModel_port());
//            System.out.println("连接地址：----->" + plugin.getModel_host() + ":" + plugin.getModel_port());
//            ous = client.getOutputStream();
//            ins = client.getInputStream();
//            if (0 == flag) {
//                List<PointL> list = new ArrayList<>();
//                for (PointL pointL : taskArray) {
//                    String addre = pointL.getPointAddress();
//                    int type = pointL.getType();
//                    if (1 == type) {//bool类型
//                        byte[] BoolValue = GetValues.getBool(addre);
//                        ous.write(BoolValue);//请求数据包
//                        //接受应答数据包
//                        String ret = "";
//                        byte[] bytes = new byte[1];
//                        while (ins.read(bytes) != -1) {
//                            ret += Conver.byteArrToHex(bytes) + " ";
//                            if (ins.available() == 0) {
//                                System.out.println("远程服务" + client.getRemoteSocketAddress() + "响应：" + ret);
//                                String bools = GetValues.getBoolValue(ret);
//                                System.out.println(addre + ":----->" + bools);
//                                pointL.setOldValue(bools);
//                                pointL.setPointlsSign(plugin.getPointls_sign());
//                                break;
//                            }
//                        }
//                        list.add(pointL);
//                    }
//                    if (2 == type) {//int整数类型
//                        byte[] IntValue = GetValues.getInt(addre);
//                        ous.write(IntValue);//请求数据包
//                        String ret = "";
//                        byte[] bytes = new byte[1];
//                        while (ins.read(bytes) != -1) {//接受应答数据包
//                            ret += Conver.byteArrToHex(bytes) + " ";
//                            if (ins.available() == 0) {
//                                System.out.println("远程服务" + client.getRemoteSocketAddress() + "响应：" + ret);
//                                String UInts = FloatValueUtil.indexStr(ret.replaceAll(" ", ""), 12, 16);
//                                System.out.println(addre + ":----->" + UInts);
//                                pointL.setOldValue(UInts);
//                                pointL.setPointlsSign(plugin.getPointls_sign());
//                                break;
//                            }
//                        }
//                        list.add(pointL);
//                    }
//                    if (6 == type) {//float类型
//                        byte[] FloatValue = GetValues.getFloat(addre);
//                        ous.write(FloatValue);//请求数据包
//                        String ret = "";
//                        byte[] bytes = new byte[1];
//                        while (ins.read(bytes) != -1) {//接受应答数据包
//                            ret += Conver.byteArrToHex(bytes) + " ";
//                            if (ins.available() == 0) {
//                                System.out.println("远程服务" + client.getRemoteSocketAddress() + "响应：" + ret);
//                                String floats = FloatValueUtil.indexStr(ret.replaceAll(" ", ""), 12, 20);
//                                System.out.println(addre + ":----->" + floats);
//                                pointL.setOldValue(floats);
//                                pointL.setPointlsSign(plugin.getPointls_sign());
//                                break;
//                            }
//                        }
//                        list.add(pointL);
//                    }
//                }
//                //利用grpc传递采集信息
////                boolean pointTF = PointSavantService.getEntity(list,"JK");
////                System.out.println("------>JK采集"+pointTF+"<---------");
//                //关闭流
//                ous.close();
//                ins.close();
//                client.close();
//            }
//            if (1 == flag) {
////                JKValue.putJKValue(taskArray,ous,ins);//遍历请求，每个点都做请求
//                JKValueTwo.putValue(taskArray, ous, ins);//bool类型统一请求修改
//                ous.close();
//                ins.close();
//                client.close();
//            }
//        }
//        LOGGER.info("{}线程执行jk任务:{}完毕.........", Thread.currentThread().getName(), plugin.getModel_host());
//        countDownLatch.countDown();
//    }
    @Override
    @SneakyThrows
    public Object invokeForBack(List<PointL> taskArray, int flag) {
        List<PointL> listReturn = new ArrayList<>();
        // 下发
        if (1 == flag) {
            client = new Socket(plugin.getModel_host(), plugin.getModel_port());
            System.out.println("下发任务---连接地址：----->" + plugin.getModel_host() + ":" + plugin.getModel_port());
            ous = client.getOutputStream();
            ins = client.getInputStream();

//            JKValue.putJKValue(taskArray, ous, ins);//遍历请求，每个点都做请求
            JKValueTwo.putValue(taskArray, ous, ins);//bool类型统一请求修改
            ous.close();
            ins.close();
            client.close();
        }
        return listReturn;
    }

}
