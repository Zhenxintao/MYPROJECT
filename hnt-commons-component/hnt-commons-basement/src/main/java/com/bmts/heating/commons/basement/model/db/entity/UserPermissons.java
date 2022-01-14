package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author naming
 * @since 2020-07-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserPermissons implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父ID
     */

    private Integer pid;

    /**
     * 资源类型（1：菜单，2：按钮，3：操作）
     */
    private Integer type;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源标识（或者叫权限字符串）
     */
    private String code;

    /**
     * 资源URI
     */
    private String uri;

    /**
     * 序号
     */
    private Integer seq;
    
    private String createUser;

    private LocalDateTime createTime;

    private String updateUser;

    private LocalDateTime updateTime;


}
