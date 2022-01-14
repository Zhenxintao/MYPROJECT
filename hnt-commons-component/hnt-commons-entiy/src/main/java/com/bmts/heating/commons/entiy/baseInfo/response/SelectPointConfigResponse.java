package com.bmts.heating.commons.entiy.baseInfo.response;

import com.bmts.heating.commons.entiy.baseInfo.request.PointPageConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("页面选择点位配置相应类")
public class SelectPointConfigResponse {
    @ApiModelProperty("页面key")
    private String pageKey;
    @ApiModelProperty("页面配置点信息")
    List<PointPageConfigResponse> pointPageConfigResponses;
}
