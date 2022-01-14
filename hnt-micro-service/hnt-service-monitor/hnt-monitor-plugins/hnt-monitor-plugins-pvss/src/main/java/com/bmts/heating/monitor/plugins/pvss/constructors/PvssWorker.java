package com.bmts.heating.monitor.plugins.pvss.constructors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bmts.heating.commons.utils.common.SnowIdUtil;
import com.bmts.heating.commons.utils.container.SpringBeanFactory;
import com.bmts.heating.commons.utils.enums.HealthStatus;
import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.monitor.dirver.config.MonitorMuster;
import com.bmts.heating.monitor.dirver.handler.IWorkEntrance;
import com.bmts.heating.monitor.plugins.pvss.constructors.pvssUtil.Computation;
import com.bmts.heating.monitor.plugins.pvss.constructors.pvssUtil.PassValue;
import com.bmts.heating.monitor.plugins.pvss.constructors.pvssUtil.PointLTemp;
import com.bmts.heating.monitor.plugins.pvss.constructors.pvssUtil.UrlConn;
import com.bmts.heating.monitor.plugins.pvss.constructors.service.PVSSCommonService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@Slf4j
@Component("PvssWorker")
@Scope("prototype")
public class PvssWorker implements IWorkEntrance {

    // private static final Logger LOGGER = LoggerFactory.getLogger(PvssWorker.class);

    private Map<String, MonitorMuster.Plugin> currentUrl = new HashMap<String, MonitorMuster.Plugin>();

    private CountDownLatch countDownLatch;

    @Getter
    @Setter
    private MonitorMuster.Plugin plugin;

    @Getter
    @Setter
    private List<MonitorMuster.Plugin> work_plugins;

    @Getter
    @Setter
    private int result; //计数器

//    @Autowired
//    private PointSavantService pointSavantService;

    @Autowired
    private PVSSCommonService pvssCommonService;

    @Autowired
    private Computation computation;

    @Override
    public void invoke(List<PointL> taskArray, int flag) {
        try {
            //判断当前任务是采集(0)还是下发（1）
            if (flag == 0) {//采集:0
                List<PointL> orgList = new ArrayList<>();

                //请求 pvss 一组数据条数  按照 每包10000 进行分组
                List<List<PointL>> collect = new ArrayList<>();
                int pointsDataLimit = 10000;
                int maxSize = taskArray.size() - 1;
                int i = 0;
                for (PointL pl : taskArray) {
                    // 填充数据
                    pl.setPointlsSign(plugin.getPointls_sign());
                    pl.setApplicationName(SpringBeanFactory.getBean("application_name").toString());
                    // 原始数据
                    orgList.add(pl);
                    if (pointsDataLimit == orgList.size() || i == maxSize) {
                        collect.add(orgList);
                        orgList = new ArrayList<>();
                    }
                    i++;
                }
                // 进行请求 pvss 数据
                List<PointL> messageList = new ArrayList<>();
                requestPvss(collect, messageList);
                // 进行 kafka 消息发送处理
                sendKafka(messageList);
            }

            log.info("{}线程执行pvss任务:{}完毕.........", Thread.currentThread().getName(), plugin.getModel_host());
        } finally {
            countDownLatch.countDown();
        }
    }

