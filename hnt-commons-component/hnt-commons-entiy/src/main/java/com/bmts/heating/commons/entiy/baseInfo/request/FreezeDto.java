package com.bmts.heating.commons.entiy.baseInfo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("热力站冻结或解冻类")
@Data
public class FreezeDto {

    @ApiModelProperty("Id")
    private Integer id;

    @ApiModelProperty("状态:true 解冻，false 冻结")
    private Boolean status;


    @ApiModelProperty("操作人")
    private String name;
}
