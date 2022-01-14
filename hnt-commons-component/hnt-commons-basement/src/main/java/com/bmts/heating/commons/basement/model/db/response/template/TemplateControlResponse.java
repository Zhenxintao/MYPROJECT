package com.bmts.heating.commons.basement.model.db.response.template;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("控制量模板返回结果集")
public class TemplateControlResponse {
    private Integer id;

    /**
     * 数据长度 1.单字节整数 2.双字节整数 3.有符号整数 4.带符号整形 5.长整型 6.浮点数
     */
//	@ApiModelProperty("数据长度 1.单字节整数 2.双字节整数 3.有符号整数 4.带符号整形 5.长整型 6.浮点数")
//	@TableField("dataLengthType")
//	private Integer dataLengthType;

    /**
     * 高限
     */
    @ApiModelProperty("高限")
    @TableField("upperBound")
    private Integer upperBound;

    /**
     * 低限
     */
    @ApiModelProperty("低限")
    @TableField("lowerBound")
    private Integer lowerBound;

    /**
     * 修正值
     */
    @ApiModelProperty("修正值")
    @TableField("correction")
    private Integer correction;

    /**
     * 数据类型
     */
    @ApiModelProperty("数据类型")
    @TableField("dataType")
    private Integer dataType;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    @TableField("address")
    private String address;

    /**
     * 下行清洗策略
     */
    @ApiModelProperty("下行清洗策略")
    @TableField("washDArray")
    private String washDArray;

    /**
     * 关联参量id
     */
    @ApiModelProperty("关联参量id")
    @TableField("pointStandardId")
    private Integer pointStandardId;

    /**
     * 扩展字段
     */
    @ApiModelProperty("扩展字段")
    @TableField("expandDesc")
    private String expandDesc;

    /**
     * 关联模板Id
     */
    @ApiModelProperty("关联模板Id")
    @TableField("pointTemplateConfigId")
    private Integer pointTemplateConfigId;

    /**
     * 注释
     */
    @ApiModelProperty("注释")
    private String comment;

    /**
     * 分类标志
     */
    @ApiModelProperty("分类标志")
    @TableField("sortFlag")
    private String sortFlag;

    /**
     * 开始字节
     */
    @ApiModelProperty("开始字节")
    @TableField("startByte")
    private Integer startByte;

    /**
     * 设备Id
     */
    @ApiModelProperty("设备Id")
    @TableField("deviceConfigId")
    private Integer deviceConfigId;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    @TableField("createUser")
    private String createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField("createTime")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    @TableField("updateUser")
    private String updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @TableField("updateTime")
    private LocalDateTime updateTime;

    @TableField("description")
    private String description;

    /**
     * 删除标识
     */
    @ApiModelProperty("删除标识")
    @TableField("deleteFlag")
    private Boolean deleteFlag;

    /**
     * 删除时间
     */
    @ApiModelProperty("删除时间")
    @TableField("deleteTime")
    private LocalDateTime deleteTime;

    /**
     * 删除人
     */
    @ApiModelProperty("删除人")
    @TableField("deleteUser")
    private String deleteUser;

    @ApiModelProperty("参量名称")
    private String pointStandardName;

    @ApiModelProperty("标签名称")
    private String columnName;

    @ApiModelProperty("网测类型：0. 公用 1.一次侧 2.二测测")
    private int netFlag;

    @ApiModelProperty("参量类型")
    private String pointParameterTypeName;

     @ApiModelProperty("类型(采集量、控制量)")
    private String pointConfigName;

     @ApiModelProperty("类型(Ai、AO...)")
    private String typeName;

    @ApiModelProperty("单位中文名称")
    private String unitName;

    @ApiModelProperty("单位")
    private String unitValue;


}
