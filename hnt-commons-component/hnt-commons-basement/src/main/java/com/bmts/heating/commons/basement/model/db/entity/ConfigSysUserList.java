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

/**
 * @ClassName: ConfigSysUserList
 * @Description: 自定义列表
 * @Author: pxf
 * @Date: 2020/12/11 15:55
 * @Version: 1.0
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("config_sys_user_list")
@ApiModel("自定义列表")
public class ConfigSysUserList implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    @TableField("userId")
    private Integer userId;

    /**
     * 自定义列表名称
     */
    @ApiModelProperty("自定义列表名称")
    @TableField("name")
    private String name;


    /**
     * 是否默认
     */
    @ApiModelProperty("是否默认")
    @TableField("isDefault")
    private Boolean isDefault;


    /**
     * 站点集合
     */
    @ApiModelProperty("站点集合")
    @TableField("staionIds")
    private String staionIds;


    /**
     * 组织机构集合
     */
    @ApiModelProperty("组织机构集合")
    @TableField("heatOrganizationIds")
    private String heatOrganizationIds;

    /**
     * 分类查询json
     */
    @ApiModelProperty("分类查询json")
    @TableField("typeJson")
    private String typeJson;


    /**
     * 选择参量
     */
    @ApiModelProperty("选择参量")
    @TableField("pointStandardIds")
    private String pointStandardIds;


}
