package com.bmts.heating.commons.entiy.gathersearch.response.issue;

import com.bmts.heating.commons.entiy.gathersearch.response.issue.read.ReadXPModeDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("二次循环泵响应实体类")
public class XPModeResponse {


    @ApiModelProperty("读取参数")
    private ReadXPModeDto read;


}
