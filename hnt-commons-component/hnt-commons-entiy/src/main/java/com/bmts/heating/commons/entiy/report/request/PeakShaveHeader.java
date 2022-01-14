package com.bmts.heating.commons.entiy.report.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: PeakShaveHeader
 * @Description: 调峰报表头部实体类
 * @Author: pxf
 * @Date: 2021/9/10 15:42
 * @Version: 1.0
 */
@Data
@ApiModel("调峰报表头部实体类")
public class PeakShaveHeader {

    @ApiModelProperty("热源名称")
    private String sourceName;
}
