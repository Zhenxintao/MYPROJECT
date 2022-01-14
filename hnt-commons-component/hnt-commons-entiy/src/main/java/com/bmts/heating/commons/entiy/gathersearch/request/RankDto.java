package com.bmts.heating.commons.entiy.gathersearch.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/1/10 17:19
 **/
@ApiModel(value = "排行模块入参")
@Data
public class RankDto {
    @ApiModelProperty("是否升序")
    private boolean isAsc;
    @ApiModelProperty("数据大小")
    private int count;
    @ApiModelProperty("点查询条件")
    private List<RankSection> points;
    @ApiModelProperty("分公司Id")
    private int orgId;
}
