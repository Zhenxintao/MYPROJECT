package com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 基础历史数据--返回数据响应实体
 * */
@Data
public class HistoryBaseResponse {
    @ApiModelProperty("查询Id标识(系统id或站id)")
    private Integer relevanceId;
    @ApiModelProperty("时间戳")
    private long timeStrap;
    @ApiModelProperty("历史点位数据信息")
    private List<PointsInfo> pointsInfo;

    @ApiModelProperty("热源或者热力站名称")

    private String name;
    @ApiModelProperty("系统名称")
    private String systemName;
}
