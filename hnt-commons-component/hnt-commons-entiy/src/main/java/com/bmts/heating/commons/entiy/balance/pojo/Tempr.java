package com.bmts.heating.commons.entiy.balance.pojo;

import lombok.Data;
import org.springframework.context.annotation.Description;

/**
 * 回温设定
 * @description
 * @date 2021/1/29 12:20
 **/
@Data
@Description("全网平衡计算服务入参")
public class Tempr
{
    // 高限(℃)
    public float hi ;

    // 低限(℃)
    public float lo ;
}
