package com.bmts.heating.commons.entiy.balance.pojo;

import lombok.Data;
import org.springframework.context.annotation.Description;

import java.util.List;


/**
 * @description
 * @date 2021/1/29 12:20
 **/
@Data
@Description("全网平衡计算服务入参")
 public class ComputDo {

    // 目标均温(targetlast)计算模式 0-自动计算 1-手动给定
    public int mode;

    // 当前计算目标均温
    public float target ;

    // 目标温度补偿值
    public float compensation ;

    // 给定与反馈值得允许误差
    public float permissible_error;


    // 判断阀门可用的超时时间(秒)
    public int timeout ;

    // 供温限定
    public Temps temps ;

    // 回温限定
    public Tempr tempr;

    // 阀门限定
    public Valve valve;

    // 泵限定
    public Pump pump ;

    // 阀门步幅设定
    public List<Sectionvalve> sectionvalve ;


    // 泵步幅设定
    public List<Sectionpump> sectionpump ;


    // 机组信息
    public List<Unit> units ;
}

