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


/**
 * <p>
 *
 * </p>
 *
 * @author zxt
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("heatNetSource")
@ApiModel("热网及热源关系实体")
public class HeatNetSource {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 热网Id
     */
    @ApiModelProperty("热网Id")
    @TableField("heatNetId")
    private Integer  heatNetId;

    /**
     * 热源Id
     */
    @ApiModelProperty("热源Id")
    @TableField("heatSourceId")
    private Integer  heatSourceId;

    /**
     * 网源关系类型
     */
    @ApiModelProperty("1:网对源  2：源对网")
    @TableField("matchupType")
    private Integer  matchupType;
}
