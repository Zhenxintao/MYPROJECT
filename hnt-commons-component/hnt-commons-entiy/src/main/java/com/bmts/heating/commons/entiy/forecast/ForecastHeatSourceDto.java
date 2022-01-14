package com.bmts.heating.commons.entiy.forecast;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("负荷预测热源选择实体类")
public class ForecastHeatSourceDto {
    private String heatSourceName;
    private Integer heatSourceId;
}
