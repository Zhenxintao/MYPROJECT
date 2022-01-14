package com.bmts.heating.commons.entiy.baseInfo.response;

import com.bmts.heating.commons.entiy.baseInfo.request.PointUnitDetailDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
@ApiModel("页面选择点位配置响应类")
public class PointPageConfigResponse {
    @ApiModelProperty("Id")
    private Integer id;
    @ApiModelProperty("大类单位名称")
    private String pointUnitName;
    @ApiModelProperty("大类单位标识名称")
    private String pointUnitValue;
    @ApiModelProperty("大类单位外键")
    private Integer pointUnitId;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("配置标准点信息")
    List<PointUnitDetailResponse> pointUnitDetailResponseList;
}
