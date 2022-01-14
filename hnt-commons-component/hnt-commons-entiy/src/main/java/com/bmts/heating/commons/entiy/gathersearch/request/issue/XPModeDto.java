package com.bmts.heating.commons.entiy.gathersearch.request.issue;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("二次循环泵实体类")
public class XPModeDto extends BaseUser {

    @ApiModelProperty("系统id")
    private Integer systemId;

    @ApiModelProperty("0:定值  1:恒定二次供回压差  2:恒定二次供压")
    private Integer options;

    @ApiModelProperty("频率设定值")
    private Double xpMSP;

    @ApiModelProperty("二次供回压差设定值")
    private Double xpPdSP;

    @ApiModelProperty("二次供压设定值")
    private Double xpPgSP;

    @ApiModelProperty("循环启动令")
    private Integer start;

    @ApiModelProperty("循环停止令")
    private Integer stop;


}
