package com.bmts.heating.commons.entiy.baseInfo.request;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author naming
 * @description
 * @date 2021/4/27 15:28
 **/
@Data
@ApiModel("工艺图模板查询")
public class CraftTemplateQueryDto extends BaseDto {
    @ApiModelProperty("1.热力站 2.热源")
    private Integer type;
}
