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
@ApiModel("pvss返回的点信息")
public class TransportPointTemp {

    @ApiModelProperty("点名称")
    private String tagName;
    @ApiModelProperty("时间")
    private String timeStamp;
    @ApiModelProperty("值")
    private String Value;

}
