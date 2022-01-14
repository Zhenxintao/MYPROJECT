package com.bmts.heating.commons.entiy.balance.pojo;

import lombok.Data;
import org.springframework.context.annotation.Description;

/**
 * 泵步幅步长
 * @description
 * @date 2021/1/29 12:20
 **/
@Data
@Description("全网平衡计算服务入参")
public class Sectionpump
{
    //高限(%)当该值为0时代表不在所有区间之外的计算方法或步幅(如5%)
    public float limit;

    // 低限(%)当该值为0时代表使用计算公式计算,当该值为-1时代表死区范围内不做调整
    public float step ;
}
