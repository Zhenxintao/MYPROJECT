package com.bmts.heating.commons.entiy.baseInfo.request.copy;

import com.bmts.heating.commons.entiy.baseInfo.pojo.HeatSource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName CopyHeatSourceDto
 * @Author pxf
 * @Date 2020/11/29 13:42
 **/
@Data
@ApiModel(value = "复制到新建热源实体")
public class CopyHeatSourceDto extends CopyDto {

    @ApiModelProperty(value = "新建热源实体")
    private HeatSource heatSource;

}
