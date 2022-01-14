package com.bmts.heating.commons.entiy.converge;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("热力站实时数据响应体")
@Data
public class HeatStationRealResponseDto {

    @ApiModelProperty("热力站Id")
    private Integer stationId;

    @ApiModelProperty("机组编号")
    private Integer number;

    @ApiModelProperty("机组名称")
    private String systemName;

    @ApiModelProperty("点参量数据")
    private List<PointReal> points;

    @Data
    public static class PointReal {
        @ApiModelProperty("点名称")
        private String pointName;
        @ApiModelProperty("点的值")
        private String pointValue;
        @ApiModelProperty("时间戳")
        private Long timeStamp;

    }

}
