package com.bmts.heating.commons.entiy.gathersearch.request.tdengine;

/**
 * TD历史查询-聚合时间方式枚举
 * */
public enum AggregateTimeTypeEnum {
    //按小时聚合
    hour,
    //按整天聚合
    day,
    //按开始、结束时间区间整体聚合
    interval
}
