package com.bmts.heating.commons.entiy.baseInfo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author pxf
 * @description 设备启停记录
 * @date 2021/1/8 9:25
 **/
@Data
@ApiModel("设备启停记录曲线")
public class RecordDeviceUpDownCurveList {

    @ApiModelProperty("开启设备的曲线数据")
    private List<RecordDeviceUpDownCurve> listTrue;

    @ApiModelProperty("停止设备的曲线数据")
    private List<RecordDeviceUpDownCurve> listFalse;


}
