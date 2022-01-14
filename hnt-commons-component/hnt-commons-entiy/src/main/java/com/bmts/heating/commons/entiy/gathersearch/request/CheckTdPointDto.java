package com.bmts.heating.commons.entiy.gathersearch.request;

import lombok.Data;

import java.util.List;

@Data
public class CheckTdPointDto {
    /**
     * 点位所属类型（1.换热站，2.热源）
     * */
    private Integer type;
    /**
     *点位名称集合
     */
    private List<String> points;
}
