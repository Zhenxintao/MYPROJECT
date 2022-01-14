package com.bmts.heating.commons.entiy.baseInfo.request;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("字典查询类")
@Data
public class DicDto extends BaseDto {
    @ApiModelProperty("所属分类")
    private int pid;
}
