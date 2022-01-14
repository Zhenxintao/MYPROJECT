package com.bmts.heating.commons.entiy.baseInfo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author pxf
 * @description 设备启停记录
 * @date 2021/1/8 9:25
 **/
@Data
@ApiModel("设备启停记录响应类")
public class RecordDeviceUpDownResponse {

    @ApiModelProperty("id")
    private int id;

    /**
     * 热力站id
     */
    @ApiModelProperty("热力站id")
    private Integer heatTransferStationId;


    @ApiModelProperty("热力站名称")
    private String stationName;

    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    private String content;


    /**
     * 设备状态  true 开启 false停止
     */
    @ApiModelProperty("设备状态 true 开启 false停止")
    private Boolean operation;


    /**
     * 设备类型 1.循环泵 2.补水泵
     */
    @ApiModelProperty("设备类型 1.循环泵 2.补水泵")
    private Integer type;


    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
