package com.bmts.heating.monitor.plugins.second.service.async;

import com.bmts.heating.commons.entiy.second.request.device.PointSecondDto;
import com.bmts.heating.monitor.plugins.second.service.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @ClassName: SecondAsync
 * @Description: 二网异步处理
 * @Author: pxf
 * @Date: 2022/1/6 16:23
 * @Version: 1.0
 */
@Component
public class SecondAsync {

    @Autowired
    private MongoService mongoService;

    @Async("asyncMongoDB")
    public void secondReal(PointSecondDto dto) {
        if (dto.getTimestamp() == 0L) {
            dto.setTimestamp(System.currentTimeMillis());
        }
        mongoService.updatSert(dto);
    }
}
