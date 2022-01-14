package com.bmts.heating.service.task.energy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author naming
 * @description
 * @date 2021/4/27 18:33
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnergySystemData {
    BigDecimal HM_HT;
    BigDecimal WM_FT;
    BigDecimal ep;
    BigDecimal HeatSourceEp;
    BigDecimal HeatSourceTotalHeat_MtrG;
    BigDecimal HeatSourceFTSupply;
    int relevanceId;
}
