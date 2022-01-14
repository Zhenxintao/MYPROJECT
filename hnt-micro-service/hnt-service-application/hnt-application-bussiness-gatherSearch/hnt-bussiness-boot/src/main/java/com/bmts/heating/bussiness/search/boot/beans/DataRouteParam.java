package com.bmts.heating.bussiness.search.boot.beans;

import com.bmts.heating.commons.basement.model.ldap.pojo.vo.PointConstructionVo;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author fei.chang
 * @Date 2020/9/10 15:38
 * @Version 1.0
 */
@Data
public class DataRouteParam extends PointConstructionVo {

    /**
     * 点集合
     */
    private List<String> pointIds;

    /**
     * 区间开始时间
     */
    private Long startTime;

    /**
     * 区间结束时间
     */
    private Long endTime;

    /**
     * 起始页数
     */
    private int start = 1;

    /**
     * 每页记录数
     */
    private int limit = 100;

}