    // 进行请求 pvss 数据
    private void requestPvss(List<List<PointL>> collect, List<PointL> addMsgList) {
        // 生成统一批号
        long lotNo = SnowIdUtil.uniqueLong();

        for (List<PointL> list : collect) {
            List<JSONObject> listJson = new ArrayList<>();
            List<PointL> listPvss = new ArrayList<>();
            for (PointL pl : list) {
                // 添加 请求的数据
                if (pl.getSystemNum() != null && pl.getParentSyncNum() != null && pl.getPointConfig() != 0) {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("pointId", pl.getPointId());
                    // 参量名称
                    jsonObj.put("pointName", pl.getPointName());
                    // 采集量 标识  ValueDesc
                    // 控制量 标识  ValueParaDesc
                    String config = "";
                    if (pl.getPointConfig() == 1) {
                        config = "ValueDesc";
                        // 标记是采集量还是 控制量
                        jsonObj.put("pointType", "ValueDesc");

                    }
                    if (pl.getPointConfig() == 2) {
                        config = "ValueParaDesc";
                        // 标记是采集量还是 控制量
                        jsonObj.put("pointType", "ValueParaDesc");
                    }

                    if (StringUtils.isNotBlank(pl.getSyncNumber())) {
                        String syncNumber = pl.getSyncNumber();
                        String[] split = syncNumber.split("\\.");

                        // 机组编号
                        jsonObj.put("narrayNo", split[2]);
                        Integer parentSyncNum = Integer.parseInt(split[0]);
                        if (StringUtils.isNotBlank(config)) {
                            if (parentSyncNum > 9999) {
                                // 表示是热力站   晋城热源是通过长输业务处理，此处不再处理
                                jsonObj.put("parentNum", parentSyncNum);
                                listJson.add(jsonObj);
                                listPvss.add(pl);
                            }

                            //if (parentSyncNum <= 9999) {
                            //    // 表示是热源
                            //    //String address = "System1:" + pl.getParentSyncNum() + "." + config + "." + pl.getPointName();
                            //    //jsonObj.put("pointAddress", address);
                            //    // 热源的编号  必须是string 类型 去请求
                            //    jsonObj.put("parentNum", String.valueOf(parentSyncNum));
                            //    listJson.add(jsonObj);
                            //    listPvss.add(pl);
                            //} else {
                            //    // 表示是热力站
                            //    // 站的编号
                            //    jsonObj.put("parentNum", parentSyncNum);
                            //    //// 机组编号
                            //    //jsonObj.put("narrayNo", pl.getSystemNum());
                            //    listJson.add(jsonObj);
                            //    listPvss.add(pl);
                            //}

                        }
                    }
                }
            }
            try {
                // 组装请求数据 json 格式
                JSONObject queryJson = new JSONObject();
                queryJson.put("lotNo", lotNo);
                queryJson.put("pointList", listJson);
                // 请求开始时间
                long starTime = System.currentTimeMillis();
                log.info("lotNo---{}---pvss开始请求---{}", lotNo, starTime);

                // 进行请求 数据
                String pvssStr = UrlConn.connUrl(plugin.getModel_url(), queryJson.toJSONString());//获取PVSS服务器数据接口
                long endTime = System.currentTimeMillis();
                log.info("lotNo---{}---pvss请求结束---{},请求耗时--{} 毫秒", lotNo, endTime, (endTime - starTime));

                // Objects.equals("Data list error", pvssStr) || Objects.equals("Data request error", pvssStr)
                if (pvssStr == null) {
                    log.error("请求 PVSS 服务器失败---{ }", pvssStr);
                } else {
                    handlePvss(addMsgList, lotNo, listPvss, pvssStr);
                }

            } catch (Exception e) {
                log.error("PVSS 请求失败！---{ }", e);
                e.printStackTrace();
            }
        }
    }

    public void handlePvss(List<PointL> messageList, long lotNo, List<PointL> list, String pvssStr) {
        JSONObject responseJson = JSONObject.parseObject(pvssStr);
        // 返回的批号
        String lotNoStr = responseJson.getString("lotNo");
        if (StringUtils.isBlank(lotNoStr)) {
            lotNoStr = String.valueOf(lotNo);
        }
        // 返回的数据
        JSONArray piontList = responseJson.getJSONArray("pointList");
        List<PointLTemp> listPvss = JSONArray.parseArray(piontList.toJSONString(), PointLTemp.class);
        if (listPvss.size() == list.size()) {
            int pi = 0;
            for (PointL pls : list) {
                PointLTemp pointLTemp = listPvss.get(pi);
                // 采集的原始值
                String orgValue = pointLTemp.getOrgValue();
                int pointId = pointLTemp.getPointId();
                // 采集的 点类型
                Integer type = pointLTemp.getType();
                // 采集的 时间戳
                Long timeStamp = pointLTemp.getTimeStamp();
                // 采集的 质量戳
                Integer qualityStamp = pointLTemp.getQualityStamp();
                // 采集的 数据类型
                Integer dataType = pointLTemp.getDataType();
                // 采集的 扩展字段
                String expandDesc = pointLTemp.getExpandDesc();
                if (pls.getPointId() == pointId) {
                    // 设置批号
                    pls.setLotNo(lotNoStr);
                    if (StringUtils.isBlank(orgValue)) {
                        pi++;
                        continue;
                    }
                    pls.setOldValue(orgValue);
                    pls.setHealthSign(HealthStatus.HEALTH_DATA.status());

                    if (type != null && type > 0) {
                        pls.setType(type);
                    }
                    if (timeStamp != null) {
                        pls.setTimeStrap(timeStamp);
                    }
                    if (qualityStamp != null) {
                        pls.setQualityStrap(qualityStamp);
                    }
                    if (dataType != null) {
                        pls.setDataType(dataType);
                    }
                    if (StringUtils.isNotBlank(expandDesc)) {
                        pls.setExpandDesc(expandDesc);
                    }

                    // 添加到 发送消息的 list
                    messageList.add(pls);
                }
                pi++;
            }
        }
    }

