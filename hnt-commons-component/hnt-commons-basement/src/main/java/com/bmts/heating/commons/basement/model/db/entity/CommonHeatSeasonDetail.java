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
 * @since 2020-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("commonHeatSeasonDetail")
@ApiModel("供暖季详细表")
public class CommonHeatSeasonDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("开始时间")
    @TableField("start")
    private LocalDateTime start;

    @ApiModelProperty("结束时间")
    @TableField("end")
    private LocalDateTime end;

    @ApiModelProperty("阶段名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("供暖季id")
    @TableField("commonHeatSeasonId")
    private Integer commonHeatSeasonId;


}
