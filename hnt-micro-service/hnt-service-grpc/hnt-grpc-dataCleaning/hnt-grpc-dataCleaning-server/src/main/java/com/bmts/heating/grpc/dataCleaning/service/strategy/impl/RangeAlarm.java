package com.bmts.heating.grpc.dataCleaning.service.strategy.impl;

import com.bmts.heating.commons.utils.msmq.PointL;
import com.bmts.heating.grpc.dataCleaning.annotation.Clean;
import com.bmts.heating.grpc.dataCleaning.enums.DataCleanType;
import com.bmts.heating.grpc.dataCleaning.service.strategy.DataCleanStrategy;
import com.bmts.heating.grpc.dataCleaning.service.strategy.pojo.RangeAlarmBO;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 清洗策略  超量程警报
 */
@Component
@Clean(cleanType = DataCleanType.RANGE_ALARM)
public class RangeAlarm implements DataCleanStrategy<PointL> {

    private static final Gson gson = new Gson();

    @Override
    public PointL dataClean(PointL pointL) {
        String value = pointL.getValue();
        String expandDesc = pointL.getExpandDesc();
        if (!StringUtils.isEmpty(value) && !StringUtils.isEmpty(expandDesc)) {
            // 进行是否超量程警报判断
            RangeAlarmBO rangeAlarmBO = gson.fromJson(expandDesc, RangeAlarmBO.class);
            int qualityStrap = quality(rangeAlarmBO, value);
            pointL.setQualityStrap(qualityStrap);
        }
        return pointL;
    }

    private int quality(RangeAlarmBO rangeAlarmBO, String value) {
        int intValue = Integer.parseInt(value);
        int qualityStrap = 192;
        // 超量程
        if (intValue > rangeAlarmBO.getRange().getH() || intValue < rangeAlarmBO.getRange().getL()) {
            qualityStrap = 0;
        }
        // 超高线
        if (intValue >= rangeAlarmBO.getAlarm().getH() && intValue <= rangeAlarmBO.getAlarm().getHh()) {
            qualityStrap = 2;
        }
        // 低低线
        if (intValue >= rangeAlarmBO.getAlarm().getLl() && intValue <= rangeAlarmBO.getAlarm().getL()) {
            qualityStrap = 1;
        }
        return qualityStrap;
    }
}
