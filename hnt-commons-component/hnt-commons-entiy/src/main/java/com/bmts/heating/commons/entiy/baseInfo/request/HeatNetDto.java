package com.bmts.heating.commons.entiy.baseInfo.request;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("热网参数")
@Data
public class HeatNetDto extends BaseDto {
    @ApiModelProperty("类型  0.全部  1.环网 2.分网")
    private int type;
}
