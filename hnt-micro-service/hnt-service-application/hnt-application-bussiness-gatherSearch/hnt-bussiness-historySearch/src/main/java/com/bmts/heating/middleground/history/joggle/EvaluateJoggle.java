package com.bmts.heating.middleground.history.joggle;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.EnergyEvaluateHistory;
import com.bmts.heating.commons.basement.model.db.response.energy.ReachStandardResponse;
import com.bmts.heating.commons.db.mapper.EnergyEvaluateHistoryMapper;
import com.bmts.heating.commons.db.service.EnergyEvaluateHistoryService;
import com.bmts.heating.commons.entiy.energy.EvalulateReachStandard;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/4/29 11:43
 **/
@RestController
@Api(tags = "能耗评价")
@RequestMapping("/energy/evaluate/home")
public class EvaluateJoggle {

    @Autowired
    EnergyEvaluateHistoryMapper energyEvaluateHistoryMapper;
    @PostMapping
    public List<ReachStandardResponse> reachStandard(@RequestBody EvalulateReachStandard evalulateReachStandard) {
        LambdaQueryWrapper<EnergyEvaluateHistory> lambdaQueryWrapper = Wrappers.<EnergyEvaluateHistory>lambdaQuery()
                .eq(EnergyEvaluateHistory::getLevel, evalulateReachStandard.getTarget().ordinal() + 1)
                .eq(EnergyEvaluateHistory::getEvaluateTime, evalulateReachStandard.getDate())
                .eq(EnergyEvaluateHistory::getType, evalulateReachStandard.getEnergyType().ordinal() + 1);
        List<ReachStandardResponse> reachStandardResponses = energyEvaluateHistoryMapper.queryReach(lambdaQueryWrapper);
        return reachStandardResponses;
    }
    @PostMapping("/reachStandardAll")
    public List<ReachStandardResponse> reachStandardAll(@RequestBody EvalulateReachStandard evalulateReachStandard) {
        LambdaQueryWrapper<EnergyEvaluateHistory> lambdaQueryWrapper = Wrappers.<EnergyEvaluateHistory>lambdaQuery().eq(EnergyEvaluateHistory::getLevel, evalulateReachStandard.getTarget().ordinal() + 1).eq(EnergyEvaluateHistory::getEvaluateTime, evalulateReachStandard.getDate());
        List<ReachStandardResponse> reachStandardResponses = energyEvaluateHistoryMapper.queryReach(lambdaQueryWrapper);
        return reachStandardResponses;
    }
}

