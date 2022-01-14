package com.bmts.heating.commons.basement.model.db.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class InsertEquipmentDto {

    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    private String equipmentName;
    /**
     * 设备编号
     */
    @ApiModelProperty("设备编号")
    private String equipmentCode;

    /**
     * 设备Id
     */
    @ApiModelProperty("点位Id")
    private List<Integer> pointStandardIds;
}
