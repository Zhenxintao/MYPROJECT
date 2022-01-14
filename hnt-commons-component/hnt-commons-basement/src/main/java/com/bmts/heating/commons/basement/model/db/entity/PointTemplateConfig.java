package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("pointTemplateConfig")
@ApiModel("模板管理")
public class PointTemplateConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板key 自定义匹配规则 例如：Station:PointCollect:1  以冒号分隔，以后切换到redis也方便存储
     */
    @ApiModelProperty("模板key 自定义匹配规则 例如：Station:PointCollect:1  以冒号分隔，以后切换到redis也方便存储")
    @TableField(value = "templateKey")
    private String templateKey;

    @ApiModelProperty("预留 例如源需要模板存储站也需要 则用此字段做区分 具体查哪一条用key的匹配规则,2:控制量，3:采集量")
    @TableField(value = "type")
    private Integer type;

    /**
     * 模板value json形式存储
     */
    @ApiModelProperty("模板value json形式存储")
    @TableField(value = "templateValue")
    private String templateValue;

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

    @ApiModelProperty("用户id")
    @TableField("userId")
    private Integer userId;

    /**
     * true 正常，false 冻结
     */
    @ApiModelProperty("true 正常，false 冻结")
    private Boolean status;

}
