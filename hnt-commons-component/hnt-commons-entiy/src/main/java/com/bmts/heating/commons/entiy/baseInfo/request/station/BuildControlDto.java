package com.bmts.heating.commons.entiy.baseInfo.request.station;

import com.bmts.heating.commons.entiy.baseInfo.pojo.PointControlConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName BuildControlDto
 * @Author naming
 * @Date 2020/11/21 16:28
 **/
@Data
@ApiModel("控制量參數")
public class BuildControlDto extends PointControlConfig {
    @ApiModelProperty("点的属性jsonproperys")
    private String properties;
}
