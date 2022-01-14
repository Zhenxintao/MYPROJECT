package com.bmts.heating.grpc.dataCleaning.service.analyse;

import com.bmts.heating.commons.utils.msmq.PointL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PVSSPointCleanService {

    @Autowired
    private PointCommonService pointCommonService;

    /**
     * 原生数据 解析  根据策略进行清洗
     *
     * @param list
     * @return
     */
    public boolean pvssClean(List<PointL> list) {
        Map<Integer, List<PointL>> groupMap = pointCommonService.group(list);
        long statTime1 = System.currentTimeMillis();
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
        System.out.println(list.size() + " 条数据清洗耗时：" + (System.currentTimeMillis() - statTime1));

        return true;

    }


    // 清洗解析数据
    private PointL cleaning(PointL point) {
        point.setTimeStrap(System.currentTimeMillis());
        String oldStr = point.getOldValue();
        if (!StringUtils.isEmpty(oldStr) && !oldStr.contains("\"")) {
            point.setValue(oldStr);
            return point;
        } else {
            return null;
        }
    }


}
