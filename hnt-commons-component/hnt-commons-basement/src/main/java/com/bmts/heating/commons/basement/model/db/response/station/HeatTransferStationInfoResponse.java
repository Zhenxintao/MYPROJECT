package com.bmts.heating.commons.basement.model.db.response.station;

import com.bmts.heating.commons.basement.model.db.entity.HeatSource;
import com.bmts.heating.commons.basement.model.db.entity.HeatTransferStation;
import com.bmts.heating.commons.basement.model.db.response.HeatCabinetResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("热源-站-控制柜等详细信息")
@Data
public class HeatTransferStationInfoResponse {

    @ApiModelProperty("热源实体类")
    private HeatSource heatSourceEntity;


    @ApiModelProperty("换热站实体类")
    private HeatTransferStation heatStationEntity;


    @ApiModelProperty("控制柜实体类")
    private List<HeatCabinetResponse> heatCabinetList;

}
