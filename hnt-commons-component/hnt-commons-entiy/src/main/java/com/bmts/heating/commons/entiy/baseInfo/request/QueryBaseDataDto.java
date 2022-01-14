package com.bmts.heating.commons.entiy.baseInfo.request;

import lombok.Data;

import java.util.List;

@Data
public class QueryBaseDataDto {
    /**
     * 系统id集合
     * */
    private List<Integer> systemIds;
    /**
     * 系统号（-1为全部系统信息，0为0系统信息，1为非0外所有系统信息）
     * */
    private Integer systemNumber;
}
