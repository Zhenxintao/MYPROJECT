package com.bmts.heating.commons.utils.compute;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Description;

import java.text.DecimalFormat;
import java.util.*;

@Slf4j
@Description("工具类")
public class CountUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(CountUtils.class);
    private static DecimalFormat df = new DecimalFormat("0.00");

    public static Map<String, Integer> divisionOperate(int a, int b) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        if (a % b == 0) {
            a = a / b;
            b = 0;
            map.put("quotient", a);
            map.put("mod", b);
        } else {
            int c = (int) Math.ceil(a / b);
            int d = Math.floorMod(a, b);
            map.put("quotient", c);
            map.put("mod", d);
        }
        return map;
    }

    public static void main(String[] arg) {
        Map<String, Integer> map = divisionOperate(9999, 4);
        LOGGER.info("{}", map.get("quotient"));
        LOGGER.info("{}", map.get("mod"));

        List<String> slist = new ArrayList<String>();
        for (int i = 0; i < 22; i++) {
            slist.add(i + "");
        }
        List<List<String>> rows = subListBySegment(slist, 3);
        LOGGER.info("size:{}", rows.size());
    }

    /**
     * 分隔数组 根据段数分段 <多出部分在最后一个数组>
     *
     * @param data     被分隔的数组
     * @param segments 需要分隔的段数
     * @return
     */
    public static <T> List<List<T>> subListBySegment(List<T> data, int segments) {

        List<List<T>> result = new ArrayList<>();

        int size = data.size();// 数据长度

        if (size > 0 && segments > 0) {// segments == 0 ，不需要分隔

            int count = size / segments;// 每段数量

            List<T> cutList = null;// 每段List

            for (int i = 0; i < segments; i++) {
                if (i == segments - 1) {
                    cutList = data.subList(count * i, size);
                } else {
                    cutList = data.subList(count * i, count * (i + 1));
                }
                result.add(cutList);
                System.out.println(result);
            }
        } else {
            result.add(data);
        }
        return result;
    }

    /**
     * 分隔数组 根据段数分段 <多出部分在最后一个数组>
     *
     * @param data     被分隔的数组
     * @param segments 需要分隔的段数
     * @return
     */
    public static <T> List<List<T>> subListToBySegment(List<List<T>> data, int segments) {
        List<List<T>> result = new ArrayList<>();
        int size = data.size();// 数据长度
        if (size > 0 && segments > 0) {// segments == 0 ，不需要分隔
            int count = size / segments;// 每段数量
//            List<List<T>> cutList = null;// 每段List
            for (int i = 0; i < segments; i++) {
                List<T> groupList = new ArrayList<>();
                if (i == segments - 1) {
                    List<List<T>> cutList = data.subList(count * i, size);
                    for (List<T> list : cutList) {
                        groupList.addAll(list);
                    }
                } else {
                    List<List<T>> cutList = data.subList(count * i, count * (i + 1));
                    for (List<T> list : cutList) {
                        groupList.addAll(list);
                    }
                }
//                result.add(cutList);
                result.add(groupList);
                // System.out.println("------分组 " + result.size() + " 数据条数 ：--- " + groupList.size());
                log.info("------分组---{}数据条数 ：---{}", result.size(), groupList.size());
            }
        } else {
//            result.add(data);
            result.addAll(data);
        }
        return result;
    }


    /**
     * 分隔数组 根据每段数量分段
     *
     * @param datas     被分隔的数组
     * @param splitSize 每段数量
     * @return
     */
    private static <T> List<List<T>> subListByCount(List<T> datas, int splitSize) {
        if (datas == null || splitSize < 1) {
            return null;
        }
        int totalSize = datas.size();
        int count = (totalSize % splitSize == 0) ?
                (totalSize / splitSize) : (totalSize / splitSize + 1);

        List<List<T>> rows = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            List<T> cols = datas.subList(i * splitSize,
                    (i == count - 1) ? totalSize : splitSize * (i + 1));
            rows.add(cols);
            System.out.println(cols);
        }
        return rows;
    }

    /**
     * 计算百分比
     *
     * @param numerator      分子
     * @param baidenominator 分母
     * @return
     */
    public static String percentage(long numerator, long baidenominator) {
        double rate_memory = numerator * 1.0 / baidenominator;
        return df.format(rate_memory * 100) + "%";
    }

    /**
     * 删除字符串中某个元素(逗号分隔)
     *
     * @param ips
     * @param targetIp
     * @return
     */
    public static String filterIps(String ips, String targetIp) {
        String[] arrs = ips.split(",");
        List<String> stringList = Arrays.asList(arrs);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < stringList.size(); i++) {
            if (!stringList.get(i).equals(targetIp))
                stringBuilder.append(stringList.get(i)).append(",");
        }
        String ss = stringBuilder.toString();
        if (!"".equals(ss))
            return ss.substring(0, ss.length() - 1);
        else
            return "";

    }
}
