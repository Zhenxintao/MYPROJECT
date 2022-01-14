package com.bmts.heating.commons.basement.model.db.response;

import lombok.Data;

@Data
public class OrgAndStationTree extends CommonTree {
	private int stationId;
	private String stationName;
	private int systemId;
	private String systemName;
	private int heatOrganizationId;
}
