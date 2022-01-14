package com.bmts.heating.commons.entiy.baseInfo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/4/26 19:36
 **/
@ApiModel("工艺图批量应用")
@Data
public class CraftTemplateDto {
    @ApiModelProperty("模板id")
    private Integer templateId;
    @ApiModelProperty("1.热力站 2.热源")
    private int type;
    @ApiModelProperty("热力站/热源 id数组")
    private List<Integer> relevanceIds;

    @ApiModelProperty("层级：1属于系统 2.控制柜 3.站 4.源 5.网")
    private Integer level;
}