    // 进行 kafka 消息发送处理
    public void sendKafka(List<PointL> messageList) {
        int systemNum = 0;
        int number = 0;
        // 进行 kafka 消息发送处理
        if (!CollectionUtils.isEmpty(messageList)) {
            // 保证是同一批号的数据
            Map<String, List<PointL>> groupMap = messageList.stream().collect(Collectors.groupingBy(e -> e.getDataType() + e.getLevel() + e.getRelevanceId() + e.getLotNo()));
            Map<String, PointL> messageMap = new HashMap<>();
            for (String key : groupMap.keySet()) {
                List<PointL> pList = groupMap.get(key);
                int dataType = pList.get(0).getDataType();
                int level = pList.get(0).getLevel();
                int relevanceId = pList.get(0).getRelevanceId();
                String lotNo = pList.get(0).getLotNo();
                // 获取计算量
                int computeKey = dataType + level + relevanceId;
                List<PointL> computeList = computation.getMap(computeKey);
                if (!CollectionUtils.isEmpty(computeList)) {
                    for (PointL computePl : computeList) {
                        computePl.setLotNo(lotNo);
                        pList.add(computePl);
                    }
                }
//                sendKafkaSize(messageMap, pList);
                kafkaSend(messageMap, pList);
                systemNum++;
                number += pList.size();

            }
            // System.out.println("---- 总共有 " + systemNum + " 个系统, 共有- " + number + " -点");
        }
    }


    private void kafkaSend(Map<String, PointL> messageMap, List<PointL> pList) {
        for (PointL ent : pList) {
            messageMap.put(String.valueOf(ent.getPointId()), ent);
        }
        if (messageMap != null) {
            pvssCommonService.setMessageProduce(messageMap, pList.get(0));
            // System.out.println("系统id ---  " + pList.get(0).getRelevanceId() + "  有--： " + pList.size() + " 个点");
        }
    }


