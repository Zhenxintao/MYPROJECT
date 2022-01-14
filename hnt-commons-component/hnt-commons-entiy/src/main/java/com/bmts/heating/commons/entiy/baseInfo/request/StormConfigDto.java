package com.bmts.heating.commons.entiy.baseInfo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/3/31 17:25
 **/
@Data
@ApiModel("拓扑图配置信息")
public class StormConfigDto {
    /**
     * 拓扑名称
     */
    @ApiModelProperty("拓扑名称")
    private String name;

    /**
     * 数据源配置信息
     */
    @ApiModelProperty("数据源配置信息")
    private String dataSourceConfig;

    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用")
    private Boolean status;

    /**
     * 描述信息
     */
    @ApiModelProperty("描述信息")
    private String description;

    private List<kjkl> childs;
    @Data
    public static class kjkl
    {

    }
}
