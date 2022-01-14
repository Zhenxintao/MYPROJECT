package com.bmts.heating.commons.entiy.balance.pojo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.List;

@Data
public class QueryScatterDiagramResponse {
     @ApiModelProperty("热网名称")
     private String netBalanceName;
     @ApiModelProperty("热网所带系统信息")
     List<BalanceMemberResponse> responses;
}
