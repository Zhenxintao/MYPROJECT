package com.bmts.heating.commons.basement.model.db.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServiceInfoResponse {
    /**
     * 投诉率
     */
    private BigDecimal complaintRate;
    /**
     * 收费率
     */
    private BigDecimal chargeRate;
}
