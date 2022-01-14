package com.bmts.heating.commons.entiy.baseInfo.request;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("系统参数")
@Data
public class HeatSystemDto extends BaseDto {

    @ApiModelProperty("控制柜id")
    private int heatCabinetId;
}
