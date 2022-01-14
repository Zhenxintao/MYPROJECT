package com.bmts.heating.stream.flink.handle;

import com.bmts.heating.commons.entiy.common.PointProperties;
import com.bmts.heating.commons.utils.msmq.Message_Point_Gather;
import com.bmts.heating.commons.utils.msmq.PointL;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.nfunk.jep.JEP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @ClassName: TestFlatMap
 * @Description: 测试 flink
 * @Author: pxf
 * @Date: 2021/7/21 16:59
 * @Version: 1.0
 */

public class RealComputeRichMap extends RichMapFunction<Message_Point_Gather, Message_Point_Gather> {

    private static Logger logger = LoggerFactory.getLogger(RealComputeRichMap.class);

    @Override
    public Message_Point_Gather map(Message_Point_Gather pointGather) throws Exception {
        logger.info("-----RealComputeRichMap------ 接收对象数据-----systemId= {}", pointGather.getRelevanceId());
        Map<String, PointL> pointMap = pointGather.getPointLS();
        // 进行计算
        compute(pointMap);

        return pointGather;
    }

    private void compute(Map<String, PointL> pointMap) {
        //  Map
        Map<String, String> collectMap = new HashMap<>();
        pointMap.forEach((k, v) -> {
                    if (v != null) {
                        if (StringUtils.isNotBlank(v.getPointName()) && StringUtils.isNotBlank(v.getValue())) {
                            collectMap.put(v.getPointName(), v.getValue());
                        }
                    }
                }
        );
        // 对 Map  进行操作  单系统内计算   实时计算
        pointMap.forEach((pointId, point) -> {
                    if (point != null && point.getPointConfig() == PointProperties.Compute.type()) {
                        if (StringUtils.isNotBlank(point.getExpression())) {
                            try {
                                JEP jep = new JEP();
                                // 计算表达式
                                String expression = point.getExpression();
                                // 进行正则验证 表达式  [^a-zA-Z0-9]
                                String trim = Pattern.compile("[^a-zA-Z0-9]").matcher(expression).replaceAll(" ").trim();
                                if (StringUtils.isNotBlank(trim)) {
                                    // 去除空字符串
                                    String[] split = trim.split("\\s+");
                                    for (String str : split) {
                                        String pointLValue = collectMap.get(str);
                                        if (StringUtils.isNotBlank(pointLValue)) {
                                            jep.addVariable(str, Double.parseDouble(pointLValue));
                                        }
                                    }
                                    // 执行计算
                                    jep.parseExpression(expression);
                                    // 计算结果
                                    double result = jep.getValue();
                                    point.setValue(String.format("%.2f", result));
                                    // 设置时间
                                    point.setTimeStrap(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));

                                    // 进行计算逻辑处理
                                    logger.info("Compute---计算数据：name={},level={},releverceId={},type={},timeStrap={},value={}",
                                            point.getPointName(), point.getLevel(), point.getRelevanceId(), point.getType(), point.getTimeStrap(), point.getValue());

                                }
                            } catch (Exception e) {
                                logger.error("Compute---异常数据---:name={},level={},releverceId={},type={},timeStrap={}," +
                                        point.getPointName(), point.getLevel(), point.getRelevanceId(), point.getType(), point.getTimeStrap());
                                e.printStackTrace();
                            }
                        } else {
                            point.setValue("0.00");
                            // 设置时间
                            point.setTimeStrap(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));
                        }
                    }
                }
        );
        collectMap.clear();
    }

}
