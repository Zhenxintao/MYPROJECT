package com.bmts.heating.commons.entiy.baseInfo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PointUnitDetailResponse {

    @ApiModelProperty("字典名称")
    private String pointTypeName;
    @ApiModelProperty("分类名称")
    private String pointName;
    @ApiModelProperty("参量名称")
    private String name;
    /**
     * 类型 1.AI模拟量 2.DI 数字量 3.TX 日期时间
     */
    @ApiModelProperty("1.AI模拟量 2.DI 数字量 3.TX 日期时间")
    private Integer type;
    /**
     * 数据类型： 1-Bool 2-Int 3-Uint 4-Long 5-ULong 6-Float 7-Double
     */
    @ApiModelProperty("数据类型： 1-Bool 2-Int 3-Uint 4-Long 5-ULong 6-Float 7-Double")
    private Integer dataType;
    /**
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    private String columnName;
    /**
     * 网测类型 0公用 1.一次测 2.二次测
     */
    @ApiModelProperty("网测类型 0.公用 1.一次测 2.二次测")
    private Integer netFlag;
    /**
     * 是否是主要参量
     */
    @ApiModelProperty("是否是主要参量")
    private Boolean isImportantPoint;

    @ApiModelProperty("fix值类型，从字典表取")
    private int fixValueType;

    @ApiModelProperty("用户id")
    private Integer userId;
    /**
     * 1.只读 2.可读可写 3.只写
     */
    @ApiModelProperty("1.只读 2.可读可写 3.只写")
    private Integer pointConfig;
    @ApiModelProperty("配置单位外键")
    private Integer webPointSearchUnitId;
    @ApiModelProperty("标准点外键")
    private Integer pointStandardId;
    @ApiModelProperty("排序")
    private Integer sort;
}
