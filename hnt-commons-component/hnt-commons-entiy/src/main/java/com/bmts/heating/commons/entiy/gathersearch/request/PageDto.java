package com.bmts.heating.commons.entiy.gathersearch.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class PageDto {

    @ApiModelProperty("当前页")
    private Integer currentPage;//当前页
    @ApiModelProperty("页面条数")
    private Integer size;//页面条数
    @ApiModelProperty("排序字段")
    private String field;
    @ApiModelProperty("排序方式，ASC/DESC")
    private Boolean sortType;
}
