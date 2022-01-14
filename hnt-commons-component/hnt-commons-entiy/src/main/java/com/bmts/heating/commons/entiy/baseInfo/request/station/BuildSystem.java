package com.bmts.heating.commons.entiy.baseInfo.request.station;

import com.bmts.heating.commons.entiy.baseInfo.pojo.HeatSystem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName BuildSystem
 * @Author naming
 * @Date 2020/11/21 16:27
 **/
@Data
@ApiModel("配置系统参数")
public class BuildSystem extends HeatSystem {
    @ApiModelProperty("采集量分类netFlagCollect")
    private NetFlagCollect netFlagCollect;
    @ApiModelProperty("控制量集合netFlagControl")
    private NetFlagControl netFlagControl;
}
