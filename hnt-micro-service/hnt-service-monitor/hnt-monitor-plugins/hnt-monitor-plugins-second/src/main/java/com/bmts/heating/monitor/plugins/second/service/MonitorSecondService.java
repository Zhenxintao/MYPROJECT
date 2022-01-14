package com.bmts.heating.monitor.plugins.second.service;

import com.bmts.heating.commons.entiy.second.request.device.PointSecondDto;
import com.bmts.heating.monitor.plugins.second.service.async.SecondAsync;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @ClassName: MonitorSecondService
 * @Description: MonitorSecond 业务处理
 * @Author: pxf
 * @Date: 2022/1/6 14:27
 * @Version: 1.0
 */
@Service
public class MonitorSecondService {

    @Autowired
    private SecondAsync secondAsync;


    public void updatList(List<PointSecondDto> list) {
        if (!CollectionUtils.isEmpty(list)) {
            list.stream().forEach(dto -> {
                if (StringUtils.isNotBlank(dto.getDeviceCode()) && StringUtils.isNotBlank(dto.getLevel())
                        && StringUtils.isNotBlank(dto.getTableName())) {
                    secondAsync.secondReal(dto);
                }
            });
        }

    }
}
