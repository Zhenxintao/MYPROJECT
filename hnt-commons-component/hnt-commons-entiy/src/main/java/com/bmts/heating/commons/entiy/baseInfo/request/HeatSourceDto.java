package com.bmts.heating.commons.entiy.baseInfo.request;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("热源参数")
@Data
public class HeatSourceDto extends BaseDto {

    @ApiModelProperty("类型  1.热水  2.燃气 3.电厂 4.热泵")
    private int type;
}
