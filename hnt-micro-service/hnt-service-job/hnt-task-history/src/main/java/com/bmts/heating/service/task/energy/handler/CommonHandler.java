package com.bmts.heating.service.task.energy.handler;

import com.bmts.heating.commons.basement.model.db.entity.EnergyEvaluateHistory;
import com.bmts.heating.commons.entiy.energy.EnergyType;
import com.bmts.heating.commons.entiy.energy.EvaluateRateType;
import com.bmts.heating.commons.entiy.energy.EvaluateTarget;
import com.bmts.heating.commons.utils.common.Tuple3;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author naming
 * @description
 * @date 2021/4/28 14:09
 **/
@Service
public class CommonHandler {
    /**
     * @param evaluate       评价、是否合格
     * @param relevanceId    关联id
     * @param name           热网或热力站名称
     * @param value          值
     * @param energyType     水电热
     * @param evaluateTarget 1.热力站 2.热网
     * @return
     */
    public EnergyEvaluateHistory buildDbDatas(Tuple3<String, EvaluateRateType, BigDecimal> evaluate, int relevanceId, String name, BigDecimal value, EnergyType energyType, EvaluateTarget evaluateTarget) {
        EnergyEvaluateHistory energyEvaluateHistory = new EnergyEvaluateHistory();
        energyEvaluateHistory
                .setRelevanceId(relevanceId)
                .setEvaluate(evaluate.first)
                .setQualified(evaluate.second.ordinal()+1)
                .setLevel(evaluateTarget.ordinal() + 1)
                .setEvaluateTime(LocalDateTime.now().toLocalDate().plusDays(-1))
                .setCreateTime(LocalDateTime.now())
                .setName(name)
                .setStandardValue(evaluate.three)
                .setType(energyType.ordinal() + 1)
                .setValue(value);
        return energyEvaluateHistory;
    }

}
