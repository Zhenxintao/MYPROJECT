package com.bmts.heating.commons.entiy.gathersearch.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("历史曲线入参")
public class SingleStationRepoDto extends PageDto {

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("1.分钟 2.小时整点 3.天 4.小时平均 后续根据需要扩充")
    private int queryType;

    @ApiModelProperty("表头")
    private List<String> fields;

    @ApiModelProperty("换热站Id")
    private Integer stationId;

    @ApiModelProperty("1：一次网数据， 2：二次网数据，3：室温数据")
    private int sourceType;//一次、二次、室温

}
