package com.bmts.heating.commons.entiy.report.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: ReportResponse
 * @Description: 报表数据返回类
 * @Author: pxf
 * @Date: 2021/9/10 13:50
 * @Version: 1.0
 */

@Data
@ApiModel("报表数据返回类")
public class HeaderDto {

    @ApiModelProperty("热源名称")
    private String sourceName = "热源名称";

    @ApiModelProperty("供暖季")
    private String heatSeason = "供暖季";

}
