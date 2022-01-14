package com.bmts.heating.commons.entiy.baseInfo.request.pointConfig;

import com.bmts.heating.commons.entiy.baseInfo.request.PointPageConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
@ApiModel("页面选择点位添加实体类")
public class SelectPointConfigDto {
    @ApiModelProperty("页面key")
    private String pageKey;
    @ApiModelProperty("页面配置点信息")
    List<PointPageConfig> pointPageConfigs;
}
