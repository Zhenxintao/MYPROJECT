package com.bmts.heating.commons.entiy.baseInfo.request.station;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName NetFlag
 * @Author naming
 * @Date 2020/11/21 17:29
 **/
@Data
@ApiModel("采集网测类型build")
public class NetFlagCollect {
    @ApiModelProperty("公用")
    private List<BuildCollectPoint> common;
    @ApiModelProperty("一次测")
    private List<BuildCollectPoint> first;
    @ApiModelProperty("二次测")
    private List<BuildCollectPoint> two;
}































