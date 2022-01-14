package com.bmts.heating.commons.entiy.energy;

import lombok.Data;
import org.springframework.context.annotation.Description;

import java.math.BigDecimal;

/**
 * @author naming
 * @description
 * @date 2021/4/27 19:16
 **/
@Data
@Description("区间值")
public class EvaluateSection {
    private float up;
    private float down;
}
