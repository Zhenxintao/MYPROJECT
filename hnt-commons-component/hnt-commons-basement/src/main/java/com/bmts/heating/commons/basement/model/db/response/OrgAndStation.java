package com.bmts.heating.commons.basement.model.db.response;

import com.bmts.heating.commons.basement.model.db.entity.HeatOrganization;
import lombok.Data;

@Data
public class OrgAndStation extends HeatOrganization {
    private int stationId;
    private String stationName;
}
