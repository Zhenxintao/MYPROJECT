package com.bmts.heating.commons.entiy.energy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author naming
 * @description
 * @date 2021/4/29 14:42
 **/
@Data
@ApiModel("达标dto")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvalulateReachStandard {
    @ApiModelProperty("日期")
    private LocalDate date;
    @ApiModelProperty("站、源、网")
    private EvaluateTarget target;
    @ApiModelProperty("水电热")
    private EnergyType energyType;
}
