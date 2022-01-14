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

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("systemInfo")
@ApiModel("系统信息表")
public class SystemInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id ;
    /**
     * 系统名称
     */
    @ApiModelProperty("系统名称")
    @TableField("systemName")
    private String systemName;
    /**
     * 图片存储
     */
    @ApiModelProperty("图片存储")
    @TableField("imageInfo")
    private byte[] imageInfo;
}