    private void sizeKafkaSend(Map<String, PointL> messageMap, List<PointL> pList) {
        int listSize = pList.size();
        if (listSize > 999) {
            int num = 1;
            for (PointL ent : pList) {
                messageMap.put(String.valueOf(ent.getPointId()), ent);
                if ((num % 1000) == 0) {
                    if (messageMap != null) {
                        pvssCommonService.setMessageProduce(messageMap, ent);
                    }
                }
                if (listSize % 1000 != 0 && num == listSize) {
                    if (messageMap != null) {
                        pvssCommonService.setMessageProduce(messageMap, ent);
                    }
                }

                num++;
            }
        } else if (listSize > 0 && listSize < 1000) {
            for (PointL ent : pList) {
                messageMap.put(String.valueOf(ent.getPointId()), ent);
            }
            if (messageMap != null) {
                pvssCommonService.setMessageProduce(messageMap, pList.get(0));
            }
        }
    }


//    @Override
//    public void invoke(List<PointL> taskArray, int flag) {
//        // 生成统一批号
//        long lotNo = SnowIdUtil.uniqueLong();
//
//        //判断当前任务是采集(0)还是下发（1）
//        if (flag == 0) {//采集:0
//            Gson gson = new Gson();
//            List<String> list = new ArrayList<>();
//            List<PointL> pointsl = new ArrayList<>();
//            List<PointL> messageList = new ArrayList<>();
//            int pointsDataLimit = 10000;//一组包的条数
//            int maxSize = taskArray.size() - 1;
//            int i = 0;
//            for (PointL pl : taskArray) {
//                // 填充参数
//                pl.setPointlsSign(plugin.getPointls_sign());
//                if (Objects.equals("pvss-device-1", plugin.getDevice_id())) {
//                    pl.setApplicationName(SpringBeanFactory.getBean("application_name").toString());
//                }
//                // 测试数据代码
//                if (Objects.equals("pvss-device-2", plugin.getDevice_id())) {
//                    pl.setApplicationName("PVSS-ApplictionName-2");
//                }
//                list.add(pl.getPointAddress());
//                pointsl.add(pl);
//                if (pointsDataLimit == list.size() || i == maxSize) {
//                    // pvss 请求对的地址转为  json 字符串
//                    String json = gson.toJson(list);
//                    try {
//                        // 进行请求 数据
//                        String pvss = UrlConn.connUrl(plugin.getModel_url(), json);//获取PVSS服务器数据接口
////                        if (Objects.equals("[\"Data list error\"]", pvss) || Objects.equals("[\"Data request error\"]", pvss)) {
//                        if (Objects.equals("Data list error", pvss) || Objects.equals("Data request error", pvss)) {
//                            System.out.println("Data list error！or ata request erro!");
//                            log.error("请求 PVSS 服务器失败---{ }", pvss);
//                        } else {
//                            /** 调用grpc传递数据*/
//                            // String realValue = PassValue.getRealValue(pvss);//统计数组数量，并清除数组类型的值
//                            // String[] strings = realValue.split(",");
//                            List<String> listPvss = JSONArray.parseArray(pvss, String.class);
//                            if (listPvss.size() == pointsl.size()) {
//                                int pi = 0;
//                                for (PointL pls : pointsl) {
//                                    String str = listPvss.get(pi);
//                                    if (StringUtils.isBlank(str)) {
//                                        pi++;
//                                        continue;
//                                    }
//                                    pls.setOldValue(str);
//                                    // pls.setTimeStrap(System.currentTimeMillis());
//                                    // if (!strings[pi].equals("%$")) {
//                                    // 添加到 发送消息的 list
//                                    messageList.add(pls);
//                                    // }
//                                    pi++;
//                                }
//                            }
//                        }
//                        list.clear();//清除遗留数据
//                        pointsl.clear();//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                i++;
//            }
//
//            // 进行 kafka 消息发送处理
//            if (!CollectionUtils.isEmpty(messageList)) {
//                Map<Integer, List<PointL>> groupMap = messageList.stream().collect(Collectors.groupingBy(e -> e.getDataType() + e.getLevel() + e.getRelevanceId()));
//                Map<String, PointL> messageMap = new HashMap<>();
//                for (Integer key : groupMap.keySet()) {
//                    List<PointL> pList = groupMap.get(key);
//                    long timeStrap = System.currentTimeMillis();
//                    int listSize = pList.size();
//                    if (listSize > 999) {
//                        int num = 1;
//                        for (PointL ent : pList) {
//                            ent.setTimeStrap(timeStrap);
//                            messageMap.put(String.valueOf(ent.getPointId()), ent);
//                            if ((num % 1000) == 0) {
//                                if (messageMap != null) {
//                                    pvssCommonService.setMessageProduce(messageMap, ent);
//                                }
//                            }
//                            if (listSize % 1000 != 0 && num == listSize) {
//                                if (messageMap != null) {
//                                    pvssCommonService.setMessageProduce(messageMap, ent);
//                                }
//                            }
//
//                            num++;
//                        }
//                    } else if (listSize > 0 && listSize < 1000) {
//                        for (PointL ent : pList) {
//                            ent.setTimeStrap(timeStrap);
//                            messageMap.put(String.valueOf(ent.getPointId()), ent);
//                        }
//                        if (messageMap != null) {
//                            pvssCommonService.setMessageProduce(messageMap, pList.get(0));
//                        }
//                    }
//
//
//                }
//
//            }
//
//        }
//
//        LOGGER.info("{}线程执行pvss任务:{}完毕.........", Thread.currentThread().getName(), plugin.getModel_host());
//
//        countDownLatch.countDown();
//    }


//    @Override
//    public void invoke(List<PointL> taskArray, int flag) {
//        //判断当前任务是采集(0)还是下发（1）
//        if (flag == 0) {//采集:0
//            Gson gson = new Gson();
//            List<String> list = new ArrayList<>();
//            List<PointL> pointsl = new ArrayList<>();
//            int pointsDataLimit = 10000;//一组包的条数
//            int maxSize = taskArray.size() - 1;
//            int i = 0;
//            for (PointL pl : taskArray) {
//
//                // 填充参数
//                pl.setPointlsSign(plugin.getPointls_sign());
//                if (Objects.equals("pvss-device-1", plugin.getDevice_id())) {
//                    pl.setApplicationName(SpringBeanFactory.getBean("application_name").toString());
//                }
//                // 测试数据代码
//                if (Objects.equals("pvss-device-2", plugin.getDevice_id())) {
//                    pl.setApplicationName("PVSS-ApplictionName-2");
//                }
//
//                list.add(pl.getPointAddress());
//                pointsl.add(pl);
//                if (pointsDataLimit == list.size() || i == maxSize) {
//                    // pvss 请求对的地址转为  json 字符串
//                    String json = gson.toJson(list);
//                    try {
//                        // 进行请求 数据
//                        String pvss = UrlConn.connUrl(plugin.getModel_url(), json);//获取PVSS服务器数据接口
//                        if (Objects.equals("[\"Data list error\"]", pvss) || Objects.equals("[\"Data request error\"]", pvss)) {
//                            System.out.println("Data list error！or ata request erro!");
//                            log.error("请求 PVSS 服务器失败---{ }", pvss);
//                        } else {
//                            /** 调用grpc传递数据*/
//                            String realValue = PassValue.getRealValue(pvss);//统计数组数量，并清除数组类型的值
//                            String[] strings = realValue.split(",");
//                            if (strings.length == pointsl.size()) {
//                                int pi = 0;
//                                List<PointL> points = new ArrayList<>();
//                                for (PointL pls : pointsl) {
//                                    pls.setOldValue(strings[pi]);
//                                    if (!strings[pi].equals("%$")) {
//                                        points.add(pls);
//                                    }
//                                    pi++;
//                                }
//                                // 直接放到 kafka 里面不在调用 Grpc 服务清洗
//                                // kafka  是按照1000条数据进行分包处理的
//
//
//                                Boolean entity = pointSavantService.getEntity(points, "PVSS");//调用grpc传递PVSS数据包给上游
//                                LOGGER.info("---------->采集数据上行推送:" + entity + ":" + points + "<---------");
//                            }
//                        }
//                        list.clear();//清除遗留数据
//                        pointsl.clear();//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                i++;
//            }
//        }
//
////        else if (flag == 1) {//下发：1
//////            String value = PassValue.downHairValue(taskArray, plugin.getIssue_url());//下发接口调用
//////            LOGGER.info("---------->下发:"+value+"<---------");
////        }
//
//        LOGGER.info("{}线程执行pvss任务:{}完毕.........", Thread.currentThread().getName(), plugin.getModel_host());
//        countDownLatch.countDown();
//    }

