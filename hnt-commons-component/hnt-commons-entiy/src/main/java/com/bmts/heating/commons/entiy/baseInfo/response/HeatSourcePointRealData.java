package com.bmts.heating.commons.entiy.baseInfo.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("热源实时数据点实体")
public class HeatSourcePointRealData {

    @ApiModelProperty("点代码")
    private  String pointName;
    @ApiModelProperty("数值")
    private  String pointValue;
    @ApiModelProperty("时间戳")
    private Long timeStamp;
    @ApiModelProperty("描述")
    private  String description;

}
