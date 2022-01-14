package com.bmts.heating.commons.basement.model.db.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShowPowerResponse {
    private Integer heatSystemId;
    private String StationBranchArrayNumber;
    private String StationName;
    private String StationBranchName;
    private BigDecimal Area;
    private BigDecimal StationLongitude;
    private BigDecimal StationLatitude;
}
