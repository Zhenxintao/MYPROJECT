package com.bmts.heating.commons.entiy.baseInfo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("全量点表响应实体")
public class PointStandardFullResponse {
    @ApiModelProperty("Id")
    private Integer id;
    @ApiModelProperty("点位名称")
    private String name;
    @ApiModelProperty("点位类型（类型 1.AI模拟量 2.DI 数字量 3.TX 日期时间）")
    private Integer type;
    @ApiModelProperty("标签名称")
    private String columnName;
    @ApiModelProperty("网测类型 0.公用 1.一次测 2.二次测")
    private Integer netFlag;
    @ApiModelProperty("是否是主要参量")
    private Boolean isImportantPoint;
    @ApiModelProperty("点位操作（1.只读 2.可读可写 3.只写）")
    private Integer pointConfig;
    @ApiModelProperty("分类名称")
    private String unitName;
    @ApiModelProperty("分类值单位")
    private String unitValue;
}
