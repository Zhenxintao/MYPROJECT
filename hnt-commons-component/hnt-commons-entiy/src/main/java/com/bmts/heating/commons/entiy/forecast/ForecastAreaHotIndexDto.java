package com.bmts.heating.commons.entiy.forecast;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class ForecastAreaHotIndexDto {
    private  float hotEnergy;
    private float tn;
}
