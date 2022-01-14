package com.bmts.heating.commons.entiy.baseInfo.request.copy;

import com.bmts.heating.commons.entiy.baseInfo.pojo.HeatTransferStation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("复制站 换热站实体")
public class HeatStation extends HeatTransferStation {
    @ApiModelProperty("换热站名称")
    private String heatTransferStationName;
}
