package com.bmts.heating.commons.basement.model.db.response;

import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import lombok.Data;

@Data
public class HeatNetSourceTree extends StationFirstNetBaseView {
	private String heatNetName;
}
