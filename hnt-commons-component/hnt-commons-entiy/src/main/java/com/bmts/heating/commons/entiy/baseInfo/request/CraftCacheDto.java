package com.bmts.heating.commons.entiy.baseInfo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/3/24 14:38
 **/
@Data
@ApiModel("工艺图实时数据")
public class CraftCacheDto {
    @ApiModelProperty("点id集合")
    private List<Integer> pointIds;
    @ApiModelProperty("点名称集合")
    private List<String> ColumnNames;
    @ApiModelProperty("站/源 id")
    private Integer relevanceId;
    @ApiModelProperty("查询类型 1热力站 2热源")
    private Integer type;
    @ApiModelProperty("层级：1属于系统 2.控制柜 3.站 4.源 5.网")
    private Integer level;
}
