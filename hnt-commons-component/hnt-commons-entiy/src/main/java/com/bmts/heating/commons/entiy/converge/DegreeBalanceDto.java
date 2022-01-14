package com.bmts.heating.commons.entiy.converge;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
@ApiModel("小区平衡度数据实体")
public class DegreeBalanceDto {

    @ApiModelProperty("此小区id要与客服系统一致")
    private String buildId;
    @ApiModelProperty("平衡度")
    private BigDecimal value;

}
