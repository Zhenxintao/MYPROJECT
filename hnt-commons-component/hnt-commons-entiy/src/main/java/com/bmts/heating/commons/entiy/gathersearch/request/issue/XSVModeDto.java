package com.bmts.heating.commons.entiy.gathersearch.request.issue;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("泄压阀实体类")
public class XSVModeDto extends BaseUser {

    @ApiModelProperty("系统id")
    private Integer systemId;

    @ApiModelProperty("0:手动 1:自动")
    private Integer options;

    @ApiModelProperty("手动启停控制(0:关闭 1开启)")
    private Integer xsvSS;

    @ApiModelProperty("自动高限设定值")
    private Integer xsvP2hH;

    @ApiModelProperty("自动低限设定值")
    private Integer xsvP2hL;


}
