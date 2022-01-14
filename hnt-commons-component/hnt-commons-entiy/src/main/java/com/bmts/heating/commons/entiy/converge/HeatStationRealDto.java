package com.bmts.heating.commons.entiy.converge;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("热力站实时数据实体")
@Data
public class HeatStationRealDto {


    @ApiModelProperty("热力站Id")
    private List<Integer> stationIds;

    @ApiModelProperty("关联参量")
    private String[] pointNames;


}
