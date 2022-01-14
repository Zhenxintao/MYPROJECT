package com.bmts.heating.commons.entiy.baseInfo.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HeatAreaChangeResponse {
   private Integer relevanceId;
   private BigDecimal areaValue;
}
