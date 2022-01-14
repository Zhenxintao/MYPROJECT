package com.bmts.heating.grpc.dataCleaning.service.analyse;

import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.grpc.dataCleaning.service.strategy.DataCleanStrategy;
import com.bmts.heating.grpc.dataCleaning.utils.CleanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JKPointCleanService {

    @Autowired
    private PointCommonService pointCommonService;


    /**
     * 原生数据 解析  根据策略进行清洗
     *
     * @param list
     * @return
     */
    public boolean jkClean(List<PointL> list) {
        // 按照   一次网、二次网、用户数据 和  站点 进行分组
        Map<Integer, List<PointL>> groupMap = pointCommonService.group(list);
        Map<String, PointL> messageMap = new HashMap<>();
        for (Integer key : groupMap.keySet()) {
            List<PointL> pList = groupMap.get(key);
            int listSize = pList.size();
            if (listSize > 999) {
                int num = 1;
                for (PointL ent : pList) {
                    PointL cleaning = cleaning(ent);
                    if (cleaning != null) {
                        messageMap.put(String.valueOf(ent.getPointId()), ent);
                    }

                    if ((num % 1000) == 0) {
                        if (messageMap != null) {
                            pointCommonService.setMessageProduce(messageMap, ent);
                        }
                    }
                    if (listSize % 1000 != 0 && num == listSize) {
                        if (messageMap != null) {
                            pointCommonService.setMessageProduce(messageMap, ent);
                        }
                    }

                    num++;
                }
            } else if (listSize > 0 && listSize < 1000) {
                for (PointL ent : pList) {
                    PointL cleaning = cleaning(ent);
                    if (cleaning != null) {
                        messageMap.put(String.valueOf(ent.getPointId()), ent);
                    }
                }
                if (messageMap != null) {
                    pointCommonService.setMessageProduce(messageMap, pList.get(0));
                }

            }


        }


        return true;
    }

    /**
     * 原生数据 解析  根据策略进行清洗
     *
     * @param
     * @return
     */
//    public boolean jkClean(List<PointL> list) {
//        // 按照   一次网、二次网、用户数据 和  站点 进行分组
//        Map<String, List<PointL>> groupMap = pointCommonService.group(list);
//        Map<String, PointL> messageMap = new HashMap<>();
//        for (String key : groupMap.keySet()) {
//            List<PointL> pList = groupMap.get(key);
//            int listSize = pList.size();
//            if (listSize > 999) {
//                int num = 1;
//                for (PointL ent : pList) {
//                    PointL cleaning = cleaning(ent);
//                    if (cleaning != null) {
//                        messageMap.put(String.valueOf(ent.getPointId()), ent);
//                    }
//
//                    if ((num % 1000) == 0) {
//                        if (messageMap != null) {
//                            pointCommonService.setMessageProduce(messageMap, ent);
//                        }
//                    }
//                    if (listSize % 1000 != 0 && num == listSize) {
//                        if (messageMap != null) {
//                            pointCommonService.setMessageProduce(messageMap, ent);
//                        }
//                    }
//
//                    num++;
//                }
//            } else if (listSize > 0 && listSize < 1000) {
//                for (PointL ent : pList) {
//                    PointL cleaning = cleaning(ent);
//                    if (cleaning != null) {
//                        messageMap.put(String.valueOf(ent.getPointId()), ent);
//                    }
//                }
//                if (messageMap != null) {
//                    pointCommonService.setMessageProduce(messageMap, pList.get(0));
//                }
//
//            }
//
//
//        }
//
//
//        return true;
//    }

    // 清洗解析数据
    private PointL cleaning(PointL point) {
        point.setTimeStrap(System.currentTimeMillis());
        int[] washArray = point.getWashArray();
        if (washArray != null && washArray.length > 0) {
            for (int i : washArray) {
                DataCleanStrategy cleanStrategy = CleanUtil.getCleanStrategy(i);
                if (cleanStrategy != null) {
                    cleanStrategy.dataClean(point);
                }
            }

            return point;
        } else {

            DataCleanStrategy cleanStrategy = CleanUtil.getCleanStrategy(1);
            if (cleanStrategy != null) {
                cleanStrategy.dataClean(point);
            }
            return point;

//            return null;
        }
    }


}
