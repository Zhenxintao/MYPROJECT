package com.bmts.heating.commons.entiy.gathersearch.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("系统配置查询实体类")
public class SystemConfigDto {
    @ApiModelProperty("配置key唯一，可为页面标识，可为某种业务标识")
    private List<String> configKey;
    @ApiModelProperty("可空用户id")
    private Integer userId;
}
