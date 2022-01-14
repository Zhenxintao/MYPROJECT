package com.bmts.heating.commons.entiy.baseInfo.request.pointConfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("点位类型查询实体类")
public class PointTypeSearchDto {

    @ApiModelProperty("是否查询全部，true为全部信息，false非全部信息")
    private Boolean isAllInfo;

    @ApiModelProperty("点位标签数组")
    private List<String> typeArrayIds;

    @ApiModelProperty("层级：1：热力站   2：热源")
    private Integer level;
}
