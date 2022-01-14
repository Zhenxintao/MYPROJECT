package com.bmts.heating.commons.entiy.common;

/**
 * @author naming
 * @description
 * @date 2021/1/31 16:54
 **/
public enum TreeLevel {
    /*
    系统
     */
    HeatSystem(1),
    /*
    控制柜
     */
    HeatCabinet(2),
    /*
    换热站
     */
    HeatStation(3),
    /*
    热源
     */
    HeatSource(4),
    /*
    热网
     */
    HeatNet(5),
    /*
    系统分支
     */
    HeatSystemBranch(6);

    private final int level;

    TreeLevel(int level) {
        this.level = level;
    }

    public int level() {
        return level;
    }
}

