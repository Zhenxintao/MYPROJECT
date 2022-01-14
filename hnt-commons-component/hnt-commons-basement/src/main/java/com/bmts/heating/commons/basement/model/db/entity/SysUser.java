package com.bmts.heating.commons.basement.model.db.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

@Data
@TableName("sys_user")
@ApiModel("用户信息")

public class SysUser implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    @JSONField(serialize = false)
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=([\\x21-\\x7e]+)[^a-zA-Z0-9]).{8,30}", message =
            "必须包含数字、小写大写字母、特殊符号、至少8个字符")
    private String password;
    @ApiModelProperty("真实姓名")
    private String nickname;
    private String email;
    @ApiModelProperty("是否启用")
    private Boolean status;
    private String username;
    //    @ApiModelProperty(hidden = true)
    @TableField("create_user")
    private Integer create_user;
    @ApiModelProperty(hidden = true)
    private String create_time;
    @ApiModelProperty(hidden = true)
    private int update_user;
    @ApiModelProperty(hidden = true)
    private String update_time;
    @TableField(exist = false)
    private List<Integer> roles;
    @TableField(exist = false)
    private List<Integer> orgs;
    private String phone;
    @TableField(value = "groupId")
    private int groupId;
    private String position;

    private int centerId;

}
