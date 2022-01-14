package com.bmts.heating.web.energy.service;

import com.bmts.heating.web.energy.pojo.EnergyInfoDto;
import com.bmts.heating.commons.utils.restful.Response;

/**
 * @author naming
 * @description
 * @date 2021/5/7 10:54
 **/

public interface EnergyEvaluateService {
    /**
     * 评价统计
     * @return
     */
    Response evaluateStatistics(EnergyInfoDto dto);
}
