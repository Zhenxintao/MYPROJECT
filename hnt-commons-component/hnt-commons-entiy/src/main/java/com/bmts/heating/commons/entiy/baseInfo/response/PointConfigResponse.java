package com.bmts.heating.commons.entiy.baseInfo.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.Data;

@Data
@ApiModel("点配置实体")
public class PointConfigResponse {

    @ApiModelProperty("列名")
    private  String columnName;

    @ApiModelProperty("参量名称")
    private String name;

}
