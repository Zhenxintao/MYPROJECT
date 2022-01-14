package com.bmts.heating.monitor.plugins.pvss.constructors.pvssUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bmts.heating.commons.utils.msmq.PointL;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PassValue {
    public static List<PointL> downHairValue(List<PointL> taskArray, String url) {
        int pointsDataLimit = 10000;//数据包条目
        int maxSize = taskArray.size() - 1;
        int i = 0;
        List<PointL> returnList = new ArrayList<>();
        List<PointL> tempList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();
        for (PointL pl : taskArray) {
            JSONObject jsonObj = new JSONObject();
//            jsonObj.put("name", pl.getPointAddress());
            // 参量名称
            jsonObj.put("name", pl.getPointName());
            jsonObj.put("value", pl.getValue());
            jsonObj.put("pointType", "ValueParaDesc");

            Integer parentSyncNum = pl.getParentSyncNum();
            if (parentSyncNum <= 9999) {
                // 表示是热源   热源
                jsonObj.put("parentNum", String.valueOf(pl.getParentSyncNum()));
            } else {
                // 表示是热力站
                jsonObj.put("parentNum", pl.getParentSyncNum());
                // 机组编号
                jsonObj.put("narrayNo", pl.getSystemNum());
            }
            jsonArray.add(jsonObj);
            tempList.add(pl);
            if (pointsDataLimit == jsonArray.size() || i == maxSize) {
                String json = jsonArray.toJSONString();
                try {
                    /**调用下发接口，将数据返回PVSS服务器*/
                    String pvss = UrlConn.connUrl(url, json);//下发接口+序列化数据
                    if (pvss.contains("code")) {
                        JSONObject jsonPvss = JSONObject.parseObject(pvss);
                        if (Integer.parseInt(jsonPvss.get("code").toString()) < 0) {
                            returnList.addAll(tempList);
                        }
                    } else {
                        List<IssueData> listIsuues = JSONArray.parseArray(pvss, IssueData.class);
                        // 筛选出为 null 的点
                        for (int n = 0; n < listIsuues.size(); n++) {
                            if (StringUtils.isEmpty(listIsuues.get(n).getValue())) {
                                returnList.add(tempList.get(n));
                            }
                        }
                    }
                    tempList.clear();
                    jsonArray.clear();//清除遗留数据
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            i++;
        }
        return returnList;
    }

    public static String getRealValue(String value) {
        String context = value.substring(1, value.length() - 1);//去掉最外层的[]
        int head = context.indexOf('['); // 标记第一个使用左括号的位置
        if (head == -1) {
            return context;
        } else {
            int next = head + 1; // 从head+1起检查每个字符
            int count = 1; // 记录括号情况
            do {
                if (context.charAt(next) == '[') {
                    count++;
                } else if (context.charAt(next) == ']') {
                    count--;
                }
                next++; // 更新即将读取的下一个字符的位置
                if (count == 0) { // 已经找到匹配的括号
                    String temp = context.substring(head, next);
                    context = context.replace(temp, "%$"); // 用特殊字符替换，复制给context
                    head = context.indexOf('['); // 找寻下一个左括号
                    next = head + 1; // 标记下一个左括号后的字符位置
                    count = 1; // count的值还原成1
                }
            } while (head != -1); // 如果在该段落中找不到左括号了，就终止循环
        }
        return context;
    }
}

@Data
class IssueData {
    private String name;
    private String value;
}
