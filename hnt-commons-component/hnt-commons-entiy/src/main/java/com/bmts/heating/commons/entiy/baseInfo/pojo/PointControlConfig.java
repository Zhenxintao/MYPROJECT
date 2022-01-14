package com.bmts.heating.commons.entiy.baseInfo.pojo;

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
@ApiModel("控制量实体")
public class PointControlConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据长度 1.单字节整数 2.双字节整数 3.有符号整数 4.带符号整形 5.长整型 6.浮点数
     */
//    @ApiModelProperty("数据长度 1.单字节整数 2.双字节整数 3.有符号整数 4.带符号整形 5.长整型 6.浮点数")
//    private Integer dataLengthType;

    /**
     * 高限
     */
    @ApiModelProperty("高限")
    private Integer upperBound;

    /**
     * 低限
     */
    @ApiModelProperty("低限")
    private Integer lowerBound;

    /**
     * 修正值
     */
    @ApiModelProperty("修正值")
    private Integer correction;

    /**
     * 数据类型
     */
    @ApiModelProperty("数据类型")
    private Integer dataType;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String address;

    /**
     * 下行清洗策略
     */
    @ApiModelProperty("下行清洗策略")
    private String washDArray;

    /**
     * 扩展字段
     */
    @ApiModelProperty("扩展字段")
    private String expandDesc;
    /**
     * 关联参量id
     */
    @ApiModelProperty("关联参量id")
    private Integer pointStandardId;

    /**
     * 系统分区/机组 id
     */
    @ApiModelProperty("系统分区/机组 id")
    private Integer heatSystemId;

    /**
     * 设备Id 从字典表里取
     */
    @ApiModelProperty("设备Id 从字典表里取")
    private Integer deviceConfigId;

    /**
     * 注释
     */
    @ApiModelProperty("注释")
    private String comment;

    /**
     * 分类标志
     */
    @ApiModelProperty("分类标志")
    private String sortFlag;

    /**
     * 开始字节
     */
    @ApiModelProperty("开始字节")
    private Integer startByte;

//    /**
//     * 显示顺序
//     */
//    @ApiModelProperty("显示顺序")
//    @TableField("showSort")
//    private Integer showSort;

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
    private Boolean deleteFlag;

    private Integer id;

    @ApiModelProperty("用户id")
    private Integer userId;

}
