package com.bmts.heating.monitor.second.config;

import com.bmts.heating.commons.entiy.second.request.device.PointConfigSecondDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: SecondPointConfig
 * @Description: 加载二网数据配置
 * @Author: pxf
 * @Date: 2021/4/20 19:51
 * @Version: 1.0
 */
@Component
public class SecondPointConfig {

    public static Map<String, List<PointConfigSecondDto>> secondMap = new HashMap<String, List<PointConfigSecondDto>>();

    public void setMap(List<PointConfigSecondDto> list) {
        if (secondMap.size() > 0) {
            secondMap.clear();
        }
        Map<String, List<PointConfigSecondDto>> collect = list.stream().collect(Collectors.groupingBy(e -> e.getCode()));
        secondMap.putAll(collect);
    }

    public List<PointConfigSecondDto> getMap(String key) {
        if (StringUtils.isNotBlank(key)) {
            return secondMap.get(key);
        }
        return Collections.emptyList();
    }

}