    /**
     * @return java.lang.Object
     * @Method invokeForBack
     * @Param [taskArray, flag]
     * @Description 任务下发
     */
    @Override
    public Object invokeForBack(List<PointL> taskArray, int flag) {
        List<PointL> listReturn = new ArrayList<>();
        if (flag == 1) {
            // 对 List<PointL> taskArray 进行分组
            Map<String, List<PointL>> groupMap = taskArray.stream().collect(Collectors.groupingBy(e -> e.getDeviceId()));
            List<PointL> pointList = groupMap.get(plugin.getDevice_id());
            if (!CollectionUtils.isEmpty(pointList)) {
                listReturn = PassValue.downHairValue(taskArray, plugin.getIssue_url());//下发接口调用
                log.info("---------->任务下发失败的点集合:" + listReturn.toString() + "<---------");
            }
        }
        return listReturn;
    }

    @Override
    public void config(Object... objs) {
        countDownLatch = (CountDownLatch) objs[0];
        this.setResult((Integer) objs[1]);
        this.setPlugin((MonitorMuster.Plugin) objs[2]);
        this.setWork_plugins((List<MonitorMuster.Plugin>) objs[3]);
        List<MonitorMuster.Plugin> pluginList = (List<MonitorMuster.Plugin>) objs[3];
        //设置url
        if (!CollectionUtils.isEmpty(pluginList)) {
            for (MonitorMuster.Plugin plu : pluginList) {
                // 根据device_id 进行区分
                currentUrl.put(plu.getDevice_id(), plu);
            }
        }
    }

    @Override
    public int getGather_count() {
        return this.getResult();
    }
}
