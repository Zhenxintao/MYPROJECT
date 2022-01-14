package com.bmts.heating.commons.entiy.gathersearch.response.issue;

import com.bmts.heating.commons.entiy.gathersearch.response.issue.read.ReadBPModeDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("二次补水变频泵响应实体类")
public class BPModeResponse {

    @ApiModelProperty("读取参数")
    private ReadBPModeDto read;


}
