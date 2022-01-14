package com.bmts.heating.commons.entiy.second.request.device;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author naming
 * @since 2021-01-29
 */
@Data
@ApiModel("点数据配置信息")
public class PointConfigSecondDto {

    @ApiModelProperty("点类型 1.真实点 2.虚拟点  3.配置点")
    private Integer pointType;

    @ApiModelProperty("string double bool int")
    private String dataType;

    @ApiModelProperty("设备id")
    private Integer deviceId;

    @ApiModelProperty("等级")
    private Integer pointLevel;

    @ApiModelProperty("点名称")
    private String pointName;

    @ApiModelProperty("注释")
    private String memo;

    @ApiModelProperty("设备类型")
    private Integer eType;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备标识")
    private String code;
}
