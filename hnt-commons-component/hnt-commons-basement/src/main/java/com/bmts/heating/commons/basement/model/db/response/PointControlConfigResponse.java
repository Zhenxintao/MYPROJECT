package com.bmts.heating.commons.basement.model.db.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @author jingwei
 * @since 2020-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("控制量列表返回实体")
public class PointControlConfigResponse implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private String address;

    /**
     * 下行清洗策略
     */
    @ApiModelProperty("下行清洗策略")
    private String washDArray;

    /**
     * 扩展字段
     */
    @TableField("expandDesc")
    private String expandDesc;
    /**
     * 关联参量id
     */
    @ApiModelProperty("关联参量id")
    @TableField("pointStandardId")
    private Integer pointStandardId;

    /**
     * 系统分区/机组 id
     */
    @ApiModelProperty("系统分区/机组 id")
    @TableField("heatSystemId")
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

    @ApiModelProperty("描述")
    private String description;

    /**
     * 删除人
     */
    @ApiModelProperty("删除人")
    @TableField("deleteUser")
    private String deleteUser;

    /**
     * 删除时间
     */
    @ApiModelProperty("删除时间")
    @TableField("deleteTime")
    private LocalDateTime deleteTime;
    /**
     * 删除标识
     */
    @ApiModelProperty("删除标识")
    @TableField("deleteFlag")
    private Boolean deleteFlag;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("系统分区/机组 名称")
    private String heatSystemName;

    @ApiModelProperty("换热站id")
    private Integer heatTransferStationId;

    @ApiModelProperty("换热站 名称")
    private String heatTransferStationName;

    @ApiModelProperty("热源id")
    private Integer heatSourceId;

    @ApiModelProperty("热源 名称")
    private String heatSourceName;

    @ApiModelProperty("热网id")
    private Integer heatNetId;

    @ApiModelProperty("热网名称")
    private String heatNetName;

    @ApiModelProperty("控制柜id")
    private Integer heatCabinetId;

    @ApiModelProperty("控制柜名称")
    private String heatCabinetName;

    @ApiModelProperty("关联参量")
    private String pointStandardName;

    @ApiModelProperty("标签名称")
    private String columnName;

    @ApiModelProperty("单位Id")
    private String pointUnitId;

    @ApiModelProperty("单位名称")
    private String unitValue;

    @ApiModelProperty("网侧类型")
    private String netFlag;
}
