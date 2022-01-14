package com.bmts.heating.commons.entiy.baseInfo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HeatNetSourceResponse {
    private Integer id;
    /**
     * 热网Id
     */
    @ApiModelProperty("热网Id")
    private Integer  heatNetId;

    /**
     * 热源Id
     */
    @ApiModelProperty("热源Id")
    private Integer  heatSourceId;
    /**
     * 热网Id
     */
    @ApiModelProperty("热网名称")
    private String  heatNetName;

    /**
     * 热源Id
     */
    @ApiModelProperty("热源名称")
    private String  heatSourceName;

    /**
     * 网源关系类型
     */
    @ApiModelProperty("1:网对源  2：源对网")
    private Integer  matchupType;
}
