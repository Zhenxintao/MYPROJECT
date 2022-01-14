package com.bmts.heating.commons.basement.model.db.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/4/19 10:53
 **/
@Data
@ApiModel("批量设置工艺图")
public class CraftBatch {
    @ApiModelProperty("复制源id,不传时场景为批量设置，传入场景为复制到多个目标")
    private Integer sourceId;
    @ApiModelProperty("目标ids")
    private List<Integer> targetIds;
    @ApiModelProperty("1.热力站 2.热源")
    private Integer type;
    @ApiModelProperty("工艺图内容")
    private String content;

    @ApiModelProperty("模板id")
    private Integer templateId;
    @ApiModelProperty("层级：1属于系统 2.控制柜 3.站 4.源 5.网")
    private Integer level;
}
