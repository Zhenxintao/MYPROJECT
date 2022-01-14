package com.bmts.heating.monitor.plugins.pvss.constructors.service;

import com.bmts.heating.monitor.plugins.pvss.constructors.PvssWorker;
import lombok.Data;

/**
 * 负荷预测 业务 实体类
 */
@Data
public class PvssServiceDto {

    // 计算业务service  接口
    private PvssWorker pvssWorker;


}
