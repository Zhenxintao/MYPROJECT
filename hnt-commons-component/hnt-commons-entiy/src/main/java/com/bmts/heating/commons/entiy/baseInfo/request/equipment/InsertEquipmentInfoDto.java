package com.bmts.heating.commons.entiy.baseInfo.request.equipment;

import lombok.Data;

import java.util.List;

@Data
public class InsertEquipmentInfoDto {
    private List<ColumnsDto> point;
    private List<ColumnsDto> tags;
    private String equipmentName;
}
