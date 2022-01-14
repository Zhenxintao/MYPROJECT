package com.bmts.heating.commons.utils.tdengine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Collections;
import java.util.List;

public class InstrumentInfo {

    /**
     * JSONArray排序
     *
     * @param data     需要排序的JSONArray
     * @param sortName 排序名称
     * @param sortType 排序类型 true为升序；false为倒序
     */
    public static JSONArray sortJsonArray(JSONArray data, String sortName, Boolean sortType) {
//        if (sortName.contains(".consumption"))
//        {
//            sortName.replaceAll("[.](.*)","");
//        }
        String name = sortName.replaceAll("[.](.*)", "");
        List<JSONObject> list = JSONObject.parseArray(data.toJSONString(), JSONObject.class);
        Collections.sort(list, (JSONObject o1, JSONObject o2) -> {
            //转成JSON对象中保存的值类型
            double a = Double.parseDouble(o1.getString(name));
            double b = Double.parseDouble(o2.getString(name));
            // 如果a, b数据类型为int，可直接 return a - b ;(升序，降序为 return b - a;)
            if (sortType) {
                if (a > b) {  //升序排列，降序改成a<b
                    return 1;
                } else if (a == b) {
                    return 0;
                } else {
                    return -1;
                }
            } else {
                if (a < b) {  //降序排列，升序改成a>b
                    return 1;
                } else if (a == b) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        data = JSONArray.parseArray(JSON.toJSONString(list));
        return data;
    }
}
