package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @author naming
 * @since 2020-07-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("角色")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色名称
     */
    private String roleName;
    //    @ApiModelProperty(hidden = true)
    private String roleCode;

    /**
     * 角色描述
     */
    private String roleDescription;
    @ApiModelProperty(hidden = true)
    private Integer createUser;
    @ApiModelProperty(hidden = true)
    private LocalDateTime createTime;
    @ApiModelProperty(hidden = true)
    private String updateUser;
    @ApiModelProperty(hidden = true)
    private LocalDateTime updateTime;


}
