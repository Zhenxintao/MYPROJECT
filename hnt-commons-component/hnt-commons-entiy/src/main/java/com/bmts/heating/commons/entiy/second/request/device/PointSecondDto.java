package com.bmts.heating.commons.entiy.second.request.device;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author pxf
 * @description 二网点信息实体
 * @date 2021/1/8 9:25
 **/
@Data
@ApiModel("二网点信息实体")
public class PointSecondDto {

    @ApiModelProperty("13位时间戳")
    private long timestamp;

    @ApiModelProperty("设备code")
    private String deviceCode;

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("层级")
    private String level;

    @ApiModelProperty("点信息")
    private Map<String, Map<String, String>> points;

}
