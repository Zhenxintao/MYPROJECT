package com.bmts.heating.commons.entiy.baseInfo.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServiceComplaintResponse {
   /**
    * 投诉率
    * */
    private BigDecimal rate;
    /**
     * 换热站名称
     * */
    private String name;
    /**
     * 换热站Id
     * */
    private String id;
}
