package com.bmts.heating.commons.basement.model.db.response;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("标准点表响应类")
public class PointStandardResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 参量名称
     */
    @ApiModelProperty("参量名称")
    private String name;

    @ApiModelProperty("参量分类名称")
    private String pointName;

    /**
     * 类型 1.AI模拟量 2.DI 数字量 3.TX 日期时间
     */
    @ApiModelProperty("1.AI模拟量 2.DI 数字量 3.TX 日期时间 id")
    private Integer type;

    @ApiModelProperty("1.AI模拟量 2.DI 数字量 3.TX 日期时间 名称")
    private String pointTypeName;
    /**
     * 1.只读 2.可读可写 3.只写
     */
    @ApiModelProperty("1.只读 2.可读可写 3.只写")
    private Integer pointConfig;

    /**
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    private String columnName;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private String unit;
    @ApiModelProperty("单位显示")
    private String unitDisable;
    /**
     * 网测类型 0公用 1.一次测 2.二次测
     */
    @ApiModelProperty("网测类型 0.公用 1.一次测 2.二次测")
    private Integer netFlag;

    /**
     * 是否参与运算
     */
    @ApiModelProperty("是否参与运算")
    private Boolean isComputePoint;

    /**
     * 是否显示
     */
    @ApiModelProperty("是否显示")
    private Boolean isShow;

    /**
     * 是否是主要参量
     */
    @ApiModelProperty("是否是主要参量")
    private Boolean isImportantPoint;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("所属参量类型")
    private int pointParameterTypeId;

    @ApiModelProperty("关联单位id")
    private int pointUnitId;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
    @ApiModelProperty("描述")
    private String description;

    /**
     * 删除人
     */
    @ApiModelProperty("删除人")
    private String deleteUser;

    /**
     * 删除时间
     */
    @ApiModelProperty("删除时间")
    private LocalDateTime deleteTime;
    /**
     * 删除标识
     */
    @ApiModelProperty("删除标识")
    @JSONField(serialize = false)
    private Boolean deleteFlag;

    private Integer id;

    @ApiModelProperty("fix值类型，字典表取值")
    private int fixValueType;


    @ApiModelProperty("fix值类型名称")
    private String fixValueTypeName;

    /**
     * 数据类型： 1-Bool 2-Int 3-Uint 4-Long 5-ULong 6-Float 7-Double
     */
    @ApiModelProperty("数据类型： 1-Bool 2-Int 3-Uint 4-Long 5-ULong 6-Float 7-Double")
    private Integer dataType;


    /**
     * 描述性信息 例如 0.表示什么 1.表示什么
     */
    @ApiModelProperty("描述性信息 例如 0.表示什么 1.表示什么")
    private String descriptionJson;


    @ApiModelProperty("层级：1：热力站   2：热源")
    private Integer level;

    @ApiModelProperty("是否同步到td库标识  默认false")
    private boolean tdColumn;

}
