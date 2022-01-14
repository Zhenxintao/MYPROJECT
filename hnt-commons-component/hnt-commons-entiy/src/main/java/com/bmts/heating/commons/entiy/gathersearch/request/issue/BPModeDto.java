package com.bmts.heating.commons.entiy.gathersearch.request.issue;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("二次补水变频泵实体类")
public class BPModeDto extends BaseUser {

    @ApiModelProperty("系统id")
    private Integer systemId;

    @ApiModelProperty("0:定值  1:恒定二次回压  2:高低限自动（电节点压力表方式）")
    private Integer options;

    @ApiModelProperty("频率设定值")
    private Double bpMsP;

    @ApiModelProperty("二次回压设定值")
    private Double bpPhSP;

    @ApiModelProperty("高限设定值")
    private Double bpP2hH;

    @ApiModelProperty("低限设定值")
    private Double bpP2hL;

    @ApiModelProperty("补水启动令")
    private Integer start;

    @ApiModelProperty("补水停止令")
    private Integer stop;

}
