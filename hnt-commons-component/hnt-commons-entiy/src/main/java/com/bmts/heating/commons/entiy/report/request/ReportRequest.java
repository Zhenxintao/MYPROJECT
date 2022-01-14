package com.bmts.heating.commons.entiy.report.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: ReportRequest
 * @Description: 报表请求实体
 * @Author: pxf
 * @Date: 2021/9/10 15:42
 * @Version: 1.0
 */
@Data
@ApiModel("报表请求实体类")
public class ReportRequest {
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;
    @ApiModelProperty("用户id")
    private Integer centerId;

}
