package com.bmts.heating.commons.entiy.baseInfo.request.station;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName NetFlagControl
 * @Author naming
 * @Date 2020/11/21 17:45
 **/
@Data
@ApiModel("控制网测类型build")
public class NetFlagControl {
    @ApiModelProperty("公用")
    private List<BuildControlDto> common;
    @ApiModelProperty("一次测")
    private List<BuildControlDto> first;
    @ApiModelProperty("二次测")
    private List<BuildControlDto> two;
}
