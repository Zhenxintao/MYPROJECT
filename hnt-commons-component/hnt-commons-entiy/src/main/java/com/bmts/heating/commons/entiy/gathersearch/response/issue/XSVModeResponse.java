package com.bmts.heating.commons.entiy.gathersearch.response.issue;

import com.bmts.heating.commons.entiy.gathersearch.response.issue.read.ReadXSVModeDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("泄压阀响应实体类")
public class XSVModeResponse {

    @ApiModelProperty("读取参数")
    private ReadXSVModeDto read;


}
