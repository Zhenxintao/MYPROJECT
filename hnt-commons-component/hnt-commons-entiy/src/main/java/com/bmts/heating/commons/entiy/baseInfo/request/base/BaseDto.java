package com.bmts.heating.commons.entiy.baseInfo.request.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("传入参数")
@Data
public class BaseDto {
    @ApiModelProperty("关键字")
    private String keyWord;
    @ApiModelProperty("当前页")
    private int currentPage = 1;
    @ApiModelProperty("页大小")
    private int pageCount = 20;
    @ApiModelProperty("排序名称")
    private String sortName;
    @ApiModelProperty("是否为升序")
    private boolean sortAsc = true;
    @ApiModelProperty("操作人")
    private int userId;
}
