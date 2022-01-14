package com.bmts.heating.commons.redis.utils;

/**
 * redis key 管理
 */
public class RedisKeyConst {
    public static final String BASE = "tscc:heatnet:";
    public static final String FIRST_NET = BASE.concat("firstnet:");
    public static final String SECOND_NET = BASE.concat("secondnet:");
    /**
     * 一网
     */
    public static final String BASE_MANAGER = FIRST_NET.concat("manager:");
    public static final String First_REAL_DATA = FIRST_NET.concat("realdata:level_");
    public static final String FIRST_BASE_DATA = FIRST_NET.concat(":firstnetbase");
    public static final String POINT_COLLECTCONFIG = FIRST_NET.concat("points:level_");
    public static final String POINT_RANK = FIRST_NET.concat("point_rank:level_");
    public static final String UN_ALARM_USERS = FIRST_NET.concat("unalarm");
    /**
     * 公用
     */
    public static final String DIC = BASE_MANAGER.concat("dic");


    /**
     * 二网
     */
    public static final String SECOND_REAL_DATA = SECOND_NET.concat("realdata:");

    public static final String SECOND_NET_DEVICE = SECOND_NET.concat("device:");

    /**
     * 单元阀
     */
    public static final String REAL_UNIT_VALVE = SECOND_REAL_DATA.concat("uv:");
    /**
     * 户阀
     */
    public static final String REAL_HOUSE_VALVE = SECOND_REAL_DATA.concat("hv:");
    /**
     * 室温
     */
    public static final String REAL_RT = SECOND_REAL_DATA.concat("rt:");


    /**
     * 长输 业务
     */
    public static final String DEVICE_OTHER = SECOND_NET_DEVICE.concat("other:");
    public static final String DEVICE_OTHER_SYSTEM = DEVICE_OTHER.concat("system:");


    /**
     * 小区平衡度
     */
    public  static  final  String DEGREE_BALANCE=BASE.concat("degreebalance:");


}
