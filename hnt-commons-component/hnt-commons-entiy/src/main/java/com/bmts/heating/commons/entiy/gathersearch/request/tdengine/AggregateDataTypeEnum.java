package com.bmts.heating.commons.entiy.gathersearch.request.tdengine;

/**
 * TD历史查询-聚合返回数据类型枚举
 * */
public enum AggregateDataTypeEnum {
    //查询Id最终返回一条整体聚合数据
    single,
    //返回查询各Id聚合数据
    whole
}
