package com.bmts.heating.commons.entiy.second.request.point;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author naming
 * @description 设备信息
 * @date 2021/1/8 9:25
 **/
@Data
@ApiModel("长输的采集点信息")
public class TransportPoint {

    @ApiModelProperty("点名称")
    private String pointName;
    @ApiModelProperty("点的数据类型")
    private String dataType;
    @ApiModelProperty("点地址")
    private String pointAddress;
    @ApiModelProperty("设备id")
    private Integer deviceId;
    @ApiModelProperty("值")
    private String value;
    @ApiModelProperty("时间")
    private String timeStamp;
    @ApiModelProperty("中文名")
    private String memo;
    @ApiModelProperty("单位")
    private String unit;


}
