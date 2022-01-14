package com.bmts.heating.commons.entiy.baseInfo.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("热源实时数据返回实体")
public class HeatSourceRealDataResponse {

    @ApiModelProperty("热源编号")
    private Integer sourceId;
    @ApiModelProperty("热源名")
    private String sourceName;
    @ApiModelProperty("机组编号")
    private Integer number;
    @ApiModelProperty("机组名称")
    private String systemName;
    @ApiModelProperty("点数据集合")
    private List<HeatSourcePointRealData> points;

}




