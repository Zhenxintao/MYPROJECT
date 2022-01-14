package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.db.entity.EnergyEvaluateHistory;
import com.bmts.heating.commons.basement.model.db.response.energy.ReachStandardResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author naming
 * @since 2021-04-27
 */
public interface EnergyEvaluateHistoryMapper extends BaseMapper<EnergyEvaluateHistory> {
    @Select("SELECT qualified, COUNT( * ) as count FROM energy_evaluate_history ${ew.customSqlSegment} \n" +
            "GROUP BY qualified")
    List<ReachStandardResponse> queryReach(@Param(Constants.WRAPPER) LambdaQueryWrapper<EnergyEvaluateHistory> queryWrapper);
}
